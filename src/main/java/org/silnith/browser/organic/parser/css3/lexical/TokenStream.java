package org.silnith.browser.organic.parser.css3.lexical;

import java.io.IOException;

import org.silnith.browser.organic.parser.css3.Token;


public interface TokenStream {
    
    Token getNextToken() throws IOException;
    
}
