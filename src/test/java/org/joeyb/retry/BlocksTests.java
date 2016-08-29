package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.joeyb.retry.TestHelpers.assertClassOnlyHasPrivateConstructor;

import org.junit.Test;

public class BlocksTests {

    @Test
    public void blocksOnlyHasPrivateConstructor() {
        assertClassOnlyHasPrivateConstructor(Blocks.class);
    }

    @Test
    public void threadSleepReturnsThreadSleepBlockInstance() throws InterruptedException {
        Block block = Blocks.threadSleep();

        assertThat(block).isInstanceOf(ThreadSleepBlock.class);
    }
}
