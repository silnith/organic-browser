package org.silnith.css.model.data;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.browser.organic.parser.css3.grammar.Parser;
import org.silnith.browser.organic.parser.css3.grammar.token.ComponentValue;
import org.silnith.browser.organic.parser.css3.lexical.TokenListStream;
import org.silnith.browser.organic.parser.css3.lexical.token.DimensionToken;
import org.silnith.browser.organic.parser.css3.lexical.token.IdentToken;
import org.silnith.browser.organic.parser.css3.lexical.token.LexicalToken;
import org.silnith.browser.organic.parser.css3.lexical.token.NumberToken;
import org.silnith.browser.organic.parser.css3.lexical.token.PercentageToken;

/**
 * A parser for CSS length values.  A CSS length value is simply a CSS number
 * with a unit suffix.
 * 
 * @author kent
 */
public class LengthParser<T extends Unit> implements PropertyValueParser<Length<T>> {
    
    private final AbsoluteLengthParser absoluteLengthParser;
    
    private final RelativeLengthParser relativeLengthParser;
    
    private final PercentageLengthParser percentageLengthParser;
    
    public LengthParser(final AbsoluteLengthParser absoluteLengthParser,
            final RelativeLengthParser relativeLengthParser, final PercentageLengthParser percentageLengthParser) {
        super();
        this.absoluteLengthParser = absoluteLengthParser;
        this.relativeLengthParser = relativeLengthParser;
        this.percentageLengthParser = percentageLengthParser;
    }
    
    public Length<T> parse(final String specifiedValue) {
        final AbsoluteLength absoluteLength = absoluteLengthParser.parse(specifiedValue);
        
        if (absoluteLength != null) {
            return (Length<T>) absoluteLength;
        } else {
            final RelativeLength relativeLength = relativeLengthParser.parse(specifiedValue);
            
            if (relativeLength != null) {
                return (Length<T>) relativeLength;
            } else {
                final PercentageLength percentageLength = percentageLengthParser.parse(specifiedValue);
                
                if (percentageLength != null) {
                    return (Length<T>) percentageLength;
                } else {
                    return null;
                }
            }
        }
    }

    @Override
    public Length<T> parse(List<Token> specifiedValue) throws IOException {
        final Parser cssParser = new Parser(new TokenListStream(specifiedValue));
        cssParser.prime();
        return parseOneToken(cssParser.parseComponentValue());
    }

    private Length<T> parseOneToken(final Token token) {
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case DIMENSION_TOKEN: {
                final DimensionToken dimensionToken = (DimensionToken) lexicalToken;
                for (final AbsoluteUnit unit : AbsoluteUnit.values()) {
                    if (unit.getSuffix().equals(dimensionToken.getUnit())) {
                        final Number numericValue = dimensionToken.getNumericValue();
                        return (Length<T>) new AbsoluteLength(numericValue.floatValue(), unit);
                    }
                }
                for (final RelativeUnit unit : RelativeUnit.values()) {
                    if (unit.getSuffix().equals(dimensionToken.getUnit())) {
                        final Number numericValue = dimensionToken.getNumericValue();
                        return (Length<T>) new RelativeLength(numericValue.floatValue(), unit);
                    }
                }
            } break;
            case PERCENTAGE_TOKEN: {
                final PercentageToken percentageToken = (PercentageToken) lexicalToken;
                final Number numericValue = percentageToken.getNumericValue();
                return (Length<T>) new PercentageLength(numericValue.floatValue());
            } // break;
            case NUMBER_TOKEN: {
                final NumberToken numberToken = (NumberToken) lexicalToken;
                if (numberToken.getNumericValue().floatValue() == 0f) {
                    return (Length<T>) new AbsoluteLength(0f, AbsoluteUnit.PT);
                }
            } break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        throw new IllegalArgumentException("Unrecognized length value: " + token);
    }
    
}
