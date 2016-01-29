package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-delim-token">&lt;delim-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class DelimToken extends LexicalToken {

	private final char[] chars;

	public DelimToken(final char ch) {
		super();
		this.chars = new char[] {ch};
	}

	public DelimToken(final char[] characters) {
		super();
		this.chars = characters;
	}

	@Override
	public LexicalType getLexicalType() {
		return LexicalType.DELIM_TOKEN;
	}

	@Override
	public String toString() {
		return "'" + String.valueOf(chars) + "'";
	}

}
