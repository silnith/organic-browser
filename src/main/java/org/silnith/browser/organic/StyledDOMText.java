package org.silnith.browser.organic;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;


public class StyledDOMText extends StyledContent {
    
    private final Text textNode;
    
    public StyledDOMText(final StyledElement parent, final Text textNode) {
        super(parent);
        if (textNode == null) {
            throw new NullPointerException();
        }
        this.textNode = textNode;
    }
    
    public Text getTextNode() {
        return textNode;
    }
    
    public String getTextContent() {
        return textNode.getData();
    }
    
    @Override
    public StyleData getStyleData() {
        return getParent().getStyleData();
    }
    
    @Override
    public Node createDOM(final Document document) {
        final Element node = document.createElement("StyledDOMText");
        node.appendChild(document.createTextNode(textNode.getData()));
        return node;
    }
    
    @Override
    public String toString() {
        return "{textNode: " + textNode + "}";
    }
    
}
