package org.silnith.browser.organic.parser.css3.lexical;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.browser.organic.parser.css3.lexical.token.EOFToken;


public class TokenListStream implements TokenStream {
    
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
