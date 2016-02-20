package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-ident-token">&lt;
 *      ident-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class IdentToken extends StringValueToken {
    
    public IdentToken() {
        super(LexicalType.IDENT_TOKEN);
    }
    
    @Override
    public String toString() {
        return getStringValue();
    }
    
}
