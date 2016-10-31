package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-bad-string-token">
 *      &lt;bad-string-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class BadStringToken extends LexicalToken {
    
    public BadStringToken() {
        super(LexicalType.BAD_STRING_TOKEN);
    }
    
    @Override
    public String toString() {
        return "<bad-string>";
    }
    
}
