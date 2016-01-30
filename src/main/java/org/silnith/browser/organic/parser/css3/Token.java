package org.silnith.browser.organic.parser.css3;

public abstract class Token {
    
    public enum Type {
        COMPONENT_VALUE,
        LEXICAL_TOKEN,
        EOF_TOKEN
    }
    
    private final Type type;
    
    protected Token(final Type type) {
        super();
        this.type = type;
    }
    
    public final Type getType() {
        return type;
    }
    
}
