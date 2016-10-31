package org.silnith.browser.organic.parser.html5.lexical.token;

public class StartTagToken extends TagToken {
    
    public StartTagToken() {
        super();
    }
    
    @Override
    public Type getType() {
        return Token.Type.START_TAG;
    }
    
    @Override
    public String toString() {
        final StringBuilder value = new StringBuilder();
        value.append("StartTagToken ");
        value.append('<');
        value.append(getTagName());
        for (final Attribute attribute : getAttributes()) {
            value.append(' ');
            value.append(attribute.toString());
        }
        if (isSelfClosing()) {
            value.append('/');
        }
        value.append('>');
        return value.toString();
    }
    
}
