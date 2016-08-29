package org.joeyb.retry;

import java.util.concurrent.Callable;

/**
 * {@code Stop} defines the interface for the retry's stop strategy, which dictates when to give up retrying.
 *
 * @param <V> the type of the return value of the {@link Callable}
 */
public interface Stop<V> {

    /**
     * Returns {@code true} if the retry should give up, or {@code false} if it should make another attempt.
     *
     * @param attempt the last failed attempt
     */
    boolean stop(Attempt<V> attempt);
}
