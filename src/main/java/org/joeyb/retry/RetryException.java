package org.joeyb.retry;

/**
 * {@code RetryException} is a {@link RuntimeException} indicating that retrying has failed and we are giving up.
 */
public class RetryException extends RuntimeException {

    private final Attempt<?> attempt;

    public RetryException(Attempt<?> attempt) {
        super("Retry failed after " + attempt.attemptNumber() + " attempts.");

        this.attempt = attempt;
    }

    /**
     * Returns the last {@link Attempt}.
     */
    public Attempt<?> attempt() {
        return attempt;
    }
}
