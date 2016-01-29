package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-percentage-token">&lt;percentage-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class PercentageToken extends NumericValueToken {

	public PercentageToken() {
		super();
	}

	@Override
	public LexicalType getLexicalType() {
		return LexicalType.PERCENTAGE_TOKEN;
	}

	@Override
	public String toString() {
		return getStringValue() + "=" + getNumericValue() + "%";
	}

}
