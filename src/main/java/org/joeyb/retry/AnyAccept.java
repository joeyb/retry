package org.joeyb.retry;

import java.util.concurrent.Callable;

/**
 * {@code AnyAccept} is an implementation of {@link Accept} that accepts all attempts with a result.
 *
 * @param <V> the type of the return value of the underlying {@link Callable}
 */
class AnyAccept<V> implements Accept<V> {

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accept(V result) {
        return true;
    }
}
