package test.project1;

public class TextAnalyzerResult {

	private final String value;
	private final String lexical;
	
	public TextAnalyzerResult(String value, String lexical) {
		super();
		this.value = value;
		this.lexical = lexical;
	}

	public String getValue() {
		return value;
	}

	public String getLexical() {
		return lexical;
	}
	
}
