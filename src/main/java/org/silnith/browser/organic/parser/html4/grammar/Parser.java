package org.silnith.browser.organic.parser.html4.grammar;

import java.io.IOException;

import org.silnith.browser.organic.parser.html4.lexical.Token;
import org.silnith.browser.organic.parser.html4.lexical.Tokenizer;

public class Parser {

	private final Tokenizer tokenizer;

	public Parser(final Tokenizer tokenizer) {
		super();
		this.tokenizer = tokenizer;
	}

	public void parse() throws IOException {
		final Token token = tokenizer.getNextToken();
		
		switch (token.getType()) {
		case AMPERSAND: {} break;
		}
	}

}
