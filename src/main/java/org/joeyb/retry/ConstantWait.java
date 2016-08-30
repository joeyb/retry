package org.joeyb.retry;

import java.util.concurrent.Callable;

/**
 * {@code ConstantWait} is an implementation of {@link Wait} always returns the given wait time.
 *
 * @param <V> the type of the return value of the {@link Callable}
 */
class ConstantWait<V> implements Wait<V> {

    private final long waitTime;

    ConstantWait(long waitTime) {
        this.waitTime = waitTime;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long waitTime(Attempt<V> attempt) {
        return waitTime;
    }

    long waitTime() {
        return waitTime;
    }
}
