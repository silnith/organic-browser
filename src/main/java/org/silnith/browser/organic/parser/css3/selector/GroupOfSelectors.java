package org.silnith.browser.organic.parser.css3.selector;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.silnith.browser.organic.StyledDOMElement;
import org.w3c.dom.Element;


public class GroupOfSelectors implements Selector {
    
    private final Collection<Selector> selectors;
    
    public GroupOfSelectors(final Collection<Selector> selectors) {
        super();
        this.selectors = selectors;
    }

    @Override
    public boolean matches(final Element element) {
        for (final Selector selector : selectors) {
            if (selector.matches(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public Collection<StyledDOMElement> select(Collection<StyledDOMElement> candidates) {
        final Collection<StyledDOMElement> subject = new HashSet<>();
        for (final Selector selector : selectors) {
            subject.addAll(selector.select(candidates));
        }
        return subject;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        final Iterator<Selector> iterator = selectors.iterator();
        final Selector first = iterator.next();
        builder.append(first.toString());
        while (iterator.hasNext()) {
            final Selector next = iterator.next();
            builder.append(", ");
            builder.append(next.toString());
        }
        return builder.toString();
    }
    
}
