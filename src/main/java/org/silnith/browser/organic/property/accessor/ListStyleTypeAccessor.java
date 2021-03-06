package org.silnith.browser.organic.property.accessor;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.silnith.browser.organic.StyleData;
import org.silnith.css.model.data.ListStyleType;
import org.silnith.css.model.data.PropertyName;
import org.silnith.parser.css3.Token;
import org.silnith.parser.css3.grammar.Parser;
import org.silnith.parser.css3.lexical.TokenListStream;
import org.silnith.parser.css3.lexical.token.IdentToken;
import org.silnith.parser.css3.lexical.token.LexicalToken;


public class ListStyleTypeAccessor extends PropertyAccessor<ListStyleType> {
    
    public ListStyleTypeAccessor() {
        super(PropertyName.LIST_STYLE_TYPE, true);
    }
    
    @Override
    public ListStyleType getInitialValue(final StyleData styleData) {
        return ListStyleType.DISC;
    }
    
    @Override
    protected ListStyleType parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        final Parser cssParser = new Parser(new TokenListStream(specifiedValue));
        cssParser.prime();
        final Token token = cssParser.parseComponentValue();
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
        throw new IllegalArgumentException(
                "Illegal value for property: " + getPropertyName() + ": " + token);
    }

    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
