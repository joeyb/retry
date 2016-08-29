package org.joeyb.retry;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * {@code Retry} defines a retry strategy for an underlying {@link Callable}.
 *
 * @param <V> the return type of the underlying {@link Callable}
 */
public class Retry<V> {

    private final Block block;
    private final Stop<V> stop;
    private final Wait<V> wait;

    public Retry(Block block, Stop<V> stop, Wait<V> wait) {
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

            if (attempt.hasResult()) {
                return attempt.result();
            }

            if (stop.stop(attempt)) {
                throw new RetryException(attempt);
            }

            long waitTime = wait.waitTime(attempt);

            try {
                block.block(waitTime);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RetryException(attempt);
            }

            attemptNumber++;
        }
    }
}
