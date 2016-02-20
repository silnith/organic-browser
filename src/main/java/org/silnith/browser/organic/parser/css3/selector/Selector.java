package org.silnith.browser.organic.parser.css3.selector;

import java.util.Collection;

import org.silnith.browser.organic.StyledContent;
import org.silnith.browser.organic.StyledDOMElement;
import org.w3c.dom.Element;


public interface Selector {
    
    boolean matches(Element element);
    
    Collection<StyledDOMElement> select(Collection<StyledDOMElement> candidates);
    
}
