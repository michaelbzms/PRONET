package model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class KNNArticles {
	
	/* --- Description ---------------------------------------------------------------------------------------------------------------------- *
	 * The ultimate goal of KKNArticles is to receive previously ordered by time articles ('ArticleIDs') and to re-order them in a fashion    *
	 * which prioritizes articles posted by K professionals who are most "alike" the logged in professional, in that they have interacted     *
	 * (by showing interest and/or commenting) on the same articles as the logged in professional                                             *
	 * -------------------------------------------------------------------------------------------------------------------------------------- */
	
	private int K;                                      // parameter K
    private int[][] connected_prof_vectors = null;      // vectors for each connected professional to logged in professionals
	private int[] connected_prof_IDs = null;            // the professional IDs for each prof of connected_prof_vector
    private int[] ArticleIDs = null;                    // the ArticleIDs that we show on logged in professional's HomePage (order matters)
	private int[] loggedprof_vector = null;             // the vector for the logged in professional
	
	public KNNArticles(int k){
        if ( k <= 0 ) { 
        	System.err.println("K must be a positive number! Restoring K to default value of 3.");
        	this.K = 3;
        } else {
        	this.K = k;
        }
    }
	
	/* --- fit() ---------------------------------------------------------------- *
	 * Create a vector for each CONNECTED professional to 'prof' such that:       *
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
        } else if ( connectedProfs.size() <= 1 ) {   // could happen
        	return 1;          // no need to run KNN then
    	} else if ( K > connectedProfs.size() ) {    // could happen
        	System.out.println("Warning: KNN's K is > than the number of connected profs. Resetting to match their count.");   // DEBUG
        	this.K = connectedProfs.size();
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
	 * -------------------------------------------------------------------------- */
    public int reorderArticleIDs(DataBaseBridge db){
    	if ( db == null || !db.checkIfConnected() ) return -1;
    	if (ArticleIDs == null || loggedprof_vector == null || connected_prof_vectors == null || connected_prof_IDs == null ) {
    		System.err.println("KNN Error: Tried to reorder ArticleIDs without first calling fit?");
    		return -2;
    	}
    	// initialize article bonuses to 0
    	HashMap<Integer, Integer> articleBonuses = new HashMap<Integer, Integer>();
    	for (int i = 0 ; i < ArticleIDs.length ; i++) {
    		articleBonuses.put(ArticleIDs[i], 0);
    	}
    	// find KNNs
    	int[][] K_neighbours = findKNearestNeighbours();
    	// use KNNs to give bonus to articles posted or shown Interest by them
    	for (int i = 0 ; i < K ; i++) {
    		List<Integer> articleIDsForBonus = db.getArticlesInvolvingProfID(connected_prof_IDs[K_neighbours[0][i]]);   // get articles posted by or shown interest by the i-th K-Neighbour
    		for (int articleID : articleIDsForBonus) {
    			Integer bonusptr = articleBonuses.get(articleID);
    			if ( bonusptr != null ) {                // only if articleID is already there
    				int bonus = bonusptr;
    				bonus += K_neighbours[1][i];         // get a bonus depending on how similar you are. TODO: find a better 'formula' than this?
    				articleBonuses.put(articleID, bonus);
    			}
    		}
    	}
    	// reorder ArticleIDs base on their bonuses:    	
    	// 1. Wrap articleIDs and their bonus to a wrapper class 'Item'
    	class Item{
    		int articleID;
    		int bonus;
    		public Item(int id, int score) { this.articleID = id; this.bonus = score; }
    		public int getBonus() { return this.bonus; }
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
    	for (int i = 0 ; i < ArticleIDs.length ; i++ ) {
    		// DEBUG:
    		// System.out.println("id: " + ItemsToSort[i].getId() + ", bonus: " + ItemsToSort[i].getBonus());
    		ArticleIDs[i] = ItemsToSort[i].getId();
    	}
    	return 0;
    }

    
    // find the indexes of connected_prof_IDs (in random order) and the distance of the K most similar 'connected_prof_vectors' to 'loggedprof_vector'
    private int[][] findKNearestNeighbours() {     // O(K*N*M) where N = connected_prof_vectors.length and M = ArticleIDs.length
    	int[][] K_neighbours = new int[2][K];      // [0] is for the professional's indexes and [1] is for their similarity measurement
    	for (int j = 0 ; j < K ; j++ ) { 
    		K_neighbours[0][j] = -1;
    		K_neighbours[1][j] = -1;
    	}
    	for (int i = 0 ; i < connected_prof_vectors.length ; i++) {
    		// calculate i-th's connected prof's similarity to logged in prof
    		int sim = similarity(loggedprof_vector, connected_prof_vectors[i]);    // O(M)
    		// find the minimum currently stored similarity between the K similarities we keep track of (if found empty spot then use that immediately)
    		int min = -1, minpos = -1;
    		for (int j = 0 ; j < K ; j++) {
    			if ( K_neighbours[1][j] == -1 ) {
    				min = -1;      // signifying that there is still an empty spot on K_neighbours table
    				minpos = j;
    				break;
    			} 
    			else if ( min == -1 || K_neighbours[1][j] < min ) {
    				min = K_neighbours[1][j];
    				minpos = j;
    			}
    		}
    		if ( minpos < 0 ) { System.err.println("KNN Unexpected warning: could not find minimum?!"); break; }
    		// check if current sim is > than the minimum of the current K similarities and if it is then overwrite the minimum with this sim
    		if ( min == -1 || sim > min) {
    			K_neighbours[1][minpos] = sim;
    			K_neighbours[0][minpos] = i;
    		}
    	}
    	return K_neighbours;
    }	
	
    /* --- similarity() --------------------------------------------------------- *
	 * Calculate the similarity between two professional vectors such that:       *
	 * For each characteristic:                                                   *
	 * 	if the i-th characteristic is > 0 for BOTH vectors then                   *
	 * 		add their values to the similarity measure                            *
	 * -------------------------------------------------------------------------- */
    private int similarity(int[] v1, int[] v2) {
    	if ( v1 == null || v2 == null ) return -1;
    	int similarity = 0;
    	for (int i = 0 ; i < ArticleIDs.length ; i++) {
    		if ( v1[i] > 0 && v2[i] > 0 ) {
    			similarity += v1[i] + v2[i];
    		}
    	}
    	return similarity;
    }
    
}
