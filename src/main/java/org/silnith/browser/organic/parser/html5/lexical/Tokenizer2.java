package org.silnith.browser.organic.parser.html5.lexical;

import java.io.IOException;
import java.io.Reader;

import javax.annotation.PostConstruct;

public class Tokenizer2 {
    
    private static final int EOF = -1;
    
    /**
     * @see <a href="https://www.w3.org/TR/html5/syntax.html#input-stream">input stream</a>
     */
    private final Reader inputStream;
    
    private int currentInputCharacter;
    
    /**
     * @see <a href="https://www.w3.org/TR/html5/syntax.html#next-input-character">next input character</a>
     */
    private int nextInputCharacter;
    
    private final int[] lookahead;
    
    public Tokenizer2(final Reader inputStream) {
        super();
        this.inputStream = inputStream;
        this.lookahead = new int[1];
    }
    
    @PostConstruct
    public void prime() throws IOException {
        nextInputCharacter = inputStream.read();
    }

}
