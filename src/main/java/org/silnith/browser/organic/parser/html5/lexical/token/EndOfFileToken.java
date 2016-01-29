package org.silnith.browser.organic.parser.html5.lexical.token;

public class EndOfFileToken extends Token {

	public EndOfFileToken() {
		super();
	}

	@Override
	public Type getType() {
		return Type.EOF;
	}

	@Override
	public String toString() {
		return "<EOF>";
	}

}
