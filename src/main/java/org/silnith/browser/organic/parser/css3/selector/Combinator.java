package org.silnith.browser.organic.parser.css3.selector;

import java.util.Collection;

import org.silnith.browser.organic.StyledDOMElement;
import org.w3c.dom.Element;


public interface Combinator {
    
    boolean matches(Element element);
    
    Collection<StyledDOMElement> combine(Collection<StyledDOMElement> source);
    
    void setNextSelector(Selector next);
    
}
