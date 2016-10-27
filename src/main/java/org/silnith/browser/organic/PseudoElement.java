package org.silnith.browser.organic;

import java.util.Locale;

import org.silnith.css.model.data.PropertyName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class PseudoElement extends StyledElement {
    
    private final String tagName;
    
    public PseudoElement(final StyledDOMElement parent, final String tagName, final StyleData styleData) {
        super(parent, styleData);
        this.tagName = tagName;
    }

    @Override
    public Locale getLanguage() {
        return getParent().getLanguage();
    }

    @Override
    public String getTagName() {
        return tagName;
    }
    
    @Override
    public Node createDOM(final Document document) {
        final Element node = document.createElement("PseudoElement");
        node.setAttribute("tagName", getTagName());
        for (final PropertyName propertyName : PropertyName.values()) {
            if (getStyleData().isPropertyComputed(propertyName)) {
                node.setAttribute(propertyName.getKey(), String.valueOf(getStyleData().getComputedValue(propertyName)));
            }
        }
        for (final StyledContent child : getChildren()) {
            node.appendChild(child.createDOM(document));
        }
        return node;
    }
    
    @Override
    public synchronized String toString() {
        return tagName + " {styleData: " + getStyleData() + "}";
    }
    
}
