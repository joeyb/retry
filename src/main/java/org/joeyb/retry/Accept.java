package org.joeyb.retry;

import java.util.concurrent.Callable;

/**
 * {@code Stop} defines the interface for the retry's attempt acceptance strategy, which dictates whether or not the
 * attempt is considered a failure.
 *
 * @param <V> the type of the return value of the underlying {@link Callable}
 */
public interface Accept<V> {

    /**
     * Returns {@code true} if the retry should accept the attempt, or {@code false} if it should consider the attempt a
     * failure.
     *
     * @param attempt the current attempt
     */
    boolean accept(Attempt<V> attempt);
}
