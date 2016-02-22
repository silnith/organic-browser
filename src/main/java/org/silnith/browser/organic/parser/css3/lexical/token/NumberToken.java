package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-number-token">&lt;
 *      number-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class NumberToken extends TypedNumericValueToken {
    
    public NumberToken() {
        super(LexicalType.NUMBER_TOKEN);
    }
    
    @Override
    public String toString() {
        switch (getNumericType()) {
        case INTEGER: return String.valueOf(getNumericValue().longValue());
        case NUMBER: return String.valueOf(getNumericValue().doubleValue());
        default: return getStringValue() + "[" + getNumericType() + "]=" + getNumericValue();
        }
    }
    
}
