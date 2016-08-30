package org.joeyb.retry;

import java.util.concurrent.Callable;

/**
 * {@code Waits} provides static helper methods for working with implementations of {@link Wait}.
 */
public class Waits {

    /**
     * Returns an implementation of {@link Wait} that always returns the given wait time.
     *
     * @param waitTime the wait time to use for all attempts
     * @param <V> the return type of the underlying {@link Callable}
     */
    public static <V> Wait<V> constant(long waitTime) {
        return new ConstantWait<>(waitTime);
    }

    /**
     * Returns an implementation of {@link Wait} that always returns a wait time of 0.
     *
     * @param <V> the return type of the underlying {@link Callable}
     */
    public static <V> Wait<V> noWait() {
        return new NoWait<>();
    }

    private Waits() { }
}
