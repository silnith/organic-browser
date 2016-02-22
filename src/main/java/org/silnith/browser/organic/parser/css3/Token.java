package org.silnith.browser.organic.parser.css3;

public abstract class Token {
    
    public enum Type {
        /**
         * @see org.silnith.browser.organic.parser.css3.grammar.token.ComponentValue
         */
        COMPONENT_VALUE,
        /**
         * @see org.silnith.browser.organic.parser.css3.lexical.token.LexicalToken
         */
        LEXICAL_TOKEN,
        /**
         * Not currently used.
         */
        @Deprecated
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
