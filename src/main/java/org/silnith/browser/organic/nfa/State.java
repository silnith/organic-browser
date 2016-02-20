package org.silnith.browser.organic.nfa;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class State<T> {
    
    private final boolean acceptState;
    
    private State<T> noMatchState;
    
    private final List<TransitionPredicate<T>> predicates;
    private final List<State<T>> targetStates;
    
    public State(final boolean acceptState) {
        super();
        this.acceptState = acceptState;
        this.noMatchState = null;
        this.predicates = new ArrayList<>();
        this.targetStates = new ArrayList<>();
    }
    
    public void setNoMatchState(final State<T> noMatchState) {
        this.noMatchState = noMatchState;
    }
    
    public void addEdge(final TransitionPredicate<T> predicate, final State<T> state) {
        predicates.add(predicate);
        targetStates.add(state);
    }
    
    public Set<State<T>> getNextState(final T t) {
        final Set<State<T>> nextStates = new HashSet<>();
        for (int i = 0; i < predicates.size(); i++) {
            final TransitionPredicate<T> transitionPredicate = predicates.get(i);
            final State<T> state = targetStates.get(i);
            
            if (transitionPredicate.matches(t)) {
                nextStates.add(state);
            }
        }
        if (nextStates.isEmpty() && noMatchState != null) {
            nextStates.add(noMatchState);
        }
        return nextStates;
    }

    public boolean isAcceptState() {
        return acceptState;
    }

}
