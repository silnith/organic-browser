package org.silnith.browser.organic;

import org.silnith.browser.organic.nfa.TransitionPredicate;
import org.w3c.dom.Element;


public class TypeSelectorTransitionPredicate implements TransitionPredicate<Element> {
    
    private final String type;
    
    public TypeSelectorTransitionPredicate(final String type) {
        super();
        this.type = type;
    }

    @Override
    public boolean matches(Element t) {
        return t.getTagName().equals(type);
    }

}
