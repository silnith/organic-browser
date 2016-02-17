package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-bad-url-token">&lt;
 *      bad-url-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class BadURLToken extends LexicalToken {
    
    public BadURLToken() {
        super(LexicalType.BAD_URL_TOKEN);
    }
    
    @Override
    public String toString() {
        return "<bad-url-token>";
    }
    
}
