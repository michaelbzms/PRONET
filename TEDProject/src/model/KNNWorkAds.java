package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KNNWorkAds {
	
	/* --- Description ---------------------------------------------------------------------------------------------------------------------- *
	 * The ultimate goal of KKNWorkAds is to receive previously ordered by time work ads and to re-order them in a fashion which prioritizes  *
	 * work ads that are most similar to their K most "alike", in that they contain similar terms, work ads that the logged  in professional  *
	 * has already applied to.                                                                                                                *
	 * -------------------------------------------------------------------------------------------------------------------------------------- */
	
	private int K;
	private Map<String, Double>[] applied_ads_vectors = null;
	private int[] applied_ads_IDs = null;
	private Map<String, Double>[] candidate_ads_vectors = null;
	private int[] candidate_ads_IDs = null;            // the goal is to reorder appropriatelly this table
	private final int TitleWeight = 3;
	
	
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
        } else if ( appliedAds.size() <= 0 ) {   // could happen
        	return 1;
    	} else if ( K > appliedAds.size() ) {    // could happen
        	System.err.println("Warning: KNN's K is " + K + " which is larger than the number of applied ads, which is " + appliedAds.size() + ". Resetting to match their count.");
        	this.K = appliedAds.size();
        }
    	applied_ads_vectors = new Map[appliedAds.size()];
    	applied_ads_IDs = new int[appliedAds.size()];
    	int k = 0;
        for ( WorkAd ad : appliedAds ) {
        	applied_ads_IDs[k] = ad.getID();
            // Create document-term matrix 'applied_ads_vectors[k]' here for 'ad'
        	List<String> stemmedWords = SkillRelevanceEvaluator.getStemmedList(String.join(" ", Collections.nCopies(TitleWeight, ad.getTitle())) + " " + ad.getDescription());
        	applied_ads_vectors[k] = DocumentTermFrequency.tf(stemmedWords);
            k++;
        }
    	return 0;
    }
	
    /* --- fit_candidates_ads() ------------------------------------------------- *
	 * Create a vector for each work ad from  the ones to reorder                 *
	 * Each vector is a document-term matrix on its work ad's text                *
	 * -------------------------------------------------------------------------- */
    public int fit_candidates_ads(DataBaseBridge db, int[] candidateAdsIDs, boolean constructVectors) {
    	if (db == null || !db.checkIfConnected()) return -1;
    	if (candidateAdsIDs == null) return -2;
    	// Make a reference to 'candidateAdsIDs' 
    	this.candidate_ads_IDs = candidateAdsIDs;
    	// Construct candidate_ads_vectors but only if need be
    	if ( constructVectors ) {
	    	candidate_ads_vectors = new Map[candidate_ads_IDs.length];
	    	for (int i = 0 ; i < candidateAdsIDs.length ; i++) {
	    		// get Work Ad for 'candidateAdsIDs[i]'
	    		WorkAd ad = db.getWorkAd(candidateAdsIDs[i]);
	    		if ( ad == null ) {
	            	System.err.println("KNN fit error: null work ad for id " + candidateAdsIDs[i]);
	            	candidate_ads_vectors = null;
	            	return -3;
	    		}
	    		// Create document-term matrix 'candidate_ads_vectors[i]' here for 'candidateAdsIDs[i]'
	    		List<String> stemmedWords = SkillRelevanceEvaluator.getStemmedList(String.join(" ", Collections.nCopies(TitleWeight, ad.getTitle())) + " " + ad.getDescription());
	    		candidate_ads_vectors[i] = DocumentTermFrequency.tf(stemmedWords);
	    	}
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
    	if ( candidate_ads_IDs == null || (firstBonus != null && firstBonus.length != candidate_ads_IDs.length) ) {
    		System.err.println("KNN Error: invalid firstBonus parameter or fit ad candidates has not been called");
    		return -2;
    	}
    	if (firstBonus == null) {    // no score from skills
    		firstBonus = new double[candidate_ads_IDs.length];
	    	for (int i = 0 ; i < candidate_ads_IDs.length ; i++) {       	// for each candidate work ad
	    		firstBonus[i] = 0.0;
	    	}
    	}
    	double[] CombinedAdBonuses = new double[candidate_ads_IDs.length];
    	if ( applied_ads_IDs == null || applied_ads_vectors == null || candidate_ads_vectors == null ) {   // applied ads have not been fitted so only order by firstBonus score
    		for (int i = 0 ; i < candidate_ads_IDs.length ; i++) { 
	    		CombinedAdBonuses[i] = firstBonus[i];                       // where firstBonus in [0,1]
	    	}
    	} else {
	    	double[] adBonuses = new double[candidate_ads_IDs.length];
	    	double max = -1.0;
	    	for (int i = 0 ; i < candidate_ads_IDs.length ; i++) {       	// for each candidate work ad
	    		// find KNN similarities
	    		double[] K_Neighbours = findKNearestNeighbours(candidate_ads_vectors[i]);
	    		// use KNNs to calculate the bonus of 'candidate_ads_IDs[i]' based on some bonus scheme on K_Neighbours' similarities
	    		double bonus = 0.0;
	    		for (int j = 0 ; j < K ; j++) {
	    			bonus += K_Neighbours[j];
	    		}
	    		adBonuses[i] = bonus;
	    		if (bonus > max) {
	    			max = bonus;
	    		}
	    	}
	    	if ( max == 0 ) {
	    		for (int i = 0 ; i < candidate_ads_IDs.length ; i++) { 
		    		CombinedAdBonuses[i] = firstBonus[i];                             // where firstBonus in [0,1]
		    	}
	    	} else {
		    	for (int i = 0 ; i < candidate_ads_IDs.length ; i++) { 
		    		CombinedAdBonuses[i] = firstBonus[i] + (adBonuses[i] / max);      // where firstBonus in [0,1] and (adBonuses[i] / max) in [0,1]
		    	}
	    	}
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
    		ItemsToSort[i] = new Item(candidate_ads_IDs[i], CombinedAdBonuses[i]);
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
    	// DEBUG:
		//System.out.println("___WorkAd bonuses_______________ ");
    	for (int i = 0 ; i < candidate_ads_IDs.length ; i++ ) {
    		// DEBUG:
    		//System.out.println("id: " + ItemsToSort[i].getId() + ", bonus: " + ItemsToSort[i].getBonus());
    		candidate_ads_IDs[i] = ItemsToSort[i].getId();
    	}
    	return 0;
    }
    
    // find the similities of the K nearest 'candidate_ads_vectors' to 'candidate_ad_vector'
    private double[] findKNearestNeighbours(Map<String, Double> candidate_ad_vector){
    	double[] K_neighbours = new double[K];
    	if ( this.K < applied_ads_vectors.length ) {     // O(applied_ads_vectors.length * K)  --> linear if K << applied_ads_vectors.length
	    	for (int j = 0 ; j < K ; j++ ) { 
	    		K_neighbours[j] = -1.0;
	    	}
	    	for (int i = 0 ; i < applied_ads_vectors.length ; i++) {
	    		// calculate i-th's applied ad's similarity to candidate_ad_vector
	    		double sim = documentSimilarity(candidate_ad_vector, applied_ads_vectors[i]);
	    		// find the minimum currently stored similarity between the K similarities we keep track of (if found empty spot then use that immediately)
	    		double min = -1.0;
	    		int minpos = -1;
	    		for (int j = 0 ; j < K ; j++) {
	    			if ( K_neighbours[j] == -1 ) {
	    				min = -1.0;      // signifying that there is still an empty spot on K_neighbours table
	    				minpos = j;
	    				break;
	    			} 
	    			else if ( min == -1 || K_neighbours[j] < min ) {
	    				min = K_neighbours[j];
	    				minpos = j;
	    			}
	    		}
	    		if ( minpos < 0 ) { System.err.println("KNN Unexpected warning: could not find minimum?!"); break; }
	    		// check if current sim is > than the minimum of the current K similarities and if it is then overwrite the minimum with this sim
	    		if ( min == -1 || sim > min) {
	    			K_neighbours[minpos] = sim;
	    		}
	    	}
    	} else {       // if K == N then this does O(applied_ads_vectors.length) time calculating all distances
    		for (int i = 0 ; i < applied_ads_vectors.length ; i++) {
    			// calculate i-th's applied ad vector's similarity to candidate_ad_vector
	    		K_neighbours[i] = documentSimilarity(candidate_ad_vector, applied_ads_vectors[i]);
    		}
    	}
    	return K_neighbours;
    }
    
    private double documentSimilarity(Map<String, Double> v1map, Map<String, Double> v2map) {   // rename this accordingly
    	// construct term frequency vectors only for terms that exist on both maps
    	ArrayList<Double> v1list = new ArrayList<Double>(), v2list = new ArrayList<Double>();
    	for (Map.Entry<String, Double> term : v1map.entrySet()) {   // iterate v1map
    		if ( v2map.containsKey(term.getKey()) ) {               // if term also on v2map then add them to vectors-to-be
    			v1list.add(term.getValue());
    			v2list.add(v2map.get(term.getKey()));
    		}
    	}
    	Double[] v1 = new Double[v1list.size()];
    	v1 = v1list.toArray(v1);
    	Double[] v2 = new Double[v1list.size()];
    	v2 = v2list.toArray(v2); 
    	// return cosineSimilarity between those vectors
    	return cosineSimilarity(v1, v2);
    }
    
    public static double cosineSimilarity(Double[] vectorA, Double[] vectorB) {
        if ( vectorA.length != vectorB.length ) {
        	System.err.println("cosineSimilarity warning: mismatching vector lengths");
        	return -2;
        }
    	double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        double squares = Math.sqrt(normA) * Math.sqrt(normB);
        return ((squares == 0.0) ? dotProduct : (dotProduct / squares));
    }
    
}
