package org.silnith.browser.organic.parser.css3.selector;

import java.util.Collection;
import java.util.HashSet;

import org.silnith.browser.organic.StyledContent;
import org.silnith.browser.organic.StyledDOMElement;
import org.w3c.dom.Element;


public class ChildCombinator implements Combinator {
    
    private Selector next;
    
    @Override
    public boolean matches(Element element) {
        return false;
    }
    
    @Override
    public void setNextSelector(Selector next) {
        this.next = next;
    }

    @Override
    public Collection<StyledDOMElement> combine(Collection<StyledDOMElement> source) {
        final Collection<StyledDOMElement> subject = new HashSet<>();
        for (final StyledDOMElement element : source) {
            for (final StyledContent child : element.getChildren()) {
                if (child instanceof StyledDOMElement) {
                    final StyledDOMElement childElement = (StyledDOMElement) child;
                    subject.add(childElement);
                }
            }
        }
        return next.select(subject);
    }

    @Override
    public String toString() {
        return " > " + next;
    }
    
}
