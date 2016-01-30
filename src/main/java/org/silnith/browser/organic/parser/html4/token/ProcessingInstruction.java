package org.silnith.browser.organic.parser.html4.token;

public class ProcessingInstruction extends Token {
    
    private final String content;
    
    public ProcessingInstruction(final String content) {
        super();
        this.content = content;
    }
    
    @Override
    public Type getType() {
        return Type.PROCESSING_INSTRUCTION;
    }
    
    public String getContent() {
        return content;
    }
    
}
