package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-semicolon-token">
 *      &lt;semicolon-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class SemicolonToken extends LexicalToken {
    
    public SemicolonToken() {
        super(LexicalType.SEMICOLON_TOKEN);
    }
    
    @Override
    public String toString() {
        return "';'";
    }
    
}
