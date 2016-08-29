package org.joeyb.retry;

import java.util.concurrent.Callable;

/**
 * {@code NonNullAccept} is an implementation of {@link Accept} that accepts all attempts with a non-null result.
 *
 * @param <V> the type of the return value of the underlying {@link Callable}
 */
class NonNullAccept<V> implements Accept<V> {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accept(V result) {
        return result != null;
    }
}
