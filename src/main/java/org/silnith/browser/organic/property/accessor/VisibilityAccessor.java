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
import org.silnith.css.model.data.Position;
import org.silnith.css.model.data.PropertyName;
import org.silnith.css.model.data.Visibility;


public class VisibilityAccessor extends PropertyAccessor<Visibility> {
    
    public VisibilityAccessor() {
        super(PropertyName.VISIBILITY, true);
    }
    
    @Override
    public Visibility getInitialValue(final StyleData styleData) {
        return Visibility.VISIBLE;
    }
    
    @Override
    protected Visibility parse(StyleData styleData, List<Token> specifiedValue) throws IOException {
        final Parser cssParser = new Parser(new TokenListStream(specifiedValue));
        cssParser.prime();
        final Token token = cssParser.parseComponentValue();
        switch (token.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) token;
            switch (lexicalToken.getLexicalType()) {
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                final Visibility visibility = Visibility.getFromValue(identToken.getStringValue());
                if (visibility != null) {
                    return visibility;
                }
            } break;
            default: {} break;
            }
        } break;
        default: {} break;
        }
        throw new IllegalArgumentException("Illegal property value: " + getPropertyName() + ": " + specifiedValue);
    }

    @Override
    public Set<PropertyName> getDependencies() {
        return Collections.emptySet();
    }
    
}
