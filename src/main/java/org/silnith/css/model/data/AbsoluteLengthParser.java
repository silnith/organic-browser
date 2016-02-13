package org.silnith.css.model.data;

/**
 * A parser for CSS absolute length values.  A length value is simply a CSS number with a unit suffix.
 * <p>
 * An absolute length value is a length value where the unit is an {@link AbsoluteUnit}.
 * 
 * @author kent
 */
public class AbsoluteLengthParser {
    
    private final CSSNumberParser cssNumberParser;
    
    public AbsoluteLengthParser(final CSSNumberParser cssNumberParser) {
        super();
        this.cssNumberParser = cssNumberParser;
    }
    
    /**
     * Attempts to parse the specified value as an absolute length. Returns
     * {@code null} if the value is not an absolute length.
     * 
     * @param specifiedValue the specified value to parse
     * @return an absolute length, or {@code null}
     * @see AbsoluteUnit
     */
    public AbsoluteLength parse(final String specifiedValue) {
        if ("0".equals(specifiedValue)) {
            return AbsoluteLength.ZERO;
        }
        for (final AbsoluteUnit unit : AbsoluteUnit.values()) {
            if (specifiedValue.endsWith(unit.getSuffix())) {
                // this is this unit, parse it
                final int lastIndexOf = specifiedValue.lastIndexOf(unit.getSuffix());
                final String substring = specifiedValue.substring(0, lastIndexOf);
                final CSSNumber cssNumber = cssNumberParser.parse(substring);
                return new AbsoluteLength(cssNumber, unit);
            }
        }
        return null;
    }
    
}
