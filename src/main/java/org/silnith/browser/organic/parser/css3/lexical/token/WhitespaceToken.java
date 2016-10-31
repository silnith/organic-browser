package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-whitespace-token">
 *      &lt;whitespace-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class WhitespaceToken extends LexicalToken {
    
    public WhitespaceToken() {
        super(LexicalType.WHITESPACE_TOKEN);
    }
    
    @Override
    public String toString() {
        return "' '";
    }
    
}
