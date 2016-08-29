package org.joeyb.retry;

import java.util.concurrent.Callable;

/**
 * {@code NeverStop} is an implementation of {@link Stop} that never stops.
 *
 * @param <V> the type of the return value of the {@link Callable}
 */
class NeverStop<V> implements Stop<V> {

    @Override
    public boolean stop(Attempt<V> attempt) {
        return false;
    }
}
