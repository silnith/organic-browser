package org.silnith.browser.organic.parser;

import java.io.IOException;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.Locale;
import java.util.StringTokenizer;

import org.silnith.browser.organic.network.Download;
import org.w3c.dom.Document;

public class HTMLParser implements FileParser<Document> {

	public HTMLParser() {
	}

	@Override
	public Document parse(final Download download) throws IOException {
		Document document = null;
		final String content = download.getContent();
		int index = 0;
		char character = content.charAt(index);
		
		if (character == '\ufeff') {
			// byte order marker
			index++;
			character = content.charAt(index);
		}
		
		// skip over as many comments and whitespace characters as exist
		
		// parse DOCTYPE
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 9; i++) {
			builder.append(character);
			index++;
			character = content.charAt(index);
		}
		consume(builder.toString().toLowerCase(Locale.ENGLISH), "<!doctype");
		
		StringTokenizer tokenizer = new StringTokenizer(content);
		tokenizer.nextToken("<");
		
		CharacterIterator iter = new StringCharacterIterator(content);
		character = iter.next();
		
		return document;
	}

	private void consume(char expected, char actual) {
		if (expected != actual) {
			throw new IllegalArgumentException("Parse error, expected: '" + expected + "', got: '" + actual + "'.");
		}
	}

	private void consume(final String expected, final String actual) {
		if ( !expected.equals(actual)) {
			throw new IllegalArgumentException("Parse error, expected: \"" + expected + "\", got: \"" + actual + "\".");
		}
	}

	@Override
	public void dispose() {
	}

}
