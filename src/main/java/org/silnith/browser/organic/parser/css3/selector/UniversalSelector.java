package org.silnith.browser.organic.parser.css3.selector;

import java.util.Collection;

import org.silnith.browser.organic.StyledDOMElement;
import org.w3c.dom.Element;


public class UniversalSelector implements SimpleSelector {
    
    @Override
    public boolean matches(Element element) {
        return true;
    }

    @Override
    public Collection<StyledDOMElement> select(Collection<StyledDOMElement> candidates) {
        return candidates;
    }

    @Override
    public String toString() {
        return "*";
    }

}
