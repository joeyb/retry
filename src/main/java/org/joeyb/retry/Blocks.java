package org.joeyb.retry;

import javax.annotation.concurrent.Immutable;

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

    @Immutable
    private static class ThreadSleepBlock implements Block {

        @Override
        public void block(long waitTime) throws InterruptedException {
            Thread.sleep(waitTime);
        }
    }

    private Blocks() { }
}
