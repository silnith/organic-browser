package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-comma-token">&lt;
 *      comma-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class CommaToken extends LexicalToken {
    
    public CommaToken() {
        super(LexicalType.COMMA_TOKEN);
    }
    
    @Override
    public String toString() {
        return "','";
    }
    
}
