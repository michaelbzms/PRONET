package model;

import java.util.*;

// Link: "https://github.com/wpm/tfidf/blob/master/src/main/java/com/github/wpm/tfidf/TfIdf.java"


public class DocumentTermFrequency {
	
	/**
     * Word count method used for term frequencies
     */
    public enum TfType {
        /**
         * Term frequency
         */
        NATURAL,
        /**
         * Log term frequency plus 1
         */
        LOGARITHM,
        /**
         * 1 if term is present, 0 if it is not
         */
        BOOLEAN
    }

    /**
     * Normalization of the tf-idf vector
     */
    public enum Normalization {
        /**
         * Do not normalize the vector
         */
        NONE,
        /**
         * Normalize by the vector elements added in quadrature
         */
        COSINE
    }

    /**
     * Term frequency for a single document
     *
     * @param document bag of terms
     * @param type     natural or logarithmic
     * @param <TERM>   term type
     * @return map of terms to their term frequencies
     */
    public static <TERM> Map<TERM, Double> tf(Collection<TERM> document, TfType type) {
        Map<TERM, Double> tf = new HashMap<>();
        for (TERM term : document) {
            tf.put(term, tf.getOrDefault(term, 0.0) + 1);
        }
        if (type != TfType.NATURAL) {
            for (TERM term : tf.keySet()) {
                switch (type) {
                    case LOGARITHM:
                        tf.put(term, 1 + Math.log(tf.get(term)));
                        break;
                    case BOOLEAN:
                        tf.put(term, tf.get(term) == 0.0 ? 0.0 : 1.0);
                        break;
                }
            }
        }
        return tf;
    }

    /**
     * Natural term frequency for a single document
     *
     * @param document bag of terms
     * @param <TERM>   term type
     * @return map of terms to their term frequencies
     */
    public static <TERM> Map<TERM, Double> tf(Collection<TERM> document) {
        return tf(document, TfType.NATURAL);
}
	
}
