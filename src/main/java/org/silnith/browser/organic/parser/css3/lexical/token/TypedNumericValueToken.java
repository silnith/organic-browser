package org.silnith.browser.organic.parser.css3.lexical.token;

public abstract class TypedNumericValueToken extends NumericValueToken {
    
    public enum NumericType {
        INTEGER,
        NUMBER
    }
    
    private NumericType numericType;
    
    public TypedNumericValueToken(final LexicalType lexicalType) {
        super(lexicalType);
        this.numericType = NumericType.INTEGER;
    }
    
    public void setNumericType(final NumericType numericType) {
        this.numericType = numericType;
    }
    
    public NumericType getNumericType() {
        return numericType;
    }
    
}
