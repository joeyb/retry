package org.joeyb.retry;

import java.util.concurrent.Callable;

/**
 * {@code MaxDelaySinceFirstAttemptStop} is an implementation of {@link Stop} that stops after the given maximum delay
 * since the first attempt, in milliseconds.
 *
 * @param <V> the type of the return value of the {@link Callable}
 */
public class MaxDelaySinceFirstAttemptStop<V> implements Stop<V> {

    private final long maxDelaySinceFirstAttempt;

    MaxDelaySinceFirstAttemptStop(long maxDelay) {
        this.maxDelaySinceFirstAttempt = maxDelay;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stop(Attempt<V> attempt) {
        return attempt.delaySinceFirstAttempt() >= maxDelaySinceFirstAttempt();
    }

    long maxDelaySinceFirstAttempt() {
        return maxDelaySinceFirstAttempt;
    }
}
