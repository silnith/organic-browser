package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-number-token">&lt;
 *      number-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class NumberToken extends TypedNumericValueToken {
    
    public NumberToken() {
        super();
    }
    
    @Override
    public LexicalType getLexicalType() {
        return LexicalType.NUMBER_TOKEN;
    }
    
    @Override
    public String toString() {
        return getStringValue() + "[" + getNumericType() + "]=" + getNumericValue();
    }
    
}
