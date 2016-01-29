package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-dimension-token">&lt;dimension-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class DimensionToken extends TypedNumericValueToken {

	private final StringBuilder unit;

	public DimensionToken() {
		super();
		this.unit = new StringBuilder();
	}

	public void setUnit(final String unit) {
		this.unit.setLength(0);
		this.unit.append(unit);
	}

	public String getUnit() {
		return unit.toString();
	}

	@Override
	public LexicalType getLexicalType() {
		return LexicalType.DIMENSION_TOKEN;
	}

	@Override
	public String toString() {
		return getStringValue() + "[" + getNumericType() + "]=" + getNumericValue() + getUnit();
	}

}
