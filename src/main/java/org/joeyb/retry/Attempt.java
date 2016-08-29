package org.joeyb.retry;

import java.util.concurrent.Callable;

/**
 * {@code Attempt} represents a single attempt at calling a {@link Callable}. It either returned a result or threw an
 * exception.
 *
 * @param <V> the type of the return value of the {@link Callable}
 */
public interface Attempt<V> {

    /**
     * Returns the attempt number, starting at 1.
     */
    long attemptNumber();

    /**
     * Returns the total delay, in milliseconds, since the first attempt.
     */
    long delaySinceFirstAttempt();

    /**
     * Returns the exception thrown by the underlying {@link Callable}.
     *
     * @throws IllegalStateException if there is no error, as indicated by {@link #hasException()}
     */
    Throwable exception();

    /**
     * Returns the result if the attempt has a result, otherwise throws its exception.
     *
     * @throws Throwable if the attempt has an exception
     */
    V get() throws Throwable;

    /**
     * Returns {@code true} if the underlying {@link Callable} failed, otherwise returns {@code false}.
     */
    boolean hasException();

    /**
     * Returns {@code true} if the underlying {@link Callable} returned a result, otherwise returns {@code false}.
     */
    boolean hasResult();

    /**
     * Returns the result of the underlying {@link Callable}.
     *
     * @throws IllegalStateException if there is no result, as indicated by {@link #hasResult()}
     */
    V result();
}
