package org.joeyb.retry;

import java.util.concurrent.Callable;

/**
 * {@code Accept} defines the interface for the retry's attempt acceptance strategy, which dictates whether or not the
 * attempt result is considered a failure. Acceptance is only tested on attempts have have a result.
 *
 * @param <V> the type of the return value of the underlying {@link Callable}
 */
public interface Accept<V> {

    /**
     * Returns {@code true} if the retry should accept the attempt result, or {@code false} if it should consider the
     * attempt a failure.
     *
     * @param result the current attempt result
     */
    boolean accept(V result);
}
