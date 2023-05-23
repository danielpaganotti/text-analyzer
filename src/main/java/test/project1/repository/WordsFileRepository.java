package test.project1.repository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WordsFileRepository implements WordsRepository {

	public void save(String word) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("words.txt", true))){
			writer.append(word);
			writer.append("\n");
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> readWords(){
		List<String> words = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader("words.txt"))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	words.add(line);
		    }
		} catch (FileNotFoundException e) {

		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return words;
	}

    
}
