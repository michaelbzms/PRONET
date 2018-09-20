package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;

import snowball.*;

public class SkillRelevanceEvaluator {

	private static List<String> stopwords = null; 
	private List<String> stemmedSkills;
	
	private static void loadStopWords() {
		if (stopwords != null)	{		// stopwords have already been loaded
			return;
		}
		stopwords = new ArrayList<String>();
		BufferedReader br = null;
		try {
			String stopwordsFilePath = SkillRelevanceEvaluator.class.getResource(PropertiesManager.getProperty("stopwordsFile")).getPath();
			br = new BufferedReader(new FileReader(stopwordsFilePath));
			String line = "";
			String sep = PropertiesManager.getProperty("stopwordsFileSep");		// split on ',' by default
            while ((line = br.readLine()) != null) {
                String[] lineStopWords = line.split(sep);
                stopwords.addAll(Arrays.asList(lineStopWords));
            }
        } catch (FileNotFoundException e) {
        	System.out.println("Stopwords file not found");
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	
	private static List<String> getStemmedList(String textString) {		// removes stopwords and returns a list of stemmed words
		SnowballStemmer stemmer = new englishStemmer();
		List<String> textWords = new ArrayList<String>(Arrays.asList(textString.split("[\\s,.;?!#_~|`(){}\\*\\[\\]]+")));		// regex to isolate words
		// convert skills to lowercase in ordered to be compared with stopwords:
		ListIterator<String> iterator = textWords.listIterator();
	    while (iterator.hasNext()) {
	        iterator.set(iterator.next().toLowerCase());
	    }
		loadStopWords();
		textWords.removeAll(stopwords);
		// Stem words using Snowball:
		String stemmedWordsString = String.join(" ", textWords) + " ";
		List<String> stemmedWords = new ArrayList<String>();
		StringBuffer input = new StringBuffer();
    	for (int i = 0; i < stemmedWordsString.length(); i++) {
			char ch = stemmedWordsString.charAt(i);
			if (Character.isWhitespace((char) ch)) {
				if (input.length() > 0) {
					stemmer.setCurrent(input.toString());
					stemmer.stem();
					stemmedWords.add(stemmer.getCurrent());
					input.delete(0, input.length());
				}
			} else {
				input.append(Character.toLowerCase(ch));
			}
		}
    	return stemmedWords;
	}
	
	public SkillRelevanceEvaluator(String skillsString) {
		// we want to remove duplicates from skills:
    	stemmedSkills = new ArrayList<String>(new HashSet<String>(getStemmedList(skillsString)));
//    	System.out.println(stemmedSkills);
	}
	
	public double calculateScore(String adTitleString, String adDescriptionString) {
		List<String> stemmedAdTitle = new ArrayList<String>(new HashSet<String>(getStemmedList(adTitleString)));
		List<String> stemmedAdDescription = getStemmedList(adDescriptionString);
		double totalScore = 0.0;
		int skillFrequency;
		/* For each unique skill the score is incremented by 3 if it appears in the title and by 1 + log(x)
		 *  where x the frequency with which the skill appears in the ad description, if it appears there */
		for (String skill : stemmedSkills) {
			totalScore += Collections.frequency(stemmedAdTitle, skill) * 3;
			skillFrequency = Collections.frequency(stemmedAdDescription, skill);
			if (skillFrequency >= 1) {		
				totalScore += 1 + Math.log(skillFrequency);
			}
		}
		return totalScore;
	}
	
}
