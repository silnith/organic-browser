package org.silnith.browser.organic.parser.css3.lexical;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.browser.organic.parser.css3.lexical.token.EOFToken;
import org.silnith.browser.organic.parser.css3.lexical.token.LexicalToken;


public class TokenListStream implements TokenStream {
    
    public static TokenListStream fromTokenStream(final TokenStream tokenStream) throws IOException {
        final List<Token> tokens = new ArrayList<>();
        Token token = tokenStream.getNextToken();
        while (!isEOFToken(token)) {
            tokens.add(token);
            token = tokenStream.getNextToken();
        }
        return new TokenListStream(tokens);
    }
    
    private static boolean isLexicalToken(final Token token, final LexicalToken.LexicalType type) {
        if (token.getType() == Token.Type.LEXICAL_TOKEN) {
            final LexicalToken lexicalToken = (LexicalToken) token;
            return lexicalToken.getLexicalType() == type;
        } else {
            return false;
        }
    }
    
    private static boolean isEOFToken(final Token token) {
        return token.getType() == Token.Type.EOF_TOKEN
                || isLexicalToken(token, LexicalToken.LexicalType.EOF);
    }
    
    private final Iterator<Token> iterator;
    
    public TokenListStream(final List<Token> tokens) {
        super();
        this.iterator = tokens.iterator();
    }
    
    @Override
    public Token getNextToken() throws IOException {
        if (iterator.hasNext()) {
            return iterator.next();
        } else {
            return new EOFToken();
        }
    }
    
}
