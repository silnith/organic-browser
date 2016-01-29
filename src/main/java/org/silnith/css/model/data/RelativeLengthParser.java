package org.silnith.css.model.data;

public class RelativeLengthParser {

	private final CSSNumberParser cssNumberParser;

	public RelativeLengthParser(final CSSNumberParser cssNumberParser) {
		super();
		this.cssNumberParser = cssNumberParser;
	}

	/**
	 * Attempts to parse the specified value as a relative length.
	 * Returns {@code null} if the value is not a relative length.
	 * 
	 * @param specifiedValue the specified value to parse
	 * @return a relative length, or {@code null}
	 * @see RelativeUnit
	 */
	public RelativeLength parse(final String specifiedValue) {
		for (final RelativeUnit unit : RelativeUnit.values()) {
			if (specifiedValue.endsWith(unit.getSuffix())) {
				// this is the unit, parse it
				final int lastIndexOf = specifiedValue.lastIndexOf(unit.getSuffix());
				final String substring = specifiedValue.substring(0, lastIndexOf);
				final CSSNumber cssNumber = cssNumberParser.parse(substring);
				return new RelativeLength(cssNumber, unit);
			}
		}
		return null;
	}

}
