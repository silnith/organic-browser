package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-colon-token">&lt;
 *      colon-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class ColonToken extends LexicalToken {
    
    public ColonToken() {
        super(LexicalType.COLON_TOKEN);
    }
    
    @Override
    public String toString() {
        return "':'";
    }
    
}
