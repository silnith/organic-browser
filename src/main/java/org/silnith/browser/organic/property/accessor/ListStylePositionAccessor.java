package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.browser.organic.parser.css3.grammar.Parser;
import org.silnith.browser.organic.parser.css3.lexical.TokenListStream;
import org.silnith.browser.organic.parser.css3.lexical.token.IdentToken;
import org.silnith.browser.organic.parser.css3.lexical.token.LexicalToken;
import org.silnith.css.model.data.ListStylePosition;
import org.silnith.css.model.data.PropertyName;


public class ListStylePositionAccessor extends PropertyAccessor<ListStylePosition> {
    
    public ListStylePositionAccessor() {
        super(PropertyName.LIST_STYLE_POSITION, true);
    }
    
    @Override
    public ListStylePosition getInitialValue(final StyleData styleData) {
        return ListStylePosition.OUTSIDE;
    }
    
    @Override
    protected ListStylePosition parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        final Parser cssParser = new Parser(new TokenListStream(specifiedValue));
        cssParser.prime();
        final Token token = cssParser.parseComponentValue();
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                final ListStylePosition listStylePosition = ListStylePosition.getFromValue(identToken.getStringValue());
                if (listStylePosition != null) {
                    return listStylePosition;
                }
            } // break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        throw new IllegalArgumentException("Unknown list style position: " + specifiedValue);
    }

    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
