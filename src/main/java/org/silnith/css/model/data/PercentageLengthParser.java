package org.silnith.css.model.data;

import java.util.List;

import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.browser.organic.parser.css3.lexical.token.DimensionToken;
import org.silnith.browser.organic.parser.css3.lexical.token.LexicalToken;

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
        if (specifiedValue.size() != 1) {
            throw new IllegalArgumentException();
        }
        final Token token = specifiedValue.get(0);
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case DIMENSION_TOKEN: {
                final DimensionToken dimensionToken = (DimensionToken) lexicalToken;
                for (final PercentageUnit unit : PercentageUnit.values()) {
                    if (unit.getSuffix().equals(dimensionToken.getUnit())) {
                        final Number numericValue = dimensionToken.getNumericValue();
                        return new PercentageLength(numericValue.floatValue());
                    }
                }
            } break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        throw new IllegalArgumentException();
    }
    
}
