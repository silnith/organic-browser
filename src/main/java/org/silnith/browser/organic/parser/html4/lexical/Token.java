package org.silnith.browser.organic.parser.html4.lexical;

/**
 * Tokens produced while parsing HTML.
 * 
 * @author kent
 */
public class Token {
    
    public enum Type {
        NAME("[a-zA-Z][a-zA-Z0-9_.-:]*"),
        WORD("[0-9_.-:][a-zA-Z0-9_.-:]*"),
        TAG_OPEN("<"),
        MARKUP_DECLARATION("<!"),
        PROCESSING_INSTRUCTION("<?"),
        ENDING_TAG("</"),
        TAG_CLOSE(">"),
        COMMENT_DELIMITER("--"),
        SLASH("/"),
        LEFT_BRACKET("["),
        RIGHT_BRACKET("]"),
        END_BLOCK("]]>"),
        SINGLE_QUOTE("'"),
        DOUBLE_QUOTE("\""),
        AMPERSAND("&"),
        SEMICOLON(";"),
        NEWLINE("\n"),
        WHITESPACE("[\r \t]+"),
        TEXT(".+"),
        EOF("(eof)");
        
        private final String represents;
        
        private Type(final String represents) {
            this.represents = represents;
        }
        
        @Override
        public String toString() {
            return represents;
        }
        
    }
    
    private final Type type;
    
    private final String content;
    
    public Token(final Type type, final String content) {
        super();
        this.type = type;
        this.content = content;
    }
    
    public Type getType() {
        return type;
    }
    
    public String getContent() {
        return content;
    }
    
}
