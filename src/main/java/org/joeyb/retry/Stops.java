package org.joeyb.retry;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Callable;

import javax.annotation.Nonnull;

/**
 * {@code Stops} provides static helper methods for working with implementations of {@link Stop}.
 */
public class Stops {

    /**
     * Returns a composite {@link Stop} implementation that stops if all of the underlying {@link Stop} instances return
     * {@code true} (logical AND).
     *
     * @param <V> the return type of the underlying {@link Callable}
     * @param stops the underlying {@link Stop} instances to test
     */
    @SafeVarargs
    public static <V> Stop<V> and(@Nonnull Stop<V>... stops) {
        return and(Arrays.asList(stops));
    }

    /**
     * Returns a composite {@link Stop} implementation that stops if all of the underlying {@link Stop} instances return
     * {@code true} (logical AND).
     *
     * @param <V> the return type of the underlying {@link Callable}
     * @param stops the underlying {@link Stop} instances to test
     */
    public static <V> Stop<V> and(@Nonnull Collection<Stop<V>> stops) {
        if (stops.size() == 0) {
            return Stops.never();
        }

        if (stops.size() == 1) {
            return stops.iterator().next();
        }

        return new CompositeAndStop<>(stops);
    }

    /**
     * Returns an implementation of {@link Stop} that stops after the given maximum number of retry attempts.
     *
     * @param <V> the return type of the underlying {@link Callable}
     * @param maxAttempts the maximum number of retry attempts
     */
    public static <V> Stop<V> maxAttempts(long maxAttempts) {
        return new MaxAttemptsStop<>(maxAttempts);
    }

    /**
     * Returns an implementation of {@link Stop} that stops after the given maximum delay since the first attempt
     * started, in milliseconds.
     *
     * @param <V> the return type of the underlying {@link Callable}
     * @param maxDelaySinceFirstAttempt the maximum delay since the first attempt started, in milliseconds
     */
    public static <V> Stop<V> maxDelaySinceFirstAttempt(long maxDelaySinceFirstAttempt) {
        return new MaxDelaySinceFirstAttemptStop<>(maxDelaySinceFirstAttempt);
    }

    /**
     * Returns an implementation of {@link Stop} that never stops.
     *
     * @param <V> the return type of the underlying {@link Callable}
     */
    public static <V> Stop<V> never() {
        return new NeverStop<>();
    }

    /**
     * Returns a composite {@link Stop} implementation that stops if any of the underlying {@link Stop} instances return
     * {@code true} (logical OR).
     *
     * @param <V> the return type of the underlying {@link Callable}
     * @param stops the underlying {@link Stop} instances to test
     */
    @SafeVarargs
    public static <V> Stop<V> or(@Nonnull Stop<V>... stops) {
        return or(Arrays.asList(stops));
    }

    /**
     * Returns a composite {@link Stop} implementation that stops if any of the underlying {@link Stop} instances return
     * {@code true} (logical OR).
     *
     * @param <V> the return type of the underlying {@link Callable}
     * @param stops the underlying {@link Stop} instances to test
     */
    public static <V> Stop<V> or(@Nonnull Collection<Stop<V>> stops) {
        if (stops.size() == 0) {
            return Stops.never();
        }

        if (stops.size() == 1) {
            return stops.iterator().next();
        }

        return new CompositeOrStop<>(stops);
    }

    private Stops() { }
}
