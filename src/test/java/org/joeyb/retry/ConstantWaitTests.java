package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class ConstantWaitTests {

    @Test
    public void constantWait() {
        long waitTime = ThreadLocalRandom.current().nextLong();

        Attempt<Long> attempt = Attempts.exception(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                ThreadLocalRandom.current().nextLong(),
                new RuntimeException());

        Wait<Long> constantWait = new ConstantWait<>(waitTime);

        assertThat(constantWait.waitTime(attempt)).isEqualTo(waitTime);
    }
}
