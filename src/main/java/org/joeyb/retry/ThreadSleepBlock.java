package org.joeyb.retry;

import javax.annotation.concurrent.Immutable;

/**
 * {@code ThreadSleepBlock} is an implementation of {@link Block} that uses {@link Thread#sleep(long)} to perform the
 * block.
 */
@Immutable
class ThreadSleepBlock implements Block {

    /**
     * {@inheritDoc}
     */
    @Override
    public void block(long waitTime) throws InterruptedException {
        Thread.sleep(waitTime);
    }
}
