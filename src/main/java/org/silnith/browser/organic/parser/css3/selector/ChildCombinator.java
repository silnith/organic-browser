package org.silnith.browser.organic.parser.css3.selector;

import org.w3c.dom.Element;


public class ChildCombinator implements Combinator {
    
    @Override
    public boolean matches(Element element) {
        return false;
    }
    
    @Override
    public String toString() {
        return " > ";
    }
    
}
