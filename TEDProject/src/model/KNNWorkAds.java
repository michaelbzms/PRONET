package model;

public class KNNWorkAds {
	
	/* --- Description ---------------------------------------------------------------------------------------------------------------------- *
	 * The ultimate goal of KKNWorkAds is to receive previously ordered by time work ads and to re-order them in a fashion which prioritizes  *
	 * work ads that are most similar to their K most "alike", in that they contain similar terms, work ads that the logged  in professional  *
	 * has already applied to.                                                                                                                *
	 * -------------------------------------------------------------------------------------------------------------------------------------- */
	
	private int K;
	private double[][] applied_ads_vectors = null;
	private int[] applied_ads_IDs = null;
	private double[][] candidate_ads_vectors = null;
	private int[] candidate_ads_IDs = null;            // the goal is to reorder appropriatelly this table

	
	public KNNWorkAds(int k) {
        if ( k <= 0 ) { 
        	System.err.println("K must be a positive number! Restoring K to default value of 3.");
        	this.K = 3;
        } else {
        	this.K = k;
        }
    }
	
	/* --- fit() ---------------------------------------------------------------- *
	 * Create a vector for each work ad that the logged in prof has applied to    *
	 * as well as a vector for each work ad that is to be reordered.              *  
	 * These verctors are document-term matrices on each ad's text                *
	 * -------------------------------------------------------------------------- */
    public int fit(DataBaseBridge db, int[] candidateAdsIDs, int loggedprofID) {
    	if (db == null || !db.checkIfConnected()) return -1;
    	if (candidateAdsIDs == null || loggedprofID < 0) return -2;
    	this.candidate_ads_IDs = candidateAdsIDs;         // make a reference to 'candidateAdsIDs' 
    	//TODO
    	return 0;
    }
	 
    /* --- reorderAdIDs() ------------------------------------------------------- *
	 * Reorder candidate_ads_IDs referenced in fit() such that:                   *
	 * candidate work ads whose K nearest neighbors from applied work ads         *
	 * receive an "ordering bonus" based on their amount of similarity            *
	 * -------------------------------------------------------------------------- */
    public int reorderAdIDs(DataBaseBridge db){
    	if ( db == null || !db.checkIfConnected() ) return -1;
    	if (candidate_ads_IDs == null || candidate_ads_vectors == null || applied_ads_IDs == null || candidate_ads_vectors == null) {
    		System.err.println("KNN Error: Tried to reorder ArticleIDs without first calling fit?");
    		return -2;
    	}
    	//TODO
    	return 0;
    }
    
    private int[][] findKNearestNeighbours(){
    	//TODO (TSIARAS)
    	return null;
    }
    
    private int distanceFunct(int[] v1, int[] v2) {   // rename this accourdingly
    	//TODO (TSIARAS)
    	return 0;
    }
    
}
