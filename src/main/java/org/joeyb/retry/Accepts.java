package org.joeyb.retry;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;

import javax.annotation.Nonnull;

public class Accepts {

    /**
     * Returns a composite {@link Accept} implementation that accepts if all of the underlying {@link Accept} instances
     * return {@code true} (logical AND).
     *
     * @param <V> the return type of the underlying {@link Callable}
     * @param accepts the underlying {@link Accept} instances to test
     */
    @SafeVarargs
    public static <V> Accept<V> and(@Nonnull Accept<V>... accepts) {
        return and(Arrays.asList(accepts));
    }

    /**
     * Returns a composite {@link Accept} implementation that accepts if all of the underlying {@link Stop} instances
     * return {@code true} (logical AND).
     *
     * @param <V> the return type of the underlying {@link Callable}
     * @param accepts the underlying {@link Accept} instances to test
     */
    public static <V> Accept<V> and(@Nonnull Collection<Accept<V>> accepts) {
        if (accepts.size() == 0) {
            return Accepts.result();
        }

        if (accepts.size() == 1) {
            return accepts.iterator().next();
        }

        return new CompositeAndAccept<>(accepts);
    }

    /**
     * Returns a composite {@link Accept} implementation that accepts if any of the underlying {@link Accept} instances
     * return {@code true} (logical OR).
     *
     * @param <V> the return type of the underlying {@link Callable}
     * @param accepts the underlying {@link Accept} instances to test
     */
    @SafeVarargs
    public static <V> Accept<V> or(@Nonnull Accept<V>... accepts) {
        return or(Arrays.asList(accepts));
    }

    /**
     * Returns a composite {@link Accept} implementation that accepts if any of the underlying {@link Stop} instances
     * return {@code true} (logical OR).
     *
     * @param <V> the return type of the underlying {@link Callable}
     * @param accepts the underlying {@link Accept} instances to test
     */
    public static <V> Accept<V> or(@Nonnull Collection<Accept<V>> accepts) {
        if (accepts.size() == 0) {
            return Accepts.result();
        }

        if (accepts.size() == 1) {
            return accepts.iterator().next();
        }

        return new CompositeOrAccept<>(accepts);
    }

    /**
     * Returns an {@link Accept} implementation that accepts an attempt with a result, regardless of its value.
     *
     * @param <V> the return type of the underlying {@link Callable}
     */
    public static <V> Accept<V> result() {
        return new ResultAccept<>();
    }

    private Accepts() { }
}
