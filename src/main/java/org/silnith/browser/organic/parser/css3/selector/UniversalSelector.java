package org.silnith.browser.organic.parser.css3.selector;

import org.w3c.dom.Element;


public class UniversalSelector implements SimpleSelector {
    
    @Override
    public boolean matches(Element element) {
        return true;
    }

    @Override
    public String toString() {
        return "*";
    }

}
