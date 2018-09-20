package model;

public class KNNArticles {
	
	/* --- Description ---------------------------------------------------------------------------------------------------------------------- *
	 * The ultimate goal of KKNArticles is to receive previously ordered by time articles ('ArticleIDs') and to re-order them in a fashion    *
	 * which prioriotizes articles posted by K professionals who are most "alike" the logged in professional, in that they have interacted    *
	 * (by showing interest and/or commenting) on the same articles as the logged in professional                                             *
	 * -------------------------------------------------------------------------------------------------------------------------------------- */
	
	private int K;                                      // parameter K
    private int[][] connected_prof_vectors = null;      // vectors for each connected professional to logged in professionals
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
	 * each i-th characteristic will be an integer of: 0                          *
	 * 		+3 if the connected professional was interested on the i-th article   *
	 * 		+1 for every comment of his on the i-th article                       *
	 * -------------------------------------------------------------------------- */
    public void fit(int[] articleIDs, int loggedprofID){
        if ( K > articleIDs.length ){
            System.err.println("Warning: KNN fit's data set contains less than K records! K is adjusted to data_set.length!");
            K = articleIDs.length;
        }
        this.ArticleIDs = articleIDs;
        //TODO
        // 1. Construct loggedprof_vector from loggedprofID and given ArticleIDs
        loggedprof_vector = null;
        // 2. Construct connected_prof_vectors for given ArticleIDs and loggedprofID
        connected_prof_vectors = null;
    }
    
    /* --- reorderArticleIDs() -------------------------------------------------- *
	 * Reorder ArticleIDs referenced in fit() such that:                          *
	 * articles posted by or shown interest by the K nearest neighbours           *
	 * receive an "ordering bonus" based on their order of similarity             *
	 * -------------------------------------------------------------------------- */
    public int[] reorderArticleIDs(){
    	if (ArticleIDs == null) {
    		System.err.println("KNN Error: Tried to reorder ArticleIDs without first calling fit?");
    		return null;
    	}
    	//TODO
    	return null;
    }

    
    // find the indexes (in order) of the K most similar 'connected_prof_vectors' to 'loggedprof_vector'
    private int[] findKNearestNeighbours() {
    	//TODO
    	return null;
    }	
	
    /* --- similarity() --------------------------------------------------------- *
	 * Calculate the similarity between two professional vectors such that:       *
	 * For each characteristic:                                                   *
	 * 	if the i-th characteristic is > 0 for BOTH vectors then                   *
	 * 		add their values to the similarity measure                            *
	 * -------------------------------------------------------------------------- */
    private int similarity(int[] v1, int v2[]) {
    	//TODO
    	return 0;
    }
    
}
