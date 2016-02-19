package org.silnith.browser.organic.parser.css3.selector;

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
    public String toString() {
        return identifier;
    }
    
}
