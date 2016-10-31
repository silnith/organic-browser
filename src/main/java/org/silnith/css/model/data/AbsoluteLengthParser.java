package org.silnith.css.model.data;

import java.util.List;

import org.silnith.parser.css3.Token;
import org.silnith.parser.css3.lexical.token.DimensionToken;
import org.silnith.parser.css3.lexical.token.LexicalToken;

/**
 * A parser for CSS absolute length values.  A length value is simply a CSS number with a unit suffix.
 * <p>
 * An absolute length value is a length value where the unit is an {@link AbsoluteUnit}.
 * 
 * @author kent
 */
public class AbsoluteLengthParser implements PropertyValueParser<AbsoluteLength> {
    
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

    @Override
    public AbsoluteLength parse(final List<Token> specifiedValue) {
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
                for (final AbsoluteUnit unit : AbsoluteUnit.values()) {
                    if (unit.getSuffix().equals(dimensionToken.getUnit())) {
                        final Number numericValue = dimensionToken.getNumericValue();
                        return new AbsoluteLength(numericValue.floatValue(), unit);
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
