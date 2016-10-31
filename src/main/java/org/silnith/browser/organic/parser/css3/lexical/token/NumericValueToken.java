package org.silnith.browser.organic.parser.css3.lexical.token;

public abstract class NumericValueToken extends StringValueToken {
    
    private Number numericValue;
    
    public NumericValueToken(final LexicalType lexicalType) {
        super(lexicalType);
        this.numericValue = Integer.valueOf(0);
    }
    
    public void setNumericValue(final Number number) {
        this.numericValue = number;
    }
    
    public Number getNumericValue() {
        return numericValue;
    }
    
}
