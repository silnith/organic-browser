package org.silnith.browser.organic.parser.css3.selector;

import java.util.Collection;
import java.util.HashSet;

import org.silnith.browser.organic.StyledDOMElement;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;


public class AttributeSelector implements SimpleSelector {
    
    private final String attributeName;
    
    public AttributeSelector(String attributeName) {
        super();
        this.attributeName = attributeName;
    }

    @Override
    public Collection<StyledDOMElement> select(Collection<StyledDOMElement> candidates) {
        final Collection<StyledDOMElement> subject = new HashSet<>();
        for (final StyledDOMElement element : candidates) {
            final Attr attributeNode = element.getElement().getAttributeNode(attributeName);
            if (attributeNode != null) {
                subject.add(element);
            }
        }
        return subject;
    }

    @Override
    public boolean matches(Element element) {
        final Attr attributeNode = element.getAttributeNode(attributeName);
        return attributeNode != null;
    }

}
