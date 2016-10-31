package org.silnith.browser.organic.parser.css3.grammar.token;

import java.util.List;

import org.silnith.browser.organic.parser.css3.Token;
import org.silnith.browser.organic.parser.css3.lexical.token.LexicalToken;


/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#simple-block">simple
 *      block</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class SimpleBlock extends ComponentValue {
    
    private final LexicalToken token;
    
    private final List<Token> value;
    
    public SimpleBlock(final LexicalToken token, final List<Token> value) {
        super(ComponentValueType.SIMPLE_BLOCK);
        this.token = token;
        this.value = value;
    }
    
    public LexicalToken getToken() {
        return token;
    }

    public List<Token> getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "{"
                + "token=" + token
                + ", "
                + "value=" + value
                + "}";
    }
    
}
