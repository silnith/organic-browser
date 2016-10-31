package org.silnith.css.model.data;

import java.io.IOException;
import java.util.List;
import java.util.ListIterator;

import org.silnith.parser.css3.Token;
import org.silnith.parser.css3.lexical.token.IdentToken;
import org.silnith.parser.css3.lexical.token.LexicalToken;
import org.silnith.parser.css3.lexical.token.StringToken;


public class FontFamilyNameParser implements PropertyValueParser<String> {
    
    @Override
    public String parse(String specifiedValue) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    public String parse(final List<Token> specifiedValue) throws IOException {
        final ListIterator<Token> listIterator = specifiedValue.listIterator();
        while (listIterator.hasNext()) {
            final Token token = listIterator.next();
            if (!isLexicalToken(token, LexicalToken.LexicalType.WHITESPACE_TOKEN)) {
                listIterator.previous();
                break;
            }
        }
        if (!listIterator.hasNext()) {
            throw new IllegalArgumentException();
        }
        final Token firstRealToken = listIterator.next();
        switch (firstRealToken.getType()) {
        case LEXICAL_TOKEN: {
            final LexicalToken lexicalToken = (LexicalToken) firstRealToken;
            switch (lexicalToken.getLexicalType()) {
            case STRING_TOKEN: {
                final StringToken stringToken = (StringToken) lexicalToken;
                final String stringValue = stringToken.getStringValue();
                while (listIterator.hasNext()) {
                    final Token trailingToken = listIterator.next();
                    if (!isLexicalToken(trailingToken, LexicalToken.LexicalType.WHITESPACE_TOKEN)) {
                        throw new IllegalArgumentException();
                    }
                }
                return stringValue;
            } // break;
            case IDENT_TOKEN: {
                final IdentToken identToken = (IdentToken) lexicalToken;
                final StringBuilder builder = new StringBuilder();
                builder.append(identToken.getStringValue());
                boolean foundWhitespace = false;
                while (listIterator.hasNext()) {
                    final Token trailingToken = listIterator.next();
                    if (isLexicalToken(trailingToken, LexicalToken.LexicalType.WHITESPACE_TOKEN)) {
                        foundWhitespace = true;
                    } else if (isLexicalToken(trailingToken, LexicalToken.LexicalType.IDENT_TOKEN)) {
                        if (foundWhitespace) {
                            builder.append(' ');
                            foundWhitespace = false;
                        }
                        final IdentToken nextIdent = (IdentToken) trailingToken;
                        builder.append(nextIdent.getStringValue());
                    } else {
                        throw new IllegalArgumentException();
                    }
                }
                return builder.toString();
            } // break;
            default: throw new IllegalArgumentException();
            }
        } // break;
        default: throw new IllegalArgumentException();
        }
    }
    
    protected boolean isLexicalToken(final Token token, final LexicalToken.LexicalType type) {
        if (token.getType() == Token.Type.LEXICAL_TOKEN) {
            final LexicalToken lexicalToken = (LexicalToken) token;
            return lexicalToken.getLexicalType() == type;
        } else {
            return false;
        }
    }
    
}
