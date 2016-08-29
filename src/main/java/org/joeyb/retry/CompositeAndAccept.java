package org.joeyb.retry;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.concurrent.Callable;

import javax.annotation.Nonnull;

/**
 * {@code CompositeAndAccept} is an implementation of {@link Accept} that accepts a collection of {@link Accept}s and
 * returns {@code true} if all of them return {@code true} (logical AND).
 *
 * @param <V> the type of the return value of the {@link Callable}
 */
public class CompositeAndAccept<V> implements Accept<V> {

    private final Collection<Accept<V>> accepts;

    public CompositeAndAccept(@Nonnull Collection<Accept<V>> accepts) {
        this.accepts = Collections.unmodifiableCollection(new LinkedList<>(accepts));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean accept(Attempt<V> attempt) {
        return accepts().stream().allMatch(a -> a.accept(attempt));
    }

    Collection<Accept<V>> accepts() {
        return accepts;
    }
}
