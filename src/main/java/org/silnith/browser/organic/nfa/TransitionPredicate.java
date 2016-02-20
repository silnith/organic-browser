package org.silnith.browser.organic.nfa;


public interface TransitionPredicate<T> {
    
    boolean matches(T t);

}
