package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class NeverStopTests {

    @Test
    public void neverStop() {
        Attempt<Long> attempt = Attempts.exception(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                ThreadLocalRandom.current().nextLong(Long.MAX_VALUE),
                new RuntimeException());

        Stop<Long> stop = new NeverStop<>();

        assertThat(stop.stop(attempt)).isFalse();
    }
}
