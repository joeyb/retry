package org.joeyb.retry;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.Callable;

import javax.annotation.Nonnull;

/**
 * {@code CompositeAndStop} is an implementation of {@link Stop} that accepts a collection of stops and returns
 * {@code true} if all of them return {@code true} (logical AND).
 *
 * @param <V> the type of the return value of the {@link Callable}
 */
class CompositeAndStop<V> implements Stop<V> {

    private final Collection<Stop<V>> stops;

    CompositeAndStop(@Nonnull Collection<Stop<V>> stops) {
        this.stops = Collections.unmodifiableCollection(new LinkedList<>(stops));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stop(Attempt<V> attempt) {
        return stops().stream().allMatch(s -> s.stop(attempt));
    }

    Collection<Stop<V>> stops() {
        return stops;
    }
}
