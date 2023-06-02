package test.project1.repository;

import java.util.List;

import io.vertx.core.Future;

public interface WordsRepository {

	public Future<Void> save(String word);
	
	public Future<List<String>> readWords();
}