package org.silnith.browser.organic.parser.css3.selector;

import org.w3c.dom.Element;


public class ClassSelector implements SimpleSelector {
    
    private final String className;
    
    public ClassSelector(String className) {
        super();
        this.className = className;
    }

    @Override
    public boolean matches(Element element) {
        final String classes = element.getAttribute("class");
        final String[] split = classes.split("\\s+");
        for (final String one : split) {
            if (className.equals(one)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "." + className;
    }
    
}
