package model;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


public class KNNWorkAds {
	
	/* --- Description ---------------------------------------------------------------------------------------------------------------------- *
	 * The ultimate goal of KKNWorkAds is to receive previously ordered by time work ads and to re-order them in a fashion which prioritizes  *
	 * work ads that are most similar to their K most "alike", in that they contain similar terms, work ads that the logged  in professional  *
	 * has already applied to.                                                                                                                *
	 * -------------------------------------------------------------------------------------------------------------------------------------- */
	
	private int K;
	private double[][][] applied_ads_vectors = null;
	private int[] applied_ads_IDs = null;
	private double[][][] candidate_ads_vectors = null;
	private int[] candidate_ads_IDs = null;            // the goal is to reorder appropriatelly this table

	
	public KNNWorkAds(int k) {
        if ( k <= 0 ) { 
        	System.err.println("K must be a positive number! Restoring K to default value of 3.");
        	this.K = 3;
        } else {
        	this.K = k;
        }
    }
	
	/* --- fit_applied_ads() ---------------------------------------------------- *
	 * Create a vector for each work ad that the logged in prof has applied to    *
	 * Each vector is a document-term matrix on its work ad's text                *
	 * -------------------------------------------------------------------------- */
    public int fit_applied_ads(DataBaseBridge db, int loggedprofID) {
    	if (db == null || !db.checkIfConnected()) return -1;
    	if (loggedprofID < 0) return -2;
    	// Construct applied_ads_vectors
    	List<WorkAd> appliedAds = db.getWorkAdsAppliedToBy(loggedprofID);
    	if ( appliedAds == null ) {              // should not happen
        	System.err.println("KNN fit error: null applied work ads");
        	return -3;
        } else if ( appliedAds.size() <= 1 ) {   // could happen
        	return 1;          // no need to run KNN then
    	} else if ( K > appliedAds.size() ) {    // could happen
        	System.out.println("Warning: KNN's K is > than the number of connected profs. Resetting to match their count.");   // DEBUG
        	this.K = appliedAds.size();
        }
    	applied_ads_vectors = new double[appliedAds.size()][][];
    	applied_ads_IDs = new int[appliedAds.size()];
    	int k = 0;
        for ( WorkAd ad : appliedAds ) {
        	applied_ads_IDs[k] = ad.getID();
        	//applied_ads_vectors[k] = new double[][]; /*sizeof(doc_term_matrix)*/
            //TODO: Create document-term matrix 'applied_ads_vectors[k]' here for 'ad'
            k++;
        }
    	return 0;
    }
	
    /* --- fit_candidates_ads() ------------------------------------------------- *
	 * Create a vector for each work ad from  the ones to reorder                 *
	 * Each vector is a document-term matrix on its work ad's text                *
	 * -------------------------------------------------------------------------- */
    public int fit_candidates_ads(DataBaseBridge db, int[] candidateAdsIDs) {
    	if (db == null || !db.checkIfConnected()) return -1;
    	if (candidateAdsIDs == null) return -2;
    	// Make a reference to 'candidateAdsIDs' 
    	this.candidate_ads_IDs = candidateAdsIDs;
    	// Construct candidate_ads_vectors
    	candidate_ads_vectors = new double[candidateAdsIDs.length][][];
    	for (int i = 0 ; i < candidateAdsIDs.length ; i++) {
    		//candidate_ads_vectors[i] = new double[][]; /*sizeof(doc_term_matrix)*/
    		//TODO: Create document-term matrix 'candidate_ads_vectors[i]' here for 'candidateAdsIDs[i]'
    	}
    	return 0;
    }
    
    /* --- reorderAdIDs() ------------------------------------------------------- *
	 * Reorder candidate_ads_IDs referenced in fit() such that:                   *
	 * candidate work ads whose K nearest neighbors from applied work ads         *
	 * receive an "ordering bonus" based on their amount of similarity            *
	 * (!) This bonus is added to the parameter 'firstBonus' before reordering    *
	 * -------------------------------------------------------------------------- */
    public int reorderAdIDs(DataBaseBridge db, double[] firstBonus){
    	if ( db == null || !db.checkIfConnected() ) return -1;
    	if (candidate_ads_IDs == null || candidate_ads_vectors == null || applied_ads_IDs == null || candidate_ads_vectors == null) {
    		System.err.println("KNN Error: Tried to reorder ArticleIDs without first calling fit?");
    		return -2;
    	}
    	if (firstBonus == null || firstBonus.length != candidate_ads_IDs.length ) {
    		System.err.println("KNN Error: invalid firstBonus parameter");
    		return -3;
    	}
    	// initialize candidate Work Ad bonuses to firstBonus
    	double[] adBonuses = new double[candidate_ads_IDs.length];
    	for (int i = 0 ; i < candidate_ads_IDs.length ; i++) {
    		adBonuses[i] = firstBonus[i];
    	}
    	// for each candidate work ad
    	for (int i = 0 ; i < candidate_ads_IDs.length ; i++) {
    		// find KNNs
    		double[][] K_Neighbours = findKNearestNeighbours(candidate_ads_vectors[i]);
    		// use KNNs to calculate the bonus of 'candidate_ads_IDs[i]' based on some bonus scheme on K_Neighbours' distances
    		double bonus = 0;
    		for (int j = 0 ; j < K ; j++) {
    			bonus += 1.0 / K_Neighbours[1][j];    // TODO: Use a different scheme?
    		}
    		adBonuses[i] += bonus;
    	}
    	// reorder candidate_ads_IDs base on their bonuses:    	
    	// 1. Wrap articleIDs and their bonus to a wrapper class 'Item'
    	class Item{
    		int adID;
    		double bonus;
    		public Item(int id, double score) { this.adID = id; this.bonus = score; }
    		public double getBonus() { return this.bonus; }
    		public int getId() { return this.adID; }

    	};
    	Item[] ItemsToSort = new Item[candidate_ads_IDs.length];
    	for (int i = 0 ; i < candidate_ads_IDs.length ; i++ ) {
    		ItemsToSort[i] = new Item(candidate_ads_IDs[i], adBonuses[i]);
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
    	// 4. Overwrite candidate_ads_IDs with ordered IDs in ItemsToSort
    	for (int i = 0 ; i < candidate_ads_IDs.length ; i++ ) {
    		// DEBUG:
    		// System.out.println("id: " + ItemsToSort[i].getId() + ", bonus: " + ItemsToSort[i].getBonus());
    		candidate_ads_IDs[i] = ItemsToSort[i].getId();
    	}
    	return 0;
    }
    
    //TODO: Do we care about the indexes on 'candidate_ads_IDs' of the K neighbours?
    // find the indexes of candidate_ads_IDs (in random order) and the distance of the K nearest 'candidate_ads_vectors' to 'candidate_ad_vector'
    private double[][] findKNearestNeighbours(double[][] candidate_ad_vector){
    	double[][] K_neighbours = new double[2][K];      // [0] is for the candidate ad's indexes (int) and [1] is for their distance measurement (double)
    	if ( this.K < applied_ads_vectors.length ) {     // O(applied_ads_vectors.length * K)  --> linear if K << applied_ads_vectors.length
	    	for (int j = 0 ; j < K ; j++ ) { 
	    		K_neighbours[0][j] = -1;   // (for ints)
	    		K_neighbours[1][j] = -1.0; // (for doubles)
	    	}
	    	for (int i = 0 ; i < applied_ads_vectors.length ; i++) {
	    		// calculate i-th's applied ad's distance to candidate_ad_vector
	    		double dist = distanceFunct(candidate_ad_vector, applied_ads_vectors[i]);
	    		// find the maximum currently stored distance between the K distances we keep track of (if found empty spot then use that immediately)
	    		double max = -1.0;
	    		int maxpos = -1;
	    		for (int j = 0 ; j < K ; j++) {
	    			if ( K_neighbours[1][j] == -1 ) {
	    				max = -1.0;      // signifying that there is still an empty spot on K_neighbours table
	    				maxpos = j;
	    				break;
	    			} 
	    			else if ( max == -1 || K_neighbours[1][j] > max ) {
	    				max = K_neighbours[1][j];
	    				maxpos = j;
	    			}
	    		}
	    		if ( maxpos < 0 ) { System.err.println("KNN Unexpected warning: could not find maximum?!"); break; }
	    		// check if current dist is < than the maximum of the current K distances and if it is then overwrite the maximum with this dist
	    		if ( max == -1 || dist < max) {
	    			K_neighbours[0][maxpos] = i;
	    			K_neighbours[1][maxpos] = dist;
	    		}
	    	}
    	} else {       // if K == N then this does O(applied_ads_vectors.length) time calculating all distances
    		for (int i = 0 ; i < applied_ads_vectors.length ; i++) {
    			// calculate i-th's applied ad vector's distance to candidate_ad_vector
	    		K_neighbours[0][i] = i;
	    		K_neighbours[1][i] = distanceFunct(candidate_ad_vector, applied_ads_vectors[i]);
    		}
    	}
    	return K_neighbours;
    }
    
    private double distanceFunct(double[][] v1, double[][] v2) {   // rename this accordingly
    	//TODO (TSIARAS)
    	return 0.0;
    }
    
}
