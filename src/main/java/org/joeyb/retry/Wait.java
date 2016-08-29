package org.joeyb.retry;

import java.util.concurrent.Callable;

/**
 * {@code Wait} defines the interface for the retry's wait strategy, which dictates how long to wait between failed
 * attempts.
 *
 * @param <V> the type of the return value of the {@link Callable}
 */
public interface Wait<V> {

    /**
     * Returns the time to wait between failed attempts, in milliseconds.
     *
     * @param attempt the last failed attempt
     */
    long waitTime(Attempt<V> attempt);
}
