package org.silnith.browser.organic.parser.css3.lexical.token;

/**
 * @see <a href="http://dev.w3.org/csswg/css-syntax/#typedef-dimension-token">
 *      &lt;dimension-token&gt;</a>
 * @author <a href="mailto:silnith@gmail.com">Kent Rosenkoetter</a>
 */
public class DimensionToken extends TypedNumericValueToken {
    
    private final StringBuilder unit;
    
    public DimensionToken() {
        super(LexicalType.DIMENSION_TOKEN);
        this.unit = new StringBuilder();
    }
    
    public DimensionToken(final TypedNumericValueToken numberToken) {
        super(LexicalType.DIMENSION_TOKEN);
        this.unit = new StringBuilder();
        this.setStringValue(numberToken.getStringValue());
        this.setNumericValue(numberToken.getNumericValue());
        this.setNumericType(numberToken.getNumericType());
    }
    
    public void setUnit(final String unit) {
        this.unit.setLength(0);
        this.unit.append(unit);
    }
    
    public String getUnit() {
        return unit.toString();
    }
    
    @Override
    public String toString() {
        switch (getNumericType()) {
        case INTEGER: return String.valueOf(getNumericValue().longValue()) + getUnit();
        case NUMBER: return String.valueOf(getNumericValue().doubleValue()) + getUnit();
        default: return getStringValue() + "[" + getNumericType() + "]=" + getNumericValue() + getUnit();
        }
    }
    
}
