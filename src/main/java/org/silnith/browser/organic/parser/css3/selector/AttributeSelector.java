package org.silnith.browser.organic.parser.css3.selector;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;


public class AttributeSelector implements SimpleSelector {
    
    private final String attributeName;
    
    public AttributeSelector(String attributeName) {
        super();
        this.attributeName = attributeName;
    }

    @Override
    public boolean matches(Element element) {
        final Attr attributeNode = element.getAttributeNode(attributeName);
        return attributeNode != null;
    }

}
