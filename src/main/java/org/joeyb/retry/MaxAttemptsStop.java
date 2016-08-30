package org.joeyb.retry;

import java.util.concurrent.Callable;

/**
 * {@code MaxAttemptsStop} is an implementation of {@link Stop} that stops after the given maximum number of failed
 * attempts.
 *
 * @param <V> the type of the return value of the {@link Callable}
 */
class MaxAttemptsStop<V> implements Stop<V> {

    private final long maxAttempts;

    MaxAttemptsStop(long maxAttempts) {
        if (maxAttempts < 1) {
            throw new IllegalArgumentException("maxAttempts must be greater than 1");
        }

        this.maxAttempts = maxAttempts;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stop(Attempt<V> attempt) {
        return attempt.attemptNumber() >= maxAttempts();
    }

    long maxAttempts() {
        return maxAttempts;
    }
}
