package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.PropertyName;
import org.silnith.parser.css3.Token;
import org.silnith.parser.css3.grammar.Parser;
import org.silnith.parser.css3.lexical.TokenListStream;
import org.silnith.parser.css3.lexical.token.LexicalToken;
import org.silnith.parser.css3.lexical.token.NumberToken;


public class WidowsAccessor extends PropertyAccessor<Integer> {
    
    public WidowsAccessor() {
        super(PropertyName.WIDOWS, true);
    }

    @Override
    public Integer getInitialValue(StyleData styleData) {
        return 2;
    }
    
    @Override
    protected Integer parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        final Parser cssParser = new Parser(new TokenListStream(specifiedValue));
        cssParser.prime();
        final Token componentValue = cssParser.parseComponentValue();
        switch (componentValue.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) componentValue;
            switch (lexicalToken.getLexicalType()) {
            case NUMBER_TOKEN: {
                final NumberToken numberToken = (NumberToken) lexicalToken;
                switch (numberToken.getNumericType()) {
                case INTEGER: {
                    return numberToken.getNumericValue().intValue();
                } // break;
                case NUMBER: {
                    throw new IllegalArgumentException("Widows must be an integer.");
                } // break;
                default: break;
                }
            } break;
            default: break;
            }
        } break;
        default: break;
        }
        throw new IllegalArgumentException();
    }
    
    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
