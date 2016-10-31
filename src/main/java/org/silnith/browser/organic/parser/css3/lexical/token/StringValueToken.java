package org.silnith.browser.organic.parser.css3.lexical.token;

public abstract class StringValueToken extends LexicalToken {
    
    private final StringBuilder value;
    
    public StringValueToken(final LexicalType lexicalType) {
        super(lexicalType);
        this.value = new StringBuilder();
    }
    
    public void append(final char character) {
        value.append(character);
    }
    
    public void append(final char[] character) {
        value.append(character);
    }
    
    public void setStringValue(final String str) {
        value.setLength(0);
        value.append(str);
    }
    
    public String getStringValue() {
        return value.toString();
    }
    
}
