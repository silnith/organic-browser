package org.silnith.css.model.data;

import java.util.List;

import org.silnith.browser.organic.parser.css3.Token;

/**
 * A parser for CSS percentage length values.  A length value is simply a CSS number with a unit suffix.
 * <p>
 * A percentage length value is a length value where the unit is a {@link PercentageUnit}.
 * 
 * @author kent
 */
public class PercentageLengthParser implements PropertyValueParser<PercentageLength> {
    
    private final CSSNumberParser cssNumberParser;
    
    public PercentageLengthParser(final CSSNumberParser cssNumberParser) {
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
    public PercentageLength parse(final String specifiedValue) {
        for (final PercentageUnit unit : PercentageUnit.values()) {
            if (specifiedValue.endsWith(unit.getSuffix())) {
                // this is the unit, parse it
                final int lastIndexOf = specifiedValue.lastIndexOf(unit.getSuffix());
                final String substring = specifiedValue.substring(0, lastIndexOf);
                final CSSNumber cssNumber = cssNumberParser.parse(substring);
                return new PercentageLength(cssNumber);
            }
        }
        return null;
    }

    @Override
    public PercentageLength parse(List<Token> specifiedValue) {
        throw new UnsupportedOperationException();
    }
    
}
