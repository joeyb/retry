package org.joeyb.retry;

/**
 * {@code Blocks} provides static helper methods for working with implementations of {@link Block}.
 */
public class Blocks {

    private static final Block THREAD_SLEEP = new ThreadSleepBlock();

    /**
     * Returns an implementation of {@link Block} that uses {@link Thread#sleep(long)} to perform the block.
     */
    public static Block threadSleep() {
        return THREAD_SLEEP;
    }

    private Blocks() { }
}
