package org.joeyb.retry;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.annotation.concurrent.NotThreadSafe;
import javax.annotation.concurrent.ThreadSafe;

/**
 * {@code Retry} defines a retry strategy for an underlying {@link Callable}.
 *
 * @param <V> the return type of the underlying {@link Callable}
 */
@ThreadSafe
public class Retry<V> {

    private final Accept<V> accept;
    private final Block block;
    private final Stop<V> stop;
    private final Wait<V> wait;

    public Retry(Accept<V> accept, Block block, Stop<V> stop, Wait<V> wait) {
        this.accept = accept;
        this.block = block;
        this.stop = stop;
        this.wait = wait;
    }

    /**
     * Returns the result of the given {@link Callable} using the configured retry strategy.
     *
     * @param callable the {@link Callable} to be called
     */
    public V call(Callable<V> callable) {
        long startTime = System.nanoTime();

        long attemptNumber = 1;

        while (true) {
            Attempt<V> attempt;

            try {
                V result = callable.call();

                attempt = Attempts.result(
                        attemptNumber,
                        TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime),
                        result);
            } catch (Exception e) {
                attempt = Attempts.exception(
                        attemptNumber,
                        TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startTime),
                        e);
            }

            if (attempt.hasResult() && accept.accept(attempt.result())) {
                return attempt.result();
            }

            if (getStop().stop(attempt)) {
                throw new RetryException(attempt);
            }

            long waitTime = getWait().waitTime(attempt);

            try {
                getBlock().block(waitTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RetryException(attempt);
            }

            attemptNumber++;
        }
    }

    /**
     * Returns a new {@link RetryBuilder} instance.
     *
     * @param <V> the return type of the underlying {@link Callable}
     */
    public static <V> RetryBuilder<V> newBuilder() {
        return new RetryBuilder<>();
    }

    Accept<V> getAccept() {
        return accept;
    }

    Block getBlock() {
        return block;
    }

    Stop<V> getStop() {
        return stop;
    }

    Wait<V> getWait() {
        return wait;
    }

    /**
     * {@code RetryBuilder} provides a fluent builder interface for generating a {@link Retry} instance.
     *
     * @param <V> the return type of the underlying {@link Callable}
     */
    @NotThreadSafe
    public static class RetryBuilder<V> {

        private Collection<Accept<V>> accepts = new LinkedList<>();
        private Block block = Blocks.threadSleep();
        private Collection<Stop<V>> stops = new LinkedList<>();
        private Wait<V> wait = Waits.noWait();

        /**
         * Returns an instance of {@link Retry} built using this builder's config.
         */
        public Retry<V> build() {
            return new Retry<>(
                    Accepts.or(accepts),
                    block,
                    Stops.or(stops),
                    wait);
        }

        /**
         * Adds the given {@link Accept} instance.
         *
         * @param accept the {@link Accept} implementation to add
         * @return the in-progress builder
         */
        public RetryBuilder<V> accept(Accept<V> accept) {
            this.accepts.add(accept);
            return this;
        }

        /**
         * Adds an {@link Accept} that accepts any attempt with a result, regardless of value.
         *
         * @return the in-progress builder
         */
        public RetryBuilder<V> acceptAnyResult() {
            this.accepts.add(Accepts.any());
            return this;
        }

        /**
         * Adds an {@link Accept} that accepts any attempt with a non-null result, regardless of value.
         *
         * @return the in-progress builder
         */
        public RetryBuilder<V> acceptNonNullResult() {
            this.accepts.add(Accepts.nonNull());
            return this;
        }

        /**
         * Sets the {@link Block} implementation to use.
         *
         * @param block the {@link Block} implementation to use
         * @return the in-progress builder
         */
        public RetryBuilder<V> block(Block block) {
            this.block = block;
            return this;
        }

        /**
         * Sets the {@link Wait} to an implementation that always waits the given amount of time, in milliseconds.
         *
         * @param waitTime the amount of time to wait between attempts, in milliseconds.
         */
        public RetryBuilder<V> constantWait(long waitTime) {
            this.wait = Waits.constant(waitTime);
            return this;
        }

        /**
         * Adds a {@link Stop} that stops after the given maximum number of attempts.
         *
         * @param maxAttempts the maximum number of failed attempts
         * @return the in-progress builder
         */
        public RetryBuilder<V> maxAttempts(long maxAttempts) {
            this.stops.add(Stops.maxAttempts(maxAttempts));
            return this;
        }

        /**
         * Adds a {@link Stop} that stops after the given maximum delay since the first attempt, in milliseconds.
         *
         * @param maxDelaySinceFirstAttempt the maximum delay since the first attempt, in milliseconds
         * @return the in-progress builder
         */
        public RetryBuilder<V> maxDelaySinceFirstAttempt(long maxDelaySinceFirstAttempt) {
            this.stops.add(Stops.maxDelaySinceFirstAttempt(maxDelaySinceFirstAttempt));
            return this;
        }

        /**
         * Adds a {@link Stop} that never stops.
         *
         * @return the in-progress builder
         */
        public RetryBuilder<V> neverStop() {
            this.stops.add(Stops.never());
            return this;
        }

        /**
         * Adds the given {@link Stop} instance.
         *
         * @param stop the {@link Stop} implementation to add
         * @return the in-progress builder
         */
        public RetryBuilder<V> stop(Stop<V> stop) {
            this.stops.add(stop);
            return this;
        }

        /**
         * Sets the {@link Wait} implementation to use.
         *
         * @param wait the {@link Wait} implementation to use
         * @return the in-progress builder
         */
        public RetryBuilder<V> wait(Wait<V> wait) {
            this.wait = wait;
            return this;
        }
    }
}
