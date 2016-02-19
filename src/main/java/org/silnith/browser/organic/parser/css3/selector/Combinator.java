package org.silnith.browser.organic.parser.css3.selector;

import org.w3c.dom.Element;


public interface Combinator {
    
    boolean matches(Element element);
    
}
