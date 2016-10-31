package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.Length;
import org.silnith.css.model.data.LengthParser;
import org.silnith.css.model.data.PercentageLength;
import org.silnith.css.model.data.PropertyName;
import org.silnith.css.model.data.RelativeLength;
import org.silnith.parser.css3.Token;
import org.silnith.parser.css3.grammar.Parser;
import org.silnith.parser.css3.lexical.TokenListStream;
import org.silnith.parser.css3.lexical.token.IdentToken;
import org.silnith.parser.css3.lexical.token.LexicalToken;


public abstract class MarginAccessor extends PropertyAccessor<Length<?>> {
    
    private final LengthParser<?> lengthParser;
    
    private final PropertyAccessor<AbsoluteLength> fontSizeAccessor;
    
    public MarginAccessor(final PropertyName propertyName, final LengthParser<?> lengthParser,
            final PropertyAccessor<AbsoluteLength> fontSizeAccessor) {
        super(propertyName, false);
        this.lengthParser = lengthParser;
        this.fontSizeAccessor = fontSizeAccessor;
    }
    
    @Override
    public Length<?> getInitialValue(final StyleData styleData) {
        return AbsoluteLength.ZERO;
    }
    
    @Override
    protected Length<?> parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        final Parser cssParser = new Parser(new TokenListStream(specifiedValue));
        cssParser.prime();
        final Token token = cssParser.parseComponentValue();
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                if (identToken.getStringValue().equals("auto")) {
                    throw new UnsupportedOperationException("Margin value 'auto' has not been implemented yet.");
                }
            } break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        
        final Length<?> length = lengthParser.parse(specifiedValue);
        
        final AbsoluteLength absoluteLength;
        switch (length.getType()) {
        case ABSOLUTE: {
            absoluteLength = (AbsoluteLength) length;
        } break;
        case RELATIVE: {
            final RelativeLength relativeLength = (RelativeLength) length;
            absoluteLength = relativeLength.resolve(fontSizeAccessor.getComputedValue(styleData));
        } break;
        case PERCENTAGE: {
            final PercentageLength percentageLength = (PercentageLength) length;
            return percentageLength;
        } // break;
        default: throw new IllegalArgumentException("Unknown length type: " + length.getType());
        }
        
        return absoluteLength;
    }

    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.singleton(PropertyName.FONT_SIZE);
    }
    
}
