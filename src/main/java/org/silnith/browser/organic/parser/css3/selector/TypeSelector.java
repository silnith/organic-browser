package org.silnith.browser.organic.parser.css3.selector;

import java.util.Collection;
import java.util.HashSet;

import org.silnith.browser.organic.StyledDOMElement;
import org.w3c.dom.Element;


/**
 * Matches a CSS qualified name. This is an identifier with an optional
 * namespace prefix.
 */
public class TypeSelector implements SimpleSelector {
    
    private final String identifier;
    
    public TypeSelector(final String identifier) {
        super();
        this.identifier = identifier;
    }
    
    @Override
    public boolean matches(Element element) {
        return identifier.equals(element.getTagName());
    }

    @Override
    public Collection<StyledDOMElement> select(Collection<StyledDOMElement> candidates) {
        final Collection<StyledDOMElement> subject = new HashSet<>();
        for (final StyledDOMElement element : candidates) {
            if (element.getElement().getTagName().equals(identifier)) {
                subject.add(element);
            }
        }
        return subject;
    }

    @Override
    public String toString() {
        return identifier;
    }
    
}
