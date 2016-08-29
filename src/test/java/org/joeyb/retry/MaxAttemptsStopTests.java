package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class MaxAttemptsStopTests {

    @Test
    public void maxAttemptsStop() {
        long maxAttempts = ThreadLocalRandom.current().nextLong(10, Long.MAX_VALUE - 1);

        Attempt<Long> attemptBeforeMax = Attempts.exception(
                maxAttempts - 1,
                ThreadLocalRandom.current().nextLong(),
                new RuntimeException());

        Attempt<Long> attemptAtMax = Attempts.exception(
                maxAttempts,
                ThreadLocalRandom.current().nextLong(),
                new RuntimeException());

        Attempt<Long> attemptAfterMax = Attempts.exception(
                maxAttempts + 1,
                ThreadLocalRandom.current().nextLong(),
                new RuntimeException());

        MaxAttemptsStop<Long> stop = new MaxAttemptsStop<>(maxAttempts);

        assertThat(stop.stop(attemptBeforeMax)).isFalse();
        assertThat(stop.stop(attemptAtMax)).isTrue();
        assertThat(stop.stop(attemptAfterMax)).isTrue();
        assertThat(stop.maxAttempts()).isEqualTo(maxAttempts);
    }
}
