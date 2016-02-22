package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-percentage-token">
 *      &lt;percentage-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class PercentageToken extends NumericValueToken {
    
    public PercentageToken() {
        super(LexicalType.PERCENTAGE_TOKEN);
    }
    
    public PercentageToken(final NumericValueToken numberToken) {
        super(LexicalType.PERCENTAGE_TOKEN);
        this.setStringValue(numberToken.getStringValue());
        this.setNumericValue(numberToken.getNumericValue());
    }
    
    @Override
    public String toString() {
        return getNumericValue() + "%";
    }
    
}
