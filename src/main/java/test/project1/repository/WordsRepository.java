package test.project1.repository;

import java.util.List;

public interface WordsRepository {

	public void save(String word);
	
	public List<String> readWords();
}