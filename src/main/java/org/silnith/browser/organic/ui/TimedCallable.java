package org.silnith.browser.organic.ui;

import java.util.concurrent.Callable;


public class TimedCallable<V> implements Callable<V> {
    
    private final Callable<V> callable;
    
    private long duration;
    
    public TimedCallable(final Callable<V> callable) {
        super();
        this.callable = callable;
        this.duration = -1;
    }
    
    @Override
    public V call() throws Exception {
        final long startTime = System.currentTimeMillis();
        final V result = callable.call();
        final long endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        return result;
    }
    
    /**
     * Returns the duration of the {@link Callable}'s execution in milliseconds.
     * 
     * @return the duration in milliseconds
     */
    public long getDuration() {
        return duration;
    }
    
}
