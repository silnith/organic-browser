package org.silnith.browser.organic.parser.html4.token;

public class DataBlock extends Token {
    
    private final String name;
    
    private final String content;
    
    public DataBlock(final String name, final String content) {
        super();
        this.name = name;
        this.content = content;
    }
    
    @Override
    public Type getType() {
        return Type.DATA_BLOCK;
    }
    
    public String getName() {
        return name;
    }
    
    public String getContent() {
        return content;
    }
    
}
