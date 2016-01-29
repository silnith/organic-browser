package org.silnith.browser.organic.parser.html4.token;

public class EmptyStartTag extends Token {

	public EmptyStartTag() {
	}

	@Override
	public Type getType() {
		return Type.EMPTY_START_TAG;
	}

}
