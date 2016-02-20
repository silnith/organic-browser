package org.silnith.browser.organic.parser.css3.selector;

import java.util.Collection;
import java.util.HashSet;

import org.silnith.browser.organic.StyledDOMElement;
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
    public Collection<StyledDOMElement> select(Collection<StyledDOMElement> candidates) {
        final Collection<StyledDOMElement> subject = new HashSet<>();
        for (final StyledDOMElement element : candidates) {
            if (element.getElement().getAttribute(attributeName).equals(attributeValue)) {
                subject.add(element);
            }
        }
        return subject;
    }

    @Override
    public boolean matches(Element element) {
        final String attribute = element.getAttribute(attributeName);
        return attributeValue.equals(attribute);
    }

}
