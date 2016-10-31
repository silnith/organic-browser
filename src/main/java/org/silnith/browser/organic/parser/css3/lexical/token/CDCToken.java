package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-cdc-token">&lt;CDC-
 *      token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class CDCToken extends LexicalToken {
    
    public CDCToken() {
        super(LexicalType.CDC_TOKEN);
    }
    
    @Override
    public String toString() {
        return "<CDC-token>";
    }
    
}
