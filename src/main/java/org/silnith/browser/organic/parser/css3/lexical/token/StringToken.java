package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-string-token">&lt;
 *      string-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class StringToken extends StringValueToken {
    
    public StringToken() {
        super();
    }
    
    @Override
    public LexicalType getLexicalType() {
        return LexicalType.STRING_TOKEN;
    }
    
    @Override
    public String toString() {
        return "\"" + getStringValue() + "\"";
    }
    
}
