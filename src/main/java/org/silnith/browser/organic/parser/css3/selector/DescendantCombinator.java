package org.silnith.browser.organic.parser.css3.selector;

import java.util.Collection;
import java.util.HashSet;

import org.silnith.browser.organic.StyledContent;
import org.silnith.browser.organic.StyledDOMElement;
import org.w3c.dom.Element;


public class DescendantCombinator implements Combinator {
    
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
        final Collection<StyledDOMElement> children = new HashSet<>();
        for (final StyledDOMElement child : source) {
            children.add(child);
            children.addAll(getChildren(child));
        }
        return next.select(children);
    }
    
    private Collection<StyledDOMElement> getChildren(final StyledDOMElement styledDOMElement) {
        final Collection<StyledDOMElement> children = new HashSet<>();
        for (final StyledContent styledContent : styledDOMElement.getChildren()) {
            if (styledContent instanceof StyledDOMElement) {
                final StyledDOMElement childElement = (StyledDOMElement) styledContent;
                children.add(childElement);
                children.addAll(getChildren(childElement));
            }
        }
        return children;
    }

    @Override
    public String toString() {
        return " " + next;
    }
    
}
