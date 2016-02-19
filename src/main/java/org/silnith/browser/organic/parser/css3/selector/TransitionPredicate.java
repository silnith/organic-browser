package org.silnith.browser.organic.parser.css3.selector;

import org.w3c.dom.Element;

public interface TransitionPredicate {
    
    /**
     * Returns {@code true} if the element matches the criteria for allowing the
     * CSS selector NDFA to transition from one state to the next.
     * 
     * @param element
     * @return
     */
    boolean matches(Element element);
    
}
