package org.silnith.browser.organic.parser.html5.lexical.token;

public class CommentToken extends Token {

	private StringBuilder content;

	public CommentToken(final String content) {
		this.content = new StringBuilder(content);
	}

	public CommentToken() {
		this.content = new StringBuilder();
	}

	public void append(final char ch) {
		content.append(ch);
	}

	public void append(final char[] ch) {
		content.append(ch);
	}

	public String getContent() {
		return content.toString();
	}

	@Override
	public Type getType() {
		return Type.COMMENT;
	}

	@Override
	public String toString() {
		return "CommentToken <!--" + content + "-->";
	}

}
