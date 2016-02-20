package org.silnith.browser.organic.parser.css3.selector;

import java.util.Collection;

import org.silnith.browser.organic.StyledDOMElement;
import org.w3c.dom.Element;


public class SelectorImpl implements Selector {
    
    private final SequenceOfSimpleSelectors sequenceOfSimpleSelectors;
    
    private final Combinator combinator;
    
    public SelectorImpl(final SequenceOfSimpleSelectors sequenceOfSimpleSelectors, final Combinator combinator) {
        super();
        this.sequenceOfSimpleSelectors = sequenceOfSimpleSelectors;
        this.combinator = combinator;
    }
    
    @Override
    public boolean matches(final Element element) {
        if (sequenceOfSimpleSelectors.matches(element)) {
            return combinator.matches(element);
        } else {
            return false;
        }
    }

    @Override
    public Collection<StyledDOMElement> select(Collection<StyledDOMElement> candidates) {
        final Collection<StyledDOMElement> selected = sequenceOfSimpleSelectors.select(candidates);
        final Collection<StyledDOMElement> combined = combinator.combine(selected);
        return combined;
    }

    @Override
    public String toString() {
        if (combinator == null) {
            return sequenceOfSimpleSelectors.toString();
        } else {
            return sequenceOfSimpleSelectors.toString() + combinator.toString();
        }
    }

}
