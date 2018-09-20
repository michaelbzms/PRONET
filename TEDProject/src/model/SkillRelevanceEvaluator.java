package model;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

import snowball.*;

public class SkillRelevanceEvaluator {

	private static List<String> stopwords = null; 
	private List<String> stemmedSkills;
	
	public SkillRelevanceEvaluator(String skillsString) {
		stemmedSkills = new ArrayList<String>();
		SnowballStemmer stemmer = new englishStemmer();
		List<String> skills = new ArrayList<String>(Arrays.asList(skillsString.split("[\\s,.;?!#_~|`(){}\\*\\[\\]]+")));		// regex to isolate words
		// convert skills to lowercase in ordered to be compared with stopwords:
		ListIterator<String> iterator = skills.listIterator();
	    while (iterator.hasNext()) {
	        iterator.set(iterator.next().toLowerCase());
	    }
		loadStopWords();
		skills.removeAll(stopwords);
		// Stem words using Snowball:
		String stemmedSkillsString = String.join(" ", skills) + " ";
		StringBuffer input = new StringBuffer();
    	for (int i = 0; i < stemmedSkillsString.length(); i++) {
			char ch = stemmedSkillsString.charAt(i);
			if (Character.isWhitespace((char) ch)) {
				if (input.length() > 0) {
					stemmer.setCurrent(input.toString());
					stemmer.stem();
					stemmedSkills.add(stemmer.getCurrent());
					input.delete(0, input.length());
				}
			} else {
				input.append(Character.toLowerCase(ch));
			}
		}
    	System.out.println(stemmedSkills);
	}
	
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
	
}
