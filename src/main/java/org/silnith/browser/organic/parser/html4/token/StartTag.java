package org.silnith.browser.organic.parser.html4.token;

import java.util.Map;


public class StartTag extends Token {
    
    private final String name;
    
    private final boolean netEnabling;
    
    private final Map<String, String> attributes;
    
    public StartTag(final String name, final Map<String, String> attributes) {
        super();
        this.name = name;
        this.netEnabling = false;
        this.attributes = attributes;
    }
    
    public StartTag(final String name, final boolean netEnabling, final Map<String, String> attributes) {
        super();
        this.name = name;
        this.netEnabling = netEnabling;
        this.attributes = attributes;
    }
    
    @Override
    public Type getType() {
        return Type.START_TAG;
    }
    
    public String getName() {
        return name;
    }
    
    public boolean isNetEnabling() {
        return netEnabling;
    }
    
    public Map<String, String> getAttributes() {
        return attributes;
    }
    
}
