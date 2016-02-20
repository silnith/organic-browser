package org.silnith.browser.organic.parser.css3.selector;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import org.silnith.browser.organic.StyledDOMElement;
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
    public Collection<StyledDOMElement> select(Collection<StyledDOMElement> candidates) {
        final Collection<StyledDOMElement> subject = new HashSet<>();
        for (final StyledDOMElement element : candidates) {
            final String classes = element.getElement().getAttribute("class");
            final String[] split = classes.split("\\s+");
            if (Arrays.asList(split).contains(className)) {
                subject.add(element);
            }
        }
        return subject;
    }

    @Override
    public String toString() {
        return "." + className;
    }
    
}
