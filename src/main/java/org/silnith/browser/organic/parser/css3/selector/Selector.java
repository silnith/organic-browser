package org.silnith.browser.organic.parser.css3.selector;

import org.silnith.browser.organic.StyledContent;
import org.w3c.dom.Element;


public interface Selector {
    
    boolean matches(Element element);
    
}
