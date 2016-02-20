package org.silnith.browser.organic;

import org.silnith.browser.organic.nfa.TransitionPredicate;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class PreviousElementTransitionPredicate implements TransitionPredicate<Element> {
    
    private final TransitionPredicate<Element> previousElementMatcher;
    
    public PreviousElementTransitionPredicate(final TransitionPredicate<Element> previousSiblingMatcher) {
        super();
        this.previousElementMatcher = previousSiblingMatcher;
    }

    @Override
    public boolean matches(final Element t) {
        Node previousSibling = t.getPreviousSibling();
        while (previousSibling != null && previousSibling.getNodeType() != Node.ELEMENT_NODE) {
            previousSibling = previousSibling.getPreviousSibling();
        }
        if (previousSibling == null) {
            return false;
        } else {
            final Element previousElement = (Element) previousSibling;
            return previousElementMatcher.matches(previousElement);
        }
    }
    
}
