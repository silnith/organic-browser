package org.silnith.browser.organic.parser.html4.token;

public abstract class Token {
    
    public enum Type {
        DOCTYPE,
        START_TAG,
        EMPTY_START_TAG,
        END_TAG,
        EMPTY_END_TAG,
        COMMENT,
        PROCESSING_INSTRUCTION,
        ENTITY_REFERENCE,
        DATA_BLOCK
    }
    
    public Token() {
    }
    
    public abstract Type getType();
    
}
