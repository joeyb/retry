package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class NoWaitTests {

    @Test
    public void noWait() {
        Attempt<Long> attempt = Attempts.exception(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                ThreadLocalRandom.current().nextLong(Long.MAX_VALUE),
                new RuntimeException());

        Wait<Long> noWait = new NoWait<>();

        assertThat(noWait.waitTime(attempt)).isEqualTo(0);
    }
}
