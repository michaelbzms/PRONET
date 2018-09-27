package model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class KNNArticles {
	
	/* --- Description ------------------------------------------------------------------------------------------------------------------------- *
	 * The ultimate goal of KKNArticles is to receive previously ordered by time articles ('ArticleIDs') and to re-order them in a fashion       *
	 * which prioritizes articles posted by or shown interest by the K connected professionals who are most "alike" the logged in professional,  *
	 * in that they have interacted (by showing interest and/or commenting) on the same articles as the logged in professional                   *
	 * ----------------------------------------------------------------------------------------------------------------------------------------- */
	
	private int K;                                      // parameter K
    private int[][] connected_prof_vectors = null;      // vectors for each connected professional to logged in professionals
	private int[] connected_prof_IDs = null;            // the professional IDs for each prof of connected_prof_vector
    private int[] ArticleIDs = null;                    // the ArticleIDs that we show on logged in professional's HomePage (order matters)
	private int[] loggedprof_vector = null;             // the vector for the logged in professional
	private final double simScalingFactor = 3.0;        // how much important the difference in similarity should be versus the extra bonuses we give (who ara analogous to K) (should be sth 2 - 5)
	private double[][] K_neighbours = null;
	
	public KNNArticles(int k){
        if ( k <= 0 ) { 
        	System.err.println("K must be a positive number! Restoring K to default value of 3.");
        	this.K = 3;
        } else {
        	this.K = k;
        }
    }
	
	/* --- fit() ---------------------------------------------------------------- *
	 * Create a vector for each connected professional to 'prof' such that:       *
	 * each i-th characteristic (for each ArticleIDs[i]) will be an integer of: 0 *
	 * 		+3 if the connected professional was interested on the i-th article   *
	 * 		+1 for every comment of his on the i-th article                       *
	 * -------------------------------------------------------------------------- */
    public int fit(DataBaseBridge db, int[] articleIDs, int loggedprofID){
    	if (db == null || !db.checkIfConnected()) return -1;
    	if (articleIDs == null || loggedprofID < 0) return -2;
        // Make a reference to ArticleIDs to be ordered
    	this.ArticleIDs = articleIDs;
        // 1. Construct connected_prof_vectors for given ArticleIDs and loggedprofID
        List<Professional> connectedProfs = db.getConnectedProfessionalsFor(loggedprofID);
        if ( connectedProfs == null ) {              // should not happen
        	System.err.println("KNN fit error: null connected professionals");
        	return -3;
        } else if ( connectedProfs.size() <= 0 ) {   // could happen
        	return 1;          // no need to run KNN then (only articles posted by loggin professional will be shown on HomePage)
    	} else if ( K > connectedProfs.size() ) {    // could happen if the logged in professional has less connected profs than our K value
        	this.K = connectedProfs.size();          // in which case reset K to |connected professionals| 
        }
        connected_prof_vectors = new int[connectedProfs.size()][];
        connected_prof_IDs = new int[connectedProfs.size()];
        int k = 0;
        for ( Professional connected_prof : connectedProfs ) {
        	connected_prof_IDs[k] = connected_prof.getID();
        	connected_prof_vectors[k] = new int[ArticleIDs.length];
            for (int i = 0 ; i < ArticleIDs.length ; i++) {
            	connected_prof_vectors[k][i] = ( (db.getInterest(ArticleIDs[i], connected_prof.getID())) ? 3 : 0 ) + db.getNumberOfComments(ArticleIDs[i], connected_prof.getID());
            }
            k++;
        }
        // 2. Construct loggedprof_vector from loggedprofID and given ArticleIDs
        loggedprof_vector = new int[ArticleIDs.length];
        for (int i = 0 ; i < ArticleIDs.length ; i++) {
        	loggedprof_vector[i] = ( (db.getInterest(ArticleIDs[i], loggedprofID)) ? 3 : 0 ) + db.getNumberOfComments(ArticleIDs[i], loggedprofID);
        }
        return 0;
    }
    
    /* --- reorderArticleIDs() -------------------------------------------------- *
	 * Reorder ArticleIDs referenced in fit() such that:                          *
	 * articles posted by or shown interest by the K nearest neighbors            *
	 * receive an "ordering bonus" based on their amount of similarity            *
	 * Also: if the logged in prof has NOT shown interest to them then they       *
	 * receive an extra bonus (as then he probably hasn't seen them yet)          *
	 * -------------------------------------------------------------------------- */
    public int reorderArticleIDs(DataBaseBridge db, int loggedInProfID, boolean giveRecentArticlesBonus, boolean giveNotLikedArticlesBonus, boolean givePopularArticlesBonus){
    	if ( db == null || !db.checkIfConnected() ) return -1;
    	if (ArticleIDs == null || loggedprof_vector == null || connected_prof_vectors == null || connected_prof_IDs == null ) {
    		System.err.println("KNN Error: Tried to reorder ArticleIDs without first calling fit?");
    		return -2;
    	}
    	// initialize article bonuses to 0
    	HashMap<Integer, Double> articleBonuses = new HashMap<Integer, Double>();
    	for (int i = 0 ; i < ArticleIDs.length ; i++) {
    		articleBonuses.put(ArticleIDs[i], 0.0);
    	}
    	// find KNNs connected professionals
    	K_neighbours = findKNearestNeighbours();              // O(K*N) if K < N and O(N) if K == N, where N = |connected profs|
    	// use KNNs connected professionals to give bonus to articles posted or shown Interest by them
    	for (int i = 0 ; i < K ; i++) {                       // O(K*M), where M = ArticleIDs.length (worst-case)
    		// get articles posted by or shown interest by the i-th K-Neighbour
    		List<Integer> articleIDsForBonus = db.getArticlesInvolvingProfID(connected_prof_IDs[(int) K_neighbours[i][0]]);
    		for (int articleID : articleIDsForBonus) {
    			Double bonusptr = articleBonuses.get(articleID);
    			if ( bonusptr != null ) {
    				double bonus = bonusptr;
    				bonus += K_neighbours[i][1];              // get a bonus depending on how similar the respective professional is
    				articleBonuses.put(articleID, bonus);
    			}
    		}
    	}
    	// Give extra bonuses (aside from KNN's similarity) to:
    	// 1. articles that the loggedin prof has NOT already shown Interest to (except from his own articles)
    	// 2. articles that are popular
    	// 3. articles that are recent
    	if ( giveNotLikedArticlesBonus || giveRecentArticlesBonus || givePopularArticlesBonus) {
    		// the higher the K the higher the potential bonuses can be => the higher the extra bonuses should be
	    	double NOT_LIKED_BONUS = 0.25 * K;         // CONFIG
	    	double POPULARITY_BONUS = 0.5 * K;         // CONFIG
	    	for (int i = 0 ; i < ArticleIDs.length ; i++) {   // O(N)
	    		Double bonusptr = articleBonuses.get(ArticleIDs[i]);
				if ( bonusptr != null ) {
					double bonus = bonusptr;
					// not "liked" yet bonus
					if ( giveNotLikedArticlesBonus && !db.getInterest(ArticleIDs[i], loggedInProfID) && !(loggedInProfID == db.getArticleAuthorID(ArticleIDs[i])) ) {
						bonus += NOT_LIKED_BONUS;
					}
					// popular article bonus
					if ( givePopularArticlesBonus ) {
						double registeredProfCount = db.getNumberOfRegisteredProfessionals();
						double interestedProfCount = db.getNumberOfInterestedProfessionals(ArticleIDs[i]);
						bonus += POPULARITY_BONUS * (interestedProfCount / registeredProfCount);   // something in the range [0, POPULARITY_BONUS]
					}
					// time posted bonus
					if ( giveRecentArticlesBonus ) {
						Article article = db.getArticleAndDate(ArticleIDs[i]);
						if ( article != null ) {
							long hoursSincePosted = MyUtil.getHoursAgo(article.getPostedDate());
							if ( hoursSincePosted < 1 ) {
								bonus += 0.75 * K;
							} else if ( hoursSincePosted < 12 ) {
								bonus += 0.25 * K;
							} else if ( hoursSincePosted < 24 ) {
								bonus += 0.12 * K;
							}
						}
					}
					articleBonuses.put(ArticleIDs[i], bonus);
				}
	    	}
    	}
    	if ( giveRecentArticlesBonus ) {
	    	List<Article> loggedInProfArticles = db.getProfArticlesAndDates(loggedInProfID);
	    	if (loggedInProfArticles != null && loggedInProfArticles.size() > 0) {
		    	for ( Article article : loggedInProfArticles ) {
		    		Double bonusptr = articleBonuses.get(article.getID());
					if ( bonusptr != null ) {
						double bonus = bonusptr;
						long hoursSincePosted = MyUtil.getHoursAgo(article.getPostedDate());
						if ( hoursSincePosted < 1 ) {
							bonus += 1.25 * K;
						} else if ( hoursSincePosted < 12 ) {
							bonus += 0.75 * K;
						} else if ( hoursSincePosted < 24 ) {
							bonus += 0.25 * K;
						} else if ( hoursSincePosted < 48 ) {
							bonus += 0.12 * K;
						} else continue;  // skip this article thus giving it no bonus
						articleBonuses.put(article.getID(), bonus);
					}
		    	}
	    	}
    	}
    	// reorder ArticleIDs base on their bonuses:    	
    	// 1. Wrap articleIDs and their bonus to a wrapper class 'Item'
    	class Item{
    		int articleID;
    		double bonus;
    		public Item(int id, double score) { this.articleID = id; this.bonus = score; }
    		public double getBonus() { return this.bonus; }
    		public int getId() { return this.articleID; }

    	};
    	Item[] ItemsToSort = new Item[ArticleIDs.length];
    	for (int i = 0 ; i < ArticleIDs.length ; i++ ) {
    		ItemsToSort[i] = new Item(ArticleIDs[i], articleBonuses.get(ArticleIDs[i]));
    	}
    	// 2. Overload a comperator for this class
    	class ItemComparator implements Comparator {   // decending order
    		@Override
    		public int compare( Object o1, Object o2 ) {
    			Item i1 = (Item)o1;
    			Item i2 = (Item)o2;
    			if ( i1.getBonus() > i2.getBonus() ) return -1;
    			else if ( i1.getBonus() < i2.getBonus() ) return 1;
    			else return 0;
			 }
		}
    	// 3. Sort the wrapped class based on 'bonus' field
    	Arrays.sort(ItemsToSort, new ItemComparator());
    	// 4. Overwrite ArticleIDs with ordered IDs in ItemsToSort
    	// DEBUG:
    	System.out.println("___Articles bonuses_______________ ");
    	for (int i = 0 ; i < ArticleIDs.length ; i++ ) {
    		// DEBUG:
    		System.out.println("id: " + ItemsToSort[i].getId() + ", bonus: " + ItemsToSort[i].getBonus());
    		ArticleIDs[i] = ItemsToSort[i].getId();
    	}
    	return 0;
    }

    
    // find the indexes of connected_prof_IDs (in random order) and the distance of the K most similar 'connected_prof_vectors' to 'loggedprof_vector'
    private double[][] findKNearestNeighbours() {           // O(K*N*M) where N = connected_prof_vectors.length and M = ArticleIDs.length
    	double[][] K_neighbours = new double[K][2];         // [0] is for the professional's indexes and [1] is for their similarity measurement
    	if ( this.K < connected_prof_vectors.length ) {     // K < N  -> O(K*N*M)
	    	for (int j = 0 ; j < K ; j++ ) { 
	    		K_neighbours[j][0] = -1;
	    		K_neighbours[j][1] = -1;
	    	}
	    	for (int i = 0 ; i < connected_prof_vectors.length ; i++) {
	    		// calculate i-th's connected prof's similarity to logged in prof
	    		double sim = similarity(loggedprof_vector, connected_prof_vectors[i]);    // O(M)
	    		// find the minimum currently stored similarity between the K similarities we keep track of (if found empty spot then use that immediately)
	    		double min = -1.0;
	    		int minpos = -1;
	    		for (int j = 0 ; j < K ; j++) {
	    			if ( K_neighbours[j][1] == -1 ) {
	    				min = -1.0;      // signifying that there is still an empty spot on K_neighbours table
	    				minpos = j;
	    				break;
	    			} 
	    			else if ( min == -1 || K_neighbours[j][1] < min ) {
	    				min = K_neighbours[j][1];
	    				minpos = j;
	    			}
	    		}
	    		if ( minpos < 0 ) { System.err.println("KNN Unexpected warning: could not find minimum?!"); break; }
	    		// check if current sim is > than the minimum of the current K similarities and if it is then overwrite the minimum with this sim
	    		if ( min == -1.0 || sim > min) {
	    			K_neighbours[minpos][0] = i;
	    			K_neighbours[minpos][1] = sim;
	    		}
	    	}
    	} else {       // K == N -> O(N*M) (optimization)
    		// if K == N then we must calculate and save all similarities to connected_prof_vectors
    		for (int i = 0 ; i < connected_prof_vectors.length ; i++) {
    			// calculate i-th's connected prof's similarity to logged in prof
	    		K_neighbours[i][0] = i;
	    		K_neighbours[i][1] = similarity(loggedprof_vector, connected_prof_vectors[i]);    // O(M)
    		}
    	}
    	return K_neighbours;
    }	
	
    /* --- similarity() --------------------------------------------------------- *
	 * Calculate the similarity between two professional vectors such that:       *
	 * For each characteristic (aka train set's article):                         *
	 * 	if the i-th characteristic is > 0 for BOTH vectors then                   *
	 * 		add their values to the similarity measure                            *
	 * all diveded by the length of the vectors for better scaling                *
	 * -------------------------------------------------------------------------- */
    private double similarity(int[] v1, int[] v2) {
    	if ( v1 == null || v2 == null || v1.length != ArticleIDs.length || v2.length != ArticleIDs.length ) return -1;
    	if ( ArticleIDs.length == 0 ) return 0;
    	double similarity = 0;
    	for (int i = 0 ; i < ArticleIDs.length ; i++) {
    		if ( v1[i] > 0 && v2[i] > 0 ) {
    			similarity += v1[i] + v2[i];
    		}
    	}
    	return (simScalingFactor * similarity) / ((double) ArticleIDs.length);
    }
    
    
    public int[] getTopKProfessionals(int maxNumber) {
    	if (K_neighbours == null || connected_prof_IDs == null) {
    		if (connected_prof_IDs == null) System.err.println("KNN Error: Tried to get KNN's ids without first running KNN to find them");   // else KNN might have not been needed ex: logged prof has no connections
    		return null;
    	}
    	if (maxNumber > K || maxNumber <= 0) { maxNumber = K; }
    	int[] KNNProfIDs = new int[maxNumber];
    	// sort KNNs based on their similarity
    	class KNNComparator implements Comparator {   // decending order
    		@Override
    		public int compare( Object o1, Object o2 ) {
    			double[] i1 = (double[]) o1;
    			double[] i2 = (double[]) o2;
    			if ( i1[1] > i2[1] ) return -1;
    			else if ( i1[1] < i2[1] ) return 1;
    			else return 0;
			 }
		}
    	Arrays.sort(K_neighbours, new KNNComparator());
		// DEBUG:
    	System.out.println("___KNNs and their similarities________");
    	for (int i = 0 ; i < maxNumber ; i++) {
    		KNNProfIDs[i] = connected_prof_IDs[(int) K_neighbours[i][0]];
    		// DEBUG:
    		System.out.println((i+1) + ". id = " + KNNProfIDs[i] + ", similarity = " + K_neighbours[i][1]);
    	}
    	return KNNProfIDs;
    }
    
}
