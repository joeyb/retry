package org.joeyb.retry;

/**
 * {@code Block} defines the interface for the retry's blocking strategy, which dictates the method used for actually
 * executing the wait. The vast majority of implementation will just use the standard {@link Thread#sleep(long)}.
 */
public interface Block {

    /**
     * Block for the given amount of time, in milliseconds.
     *
     * @param waitTime the amount of time to wait, in milliseconds.
     * @throws InterruptedException if the wait was interrupted before completing
     */
    void block(long waitTime) throws InterruptedException;
}
