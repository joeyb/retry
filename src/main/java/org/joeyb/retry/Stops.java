package org.joeyb.retry;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Callable;

/**
 * {@code Stops} provides static helper methods for working with implementations of {@link Stop}.
 */
public class Stops {

    /**
     * Returns a composite {@link Stop} implementation that stops if any of the underlying {@link Stop} instances return
     * {@code true}.
     *
     * @param <V> the return type of the underlying {@link Callable}
     * @param stops the underlying {@link Stop} instances to test
     */
    @SafeVarargs
    public static <V> Stop<V> composite(Stop<V>... stops) {
        return new CompositeStop<>(stops);
    }

    /**
     * Returns a composite {@link Stop} implementation that stops if any of the underlying {@link Stop} instances return
     * {@code true}.
     *
     * @param <V> the return type of the underlying {@link Callable}
     * @param stops the underlying {@link Stop} instances to test
     */
    public static <V> Stop<V> composite(Collection<Stop<V>> stops) {
        return new CompositeStop<>(stops);
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

    private static class CompositeStop<V> implements Stop<V> {

        private final Collection<Stop<V>> stops;

        @SafeVarargs
        CompositeStop(Stop<V>... stops) {
            this.stops = Arrays.asList(stops);
        }

        CompositeStop(Collection<Stop<V>> stops) {
            this.stops = new LinkedList<>(stops);
        }

        @Override
        public boolean stop(Attempt<V> attempt) {
            return stops.stream().anyMatch(s -> s.stop(attempt));
        }
    }

    private static class MaxAttemptsStop<V> implements Stop<V> {

        private final long maxAttempts;

        MaxAttemptsStop(long maxAttempts) {
            this.maxAttempts = maxAttempts;
        }

        @Override
        public boolean stop(Attempt<V> attempt) {
            return attempt.attemptNumber() >= maxAttempts;
        }
    }

    private static class MaxDelaySinceFirstAttemptStop<V> implements Stop<V> {

        private final long maxDelaySinceFirstAttempt;

        MaxDelaySinceFirstAttemptStop(long maxDelay) {
            this.maxDelaySinceFirstAttempt = maxDelay;
        }

        @Override
        public boolean stop(Attempt<V> attempt) {
            return attempt.delaySinceFirstAttempt() >= maxDelaySinceFirstAttempt;
        }
    }

    private static class NeverStop<V> implements Stop<V> {

        @Override
        public boolean stop(Attempt<V> attempt) {
            return false;
        }
    }

    private Stops() { }
}
