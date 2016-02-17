package org.silnith.css.model.data;

import java.util.List;

import org.silnith.browser.organic.parser.css3.Token;

/**
 * A parser for CSS relative length values.  A length value is simply a CSS number with a unit suffix.
 * <p>
 * A relative length value is a length value where the unit is a {@link RelativeUnit}.
 * 
 * @author kent
 */
public class RelativeLengthParser implements PropertyValueParser<RelativeLength> {
    
    private final CSSNumberParser cssNumberParser;
    
    public RelativeLengthParser(final CSSNumberParser cssNumberParser) {
        super();
        this.cssNumberParser = cssNumberParser;
    }
    
    /**
     * Attempts to parse the specified value as a relative length. Returns
     * {@code null} if the value is not a relative length.
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

    @Override
    public RelativeLength parse(final List<Token> specifiedValue) {
        throw new UnsupportedOperationException();
    }
    
}
