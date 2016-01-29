package org.silnith.browser.organic.parser.html4.token;

public class EndTag extends Token {

	private final String name;

	public EndTag(final String name) {
		super();
		this.name = name;
	}

	@Override
	public Type getType() {
		return Type.END_TAG;
	}

	public String getName() {
		return name;
	}

}
