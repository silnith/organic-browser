package org.silnith.browser.organic.parser.css3.selector;

import org.w3c.dom.Element;


public class AttributeValueSelector implements SimpleSelector {
    
    private final String attributeName;
    
    private final String attributeValue;
    
    public AttributeValueSelector(final String attributeName, final String attributeValue) {
        super();
        this.attributeName = attributeName;
        this.attributeValue = attributeValue;
    }
    
    @Override
    public boolean matches(Element element) {
        final String attribute = element.getAttribute(attributeName);
        return attributeValue.equals(attribute);
    }

}
