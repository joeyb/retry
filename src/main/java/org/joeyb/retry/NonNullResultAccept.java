package org.joeyb.retry;

import java.util.concurrent.Callable;

/**
 * {@code NonNullResultAccept} is an implementation of {@link Accept} that accepts all attempts with a non-null result.
 *
 * @param <V> the type of the return value of the underlying {@link Callable}
 */
class NonNullResultAccept<V> implements Accept<V> {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accept(Attempt<V> attempt) {
        return attempt.hasResult() && attempt.result() != null;
    }
}
