package org.silnith.browser.organic.parser.html4.token;

public class EmptyEndTag extends Token {
    
    public EmptyEndTag() {
    }
    
    @Override
    public Type getType() {
        return Type.EMPTY_END_TAG;
    }
    
}
