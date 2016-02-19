package org.silnith.browser.organic.parser.css3.selector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Element;


public class SequenceOfSimpleSelectors implements Selector {
    
    private final List<SimpleSelector> selectors;
    
    public SequenceOfSimpleSelectors(final TypeSelector typeSelector, final SimpleSelector... additionalSelectors) {
        super();
        if (typeSelector == null) {
            throw new IllegalArgumentException();
        }
        for (final SimpleSelector additionalSelector : additionalSelectors) {
            if (isTypeSelector(additionalSelector)) {
                throw new IllegalArgumentException();
            }
        }
        this.selectors = new ArrayList<>(additionalSelectors.length + 1);
        this.selectors.add(typeSelector);
        this.selectors.addAll(Arrays.asList(additionalSelectors));
    }

    public SequenceOfSimpleSelectors(final UniversalSelector universalSelector, final SimpleSelector... additionalSelectors) {
        super();
        if (universalSelector == null) {
            throw new IllegalArgumentException();
        }
        for (final SimpleSelector additionalSelector : additionalSelectors) {
            if (isTypeSelector(additionalSelector)) {
                throw new IllegalArgumentException();
            }
        }
        this.selectors = new ArrayList<>(additionalSelectors.length + 1);
        this.selectors.add(universalSelector);
        this.selectors.addAll(Arrays.asList(additionalSelectors));
    }
    
    public SequenceOfSimpleSelectors(final List<SimpleSelector> selectors) {
        super();
        if (selectors.isEmpty()) {
            throw new IllegalArgumentException();
        }
        final Iterator<SimpleSelector> iterator = selectors.iterator();
        final SimpleSelector first = iterator.next();
        if (!isTypeSelector(first)) {
            throw new IllegalArgumentException();
        }
        while (iterator.hasNext()) {
            final SimpleSelector next = iterator.next();
            if (isTypeSelector(next)) {
                throw new IllegalArgumentException();
            }
        }
        this.selectors = selectors;
    }
    
    private static boolean isTypeSelector(final SimpleSelector simpleSelector) {
        return simpleSelector instanceof TypeSelector || simpleSelector instanceof UniversalSelector;
    }

    @Override
    public boolean matches(final Element element) {
        assert !selectors.isEmpty();
        
        for (final SimpleSelector simpleSelector : selectors) {
            if (!simpleSelector.matches(element)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        for (final SimpleSelector selector : selectors) {
            builder.append(selector.toString());
        }
        return builder.toString();
    }
    
}
