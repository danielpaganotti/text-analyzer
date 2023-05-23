package test.project1;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import test.project1.repository.WordsFileRepository;

public class Server extends AbstractVerticle {

	private Router router;
	private HttpServer server;
	
	private TextAnalyzer analyzer = new TextAnalyzer(
			new DefaultCharacterValueCalculatorImpl() ,new WordsFileRepository());
	
	@Override
	public void start(Promise<Void> start) throws Exception {
		router = Router.router(vertx);
		
		router.route("/").handler(context -> {
			HttpServerResponse response = context.response();
			response.putHeader("content-type", "text/html")
			.end("<h1>Hello world</h1>");

		});
		
		router.route("/analyze").method(HttpMethod.POST)
		.handler(BodyHandler.create())
		.handler(context -> {
			JsonObject json = context.body().asJsonObject();
			
			TextAnalyzerResult result = analyzer.analyze(json.getString("text"));
			JsonObject jsonResult = new JsonObject();
			jsonResult.put("value", result.getValue());
			jsonResult.put("lexical", result.getLexical());
			
			HttpServerResponse response = context.response();
			response.putHeader("content-type", "text/json")
			.end(jsonResult.toBuffer());
		});
		
		vertx.createHttpServer().requestHandler(router)
			.listen(config().getInteger("http.port", 8080))
			.onSuccess(server -> {
				this.server = server;
				start.complete();
			})
			.onFailure(start::fail);
	}
}
