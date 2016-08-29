package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class ThreadSleepBlockTests {

    @Test
    public void waitsAtLeastTheExpectedAmountOfTime() throws InterruptedException {
        Block block = new ThreadSleepBlock();

        Long waitTime = ThreadLocalRandom.current().nextLong(500, 2000);

        Long startTime = System.nanoTime();

        block.block(waitTime);

        Long endTime = System.nanoTime();

        assertThat(TimeUnit.NANOSECONDS.toMillis(endTime - startTime)).isGreaterThanOrEqualTo(waitTime);
    }
}
