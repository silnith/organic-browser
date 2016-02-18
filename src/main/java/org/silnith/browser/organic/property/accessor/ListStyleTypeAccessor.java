package org.silnith.browser.organic.property.accessor;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.browser.organic.parser.css3.lexical.token.IdentToken;
import org.silnith.browser.organic.parser.css3.lexical.token.LexicalToken;
import org.silnith.css.model.data.ListStyleType;
import org.silnith.css.model.data.PropertyName;


public class ListStyleTypeAccessor extends PropertyAccessor<ListStyleType> {
    
    public ListStyleTypeAccessor() {
        super(PropertyName.LIST_STYLE_TYPE, true);
    }
    
    @Override
    public ListStyleType getInitialValue(final StyleData styleData) {
        return ListStyleType.DISC;
    }
    
    @Override
    protected ListStyleType parse(final StyleData styleData, final String specifiedValue) {
        return ListStyleType.getFromValue(specifiedValue);
    }
    
    @Override
    protected ListStyleType parse(StyleData styleData, List<Token> specifiedValue) {
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
                final String ident = identToken.getStringValue();
                final ListStyleType listStyleType = ListStyleType.getFromValue(ident);
                if (listStyleType != null) {
                    return listStyleType;
                }
            } break;
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
