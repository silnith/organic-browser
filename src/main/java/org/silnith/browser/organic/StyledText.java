package org.silnith.browser.organic;

import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class StyledText extends StyledContent {
    
    private final String text;
    private String spacedText;
    
    public StyledText(final StyledDOMElement parent, final String text) {
        super(parent);
        // styled text must have a parent, namely an element
        if (parent == null) {
            throw new NullPointerException();
        }
        if (text == null) {
            throw new NullPointerException();
        }
        this.text = text;
        this.spacedText = null;
    }
    
    @Override
    public Locale getLanguage() {
        return getParent().getLanguage();
    }

    public String getText() {
        return text;
    }
    
    public String getSpacedText() {
        return spacedText;
    }
    
    public void setSpacedText(final String spacedText) {
        this.spacedText = spacedText;
    }

    @Override
    public StyleData getStyleData() {
        return getParent().getStyleData();
    }
    
    @Override
    public Node createDOM(final Document document) {
        final Element node = document.createElement("StyledText");
        node.appendChild(document.createTextNode(text));
        return node;
    }
    
    @Override
    public String toString() {
        return "{text: " + text + "}";
    }
    
}
