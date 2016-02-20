package org.silnith.browser.organic;

import java.util.Collection;

import org.silnith.browser.organic.nfa.TransitionPredicate;


/**
 * A predicate that returns {@code true} when all the delegate predicates
 * return {@code true}.  If there are no delegate predicates, defaults to
 * returning {@code true}.
 * 
 * @param <T>
 * @author kent
 */
public class TransitionPredicateGroup<T> implements TransitionPredicate<T> {
    
    private final Collection<TransitionPredicate<T>> predicates;
    
    public TransitionPredicateGroup(Collection<TransitionPredicate<T>> predicates) {
        super();
        this.predicates = predicates;
    }

    @Override
    public boolean matches(T t) {
        for (final TransitionPredicate<T> predicate : predicates) {
            if (!predicate.matches(t)) {
                return false;
            }
        }
        return true;
    }
    
}
