package org.silnith.browser.organic.parser.html4.token;

public class Comment extends Token {

	private final String content;

	public Comment(final String content) {
		super();
		this.content = content;
	}

	@Override
	public Type getType() {
		return Type.COMMENT;
	}

	public String getContent() {
		return content;
	}

}
