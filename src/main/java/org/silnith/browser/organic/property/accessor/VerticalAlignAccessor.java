package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.browser.organic.parser.css3.grammar.Parser;
import org.silnith.browser.organic.parser.css3.lexical.TokenListStream;
import org.silnith.browser.organic.parser.css3.lexical.token.DimensionToken;
import org.silnith.browser.organic.parser.css3.lexical.token.IdentToken;
import org.silnith.browser.organic.parser.css3.lexical.token.LexicalToken;
import org.silnith.browser.organic.parser.css3.lexical.token.NumberToken;
import org.silnith.browser.organic.parser.css3.lexical.token.TypedNumericValueToken;
import org.silnith.css.model.data.AbsoluteLength;
import org.silnith.css.model.data.AlignAndOffset;
import org.silnith.css.model.data.LengthParser;
import org.silnith.css.model.data.PropertyName;
import org.silnith.css.model.data.VerticalAlignment;


public class VerticalAlignAccessor extends PropertyAccessor<AlignAndOffset> {
    
    private final LengthParser<?> lengthParser;
    
    public VerticalAlignAccessor(final LengthParser<?> lengthParser) {
        super(PropertyName.VERTICAL_ALIGN, false);
        this.lengthParser = lengthParser;
    }

    @Override
    public AlignAndOffset getInitialValue(StyleData styleData) {
        return new AlignAndOffset(VerticalAlignment.BASELINE);
    }
    
    @Override
    protected AlignAndOffset parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        final Parser cssParser = new Parser(new TokenListStream(specifiedValue));
        cssParser.prime();
        final Token componentValue = cssParser.parseComponentValue();
        switch (componentValue.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) componentValue;
            switch (lexicalToken.getLexicalType()) {
            case DIMENSION_TOKEN: {
                final DimensionToken dimensionToken = (DimensionToken) lexicalToken;
                
                return new AlignAndOffset(lengthParser.parse(specifiedValue));
            } // break;
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                final String identifier = identToken.getStringValue();
                for (final VerticalAlignment alignment : VerticalAlignment.values()) {
                    if (alignment.is(identifier)) {
                        return new AlignAndOffset(alignment);
                    }
                }
            } break;
            case NUMBER_TOKEN: {
                final NumberToken numberToken = (NumberToken) lexicalToken;
                if (numberToken.getNumericType() == TypedNumericValueToken.NumericType.INTEGER
                        && numberToken.getNumericValue().intValue() == 0) {
                    return new AlignAndOffset(AbsoluteLength.ZERO);
                } else {
                    throw new IllegalArgumentException();
                }
            } // break;
            default: break;
            }
        } break;
        default: break;
        }
        throw new IllegalArgumentException();
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        final Set<PropertyName> dependencies = EnumSet.noneOf(PropertyName.class);
        dependencies.add(PropertyName.FONT_SIZE);
        dependencies.add(PropertyName.LINE_HEIGHT);
        return dependencies;
    }
    
}
