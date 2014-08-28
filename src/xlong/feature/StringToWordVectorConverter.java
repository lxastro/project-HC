/**
 * Project : Classify URLs
 */
package xlong.feature;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import xlong.data.TypeMapIO;
import xlong.feature.tokenizer.SingleWordTokenizer;
import xlong.feature.tokenizer.Tokenizer;

/**
 * Class to convert string to word vectore
 * 
 * @author Xiang Long (longx13@mails.tsinghua.edu.cn)
 */
public class StringToWordVectorConverter {
	
	private Tokenizer tokenizer;
	private HashSet<String> stopwords;
	private boolean s_lowerCaseTokens = true;
	private boolean s_useStoplist = false;
    private boolean s_OutputCounts = false;
	private int s_wordsToKeep = 10000;
	private int s_minTermFreq = 1;

	private TreeMap<String, Integer> dictionary;
	private TreeMap<String, Count> wordMap;

	private class Count {
		int count = 0;

		public Count(int x) {
			count = x;
		}

		public void addOne() {
			count++;
		}
	}
	
	public StringToWordVectorConverter(Tokenizer tokenizer, int wordsToKeep) {
		this.wordMap = new TreeMap<String, Count>();
		this.tokenizer = tokenizer;
		s_wordsToKeep = wordsToKeep;
	}

	private static void sortArray(int[] array) {
		int i, j, h, N = array.length - 1;

		for (h = 1; h <= N / 9; h = 3 * h + 1) {
			;
		}

		for (; h > 0; h /= 3) {
			for (i = h + 1; i <= N; i++) {
				int v = array[i];
				j = i;
				while (j > h && array[j - h] > v) {
					array[j] = array[j - h];
					j -= h;
				}
				array[j] = v;
			}
		}
	}

	public void determineDictionary() {
		// Figure out the minimum required word frequency
		int array[] = new int[wordMap.size()];
		Iterator<Count> it = wordMap.values().iterator();
		int pos = 0;
		int prune;
		while (it.hasNext()) {
			Count count = it.next();
			array[pos] = count.count;
			pos++;
		}
		// sort the array
		sortArray(array);
		if (array.length < s_wordsToKeep) {
			// if there aren't enough words, set the threshold to minFreq
			prune = s_minTermFreq;
		} else {
			// otherwise set it to be at least minFreq
			prune = Math
					.max(s_minTermFreq, array[array.length - s_wordsToKeep]);
		}

		// Add the word vector attributes
		TreeMap<String, Integer> newDictionary = new TreeMap<String, Integer>();
		Iterator<Entry<String, Count>> ite = wordMap.entrySet().iterator();
		
		int index = 0;
		while (ite.hasNext()) {
			Entry<String, Count> en = ite.next();
			String word = en.getKey();
			Count count = en.getValue();
			if (count.count >= prune) {
				if (!newDictionary.containsKey(word)) {
					newDictionary.put(word, new Integer(index++));
				}
			}
		}
		dictionary = newDictionary;
		
		// Release memory
		wordMap = null;
	}

	public void buildDictionary(String text) {
		List<String> words = tokenizer.tokenize(text);
		
		for (String word : words) {
			if (this.s_lowerCaseTokens == true) {
				word = word.toLowerCase();
			}
			// TODO process word. word = stemmer.stem(word);
			if (this.s_useStoplist == true) {
				if (stopwords.contains(word)) {
					continue;
				}
			}
			if (!(wordMap.containsKey(word))) {
				wordMap.put(word, new Count(1));
			} else {
				wordMap.get(word).addOne();
			}
		}
	}
	
	public Map<Integer, Double> convert(String text){
		TreeMap<Integer, Double> vector = new TreeMap<Integer, Double>();
		List<String> words = tokenizer.tokenize(text);
		for (String word : words) {
			if (this.s_lowerCaseTokens == true) {
				word = word.toLowerCase();
			}
			// TODO process word. word = stemmer.stem(word);
			Integer index = dictionary.get(word);
			if (index != null) {
				if (s_OutputCounts) {
					Double count = vector.get(index);
					if (count != null) {
						vector.put(index, new Double(count.doubleValue() + 1.0));
					} else {
						vector.put(index, new Double(1));
					}
				} else {
					vector.put(index, new Double(1));
				}
			}
		}
		return vector;
	}
	
	public static void main(String[] args) throws IOException{
		HashMap<String, HashSet<String>> typeMap = TypeMapIO.read("result/types");
		StringToWordVectorConverter converter = new StringToWordVectorConverter(new SingleWordTokenizer(),1000000);
		for (Entry<String, HashSet<String>> en:typeMap.entrySet()) {
			System.out.println("Bulid: " + en.getKey());
			for (String url:en.getValue()) {
				converter.buildDictionary(url);
			}
		}
		converter.determineDictionary();
		
		System.out.println(converter.dictionary.size());
		
		for (String url:typeMap.get("AmericanFootballTeam")) {
			for (Entry<Integer, Double> en:converter.convert(url).entrySet()){
				System.out.print(en.getKey() + " ");
			}
			System.out.println();
		}
	}
}
