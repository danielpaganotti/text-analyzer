package test.project1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import io.vertx.core.Future;
import test.project1.repository.WordsRepository;

public class TextAnalyzer {
	
	private final CharacterValueCalculator valueCalculator;
	private final WordsRepository repository;
	
	private Map<Integer, SortedSet<String>> words = new HashMap<>();
	private List<String> sortedWords = new ArrayList<>();
	
	private boolean initialized = true; 
		
	public TextAnalyzer(CharacterValueCalculator valueCalculator, WordsRepository repository) {
		this.valueCalculator = valueCalculator;
		this.repository = repository;
		
		init();
	}
	
	private void init() {
		repository.readWords().andThen(result -> {
			this.sortedWords = result.result();
			
			Collections.sort(this.sortedWords);	
			this.sortedWords.forEach(word -> {
				Integer wordValue = this.valueCalculator.calculate(word);
				if (!words.containsKey(wordValue)) {
					words.put(wordValue, new TreeSet<String>());
				}
				words.get(wordValue).add(word);
			});
			
			this.initialized =  true;
			System.out.println("Initialized With "+this.sortedWords.size()+ " words");
		});
	}
	
	public Future<TextAnalyzerResult> analyze(String text) {
		
		if (!initialized) {
			throw new RuntimeException("TextAnalyzer is not initialized");
		}
				
		return Future.future(handler -> {
			
			Integer wordValue = this.valueCalculator.calculate(text);
			if (!words.containsKey(wordValue)) {
				words.put(wordValue, new TreeSet<String>());
			}
			words.get(wordValue).add(text);
			
			String closestValuedWord = null;
			int closestValue = getClosestWordValue(wordValue);
			SortedSet<String> closestWords = words.get(closestValue);
			if (closestWords != null && !closestWords.isEmpty()) {
				closestValuedWord = closestWords.first();
			}
			
			if (!sortedWords.contains(text)) {
				sortedWords.add(text);
				Collections.sort(sortedWords);
				
				this.repository.save(text);
			}

			String closestLexicalWord = null;
			int index = sortedWords.indexOf(text);
			int listSize = sortedWords.size();
			if (listSize > 1) {
				if (index == 0 && listSize > 1) {
					closestLexicalWord = sortedWords.get(1);
				}else if (index == (listSize - 1)) {
					closestLexicalWord = sortedWords.get(listSize - 2);
				}else {
					String previousWord = sortedWords.get(index - 1); 
					String nextWord = sortedWords.get(index + 1);
					int previousWordCompare = Math.abs(text.compareTo(previousWord));
					int nextWordCompare = Math.abs(text.compareTo(nextWord));
					closestLexicalWord = previousWordCompare <= nextWordCompare ? previousWord : nextWord;
				}
			}
			handler.complete(new TextAnalyzerResult(closestValuedWord, closestLexicalWord));
			
		});
	}
	
	private Integer getClosestWordValue(int givenTextValue) {
		int closestValue = 0;
		int lastDiff = Integer.MAX_VALUE;
		for(Integer value: words.keySet()){
			
			int diff = Math.abs(givenTextValue - value);
		    if(givenTextValue != value && 
		    		(diff < lastDiff || (diff == lastDiff && value > closestValue))){
		    	closestValue = value;
		    	lastDiff = diff;
		    }
		}
		return closestValue;
	}
}
