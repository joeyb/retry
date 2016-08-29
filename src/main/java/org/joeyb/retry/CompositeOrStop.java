package org.joeyb.retry;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.Callable;

import javax.annotation.Nonnull;

/**
 * {@code CompositeOrStop} is an implementation of {@link Stop} that accepts a collection of stops and returns
 * {@code true} if any of them return {@code true} (logical OR).
 *
 * @param <V> the type of the return value of the {@link Callable}
 */
class CompositeOrStop<V> implements Stop<V> {

    private final Collection<Stop<V>> stops;

    CompositeOrStop(@Nonnull Collection<Stop<V>> stops) {
        this.stops = Collections.unmodifiableCollection(new LinkedList<>(stops));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean stop(Attempt<V> attempt) {
        return stops().stream().anyMatch(s -> s.stop(attempt));
    }

    Collection<Stop<V>> stops() {
        return stops;
    }
}
