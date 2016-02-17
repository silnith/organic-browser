package org.silnith.browser.organic.property.accessor;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
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
    protected ListStylePosition parse(final StyleData styleData, final String specifiedValue) {
        return ListStylePosition.getFromValue(specifiedValue);
    }
    
    @Override
    protected ListStylePosition parse(StyleData styleData, List<Token> specifiedValue) {
        if (specifiedValue.size() != 1) {
            throw new IllegalArgumentException();
        }
        final Token token = specifiedValue.get(0);
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
        throw new IllegalArgumentException();
    }

    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
