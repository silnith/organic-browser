package org.silnith.css.model.data;

import java.util.List;

import org.silnith.parser.css3.Token;
import org.silnith.parser.css3.lexical.token.DimensionToken;
import org.silnith.parser.css3.lexical.token.LexicalToken;

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
                for (final RelativeUnit unit : RelativeUnit.values()) {
                    if (unit.getSuffix().equals(dimensionToken.getUnit())) {
                        final Number numericValue = dimensionToken.getNumericValue();
                        return new RelativeLength(numericValue.floatValue(), unit);
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
