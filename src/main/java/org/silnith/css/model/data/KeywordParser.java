package org.silnith.css.model.data;

import java.io.IOException;
import java.util.List;

import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.browser.organic.parser.css3.grammar.Parser;
import org.silnith.browser.organic.parser.css3.lexical.TokenListStream;
import org.silnith.browser.organic.parser.css3.lexical.token.IdentToken;
import org.silnith.browser.organic.parser.css3.lexical.token.LexicalToken;

public class KeywordParser<T extends Enum<?> & Keyword> {
    
    private final Class<T> targetType;
    
    public KeywordParser(final Class<T> targetType) {
        super();
        this.targetType = targetType;
    }
    
    public T parse(final String identifier) {
        for (final T t : targetType.getEnumConstants()) {
            if (t.is(identifier)) {
                return t;
            }
        }
        throw new IllegalArgumentException("Unknown keyword: " + identifier + " for enum " + targetType);
    }
    
    public T parse(final List<Token> specifiedValue) throws IOException {
        final Parser cssParser = new Parser(new TokenListStream(specifiedValue));
        cssParser.prime();
        final Token componentValue = cssParser.parseComponentValue();
        switch (componentValue.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) componentValue;
            switch (lexicalToken.getLexicalType()) {
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                final String identifier = identToken.getStringValue();
                return parse(identifier);
            } // break;
            default: break;
            }
        } break;
        default: break;
        }
        throw new IllegalArgumentException();
    }

}
