package org.joeyb.retry;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.Callable;

import javax.annotation.Nonnull;

/**
 * {@code CompositeOrAccept} is an implementation of {@link Accept} that accepts a collection of {@link Accept}s and
 * returns {@code true} if any of them return {@code true} (logical OR).
 *
 * @param <V> the type of the return value of the {@link Callable}
 */
public class CompositeOrAccept<V> implements Accept<V> {

    private final Collection<Accept<V>> accepts;

    public CompositeOrAccept(@Nonnull Collection<Accept<V>> accepts) {
        this.accepts = Collections.unmodifiableCollection(new LinkedList<>(accepts));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accept(Attempt<V> attempt) {
        return accepts().stream().anyMatch(a -> a.accept(attempt));
    }

    Collection<Accept<V>> accepts() {
        return accepts;
    }
}
