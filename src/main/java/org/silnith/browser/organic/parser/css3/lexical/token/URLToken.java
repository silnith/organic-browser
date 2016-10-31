package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-url-token">&lt;url-
 *      token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class URLToken extends StringValueToken {
    
    public URLToken() {
        super(LexicalType.URL_TOKEN);
    }
    
    @Override
    public String toString() {
        return "url(" + getStringValue() + ")";
    }
    
}
