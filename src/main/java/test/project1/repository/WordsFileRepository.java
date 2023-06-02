package test.project1.repository;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.file.AsyncFile;
import io.vertx.core.file.FileSystem;
import io.vertx.core.file.OpenOptions;
import io.vertx.core.parsetools.RecordParser;

public class WordsFileRepository implements WordsRepository {

	private final FileSystem fs;
	
	public WordsFileRepository() {
		fs = Vertx.vertx().fileSystem(); 
	}
	
	public Future<Void> save(String word) {
		OpenOptions opts = new OpenOptions();
		opts.setAppend(true);
		
		return 
			fs.open("words.txt", opts)
				.compose(handler -> {
					Promise<AsyncFile> p = Promise.promise();
					
					handler.write(Buffer.buffer(word+"\n"))
						.onSuccess(handler1 -> p.complete());
					return p.future();
					
				}).compose(handler -> {
					return handler.close();
				});
		
	}
	
	public Future<List<String>> readWords(){
		OpenOptions opts = new OpenOptions();
		opts.setRead(true);
		opts.setWrite(false);
		
		List<String> words = new ArrayList<>();
		Promise<List<String>> promise = Promise.promise();
		
		fs.exists("words.txt").onComplete(result -> {
			if (result.result()) {
				fs.open("words.txt", opts)
				.andThen(file -> {
					file.result().handler(
						RecordParser.newDelimited("\n", line -> {
							System.out.println("reading line="+line);
							words.add(line.toString());
						})
					);
					
					file.result().endHandler(end -> {
						file.result().close();
						promise.complete(words);
					});
				});
			}else {
				promise.complete(words);
			}			
		});

		return promise.future();
	}    
}
