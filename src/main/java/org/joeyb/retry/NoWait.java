package org.joeyb.retry;

import java.util.concurrent.Callable;

/**
 * {@code NoWait} is an implementation of {@link Wait} always returns a wait time of 0.
 *
 * @param <V> the type of the return value of the {@link Callable}
 */
class NoWait<V> implements Wait<V> {

    /**
     * {@inheritDoc}
     */
    @Override
    public long waitTime(Attempt<V> attempt) {
        return 0;
    }
}
