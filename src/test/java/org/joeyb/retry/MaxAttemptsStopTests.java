package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class MaxAttemptsStopTests {

    @Test
    public void maxAttemptsStop() {
        long maxAttempts = ThreadLocalRandom.current().nextLong(10, Long.MAX_VALUE - 1);

        Attempt<Long> attemptBeforeMax = Attempts.exception(
                maxAttempts - 1,
                ThreadLocalRandom.current().nextLong(Long.MAX_VALUE),
                new RuntimeException());

        Attempt<Long> attemptAtMax = Attempts.exception(
                maxAttempts,
                ThreadLocalRandom.current().nextLong(Long.MAX_VALUE),
                new RuntimeException());

        Attempt<Long> attemptAfterMax = Attempts.exception(
                maxAttempts + 1,
                ThreadLocalRandom.current().nextLong(Long.MAX_VALUE),
                new RuntimeException());

        MaxAttemptsStop<Long> stop = new MaxAttemptsStop<>(maxAttempts);

        assertThat(stop.stop(attemptBeforeMax)).isFalse();
        assertThat(stop.stop(attemptAtMax)).isTrue();
        assertThat(stop.stop(attemptAfterMax)).isTrue();
        assertThat(stop.maxAttempts()).isEqualTo(maxAttempts);
    }

    @Test
    public void throwsIfMaxAttemptsIsLessThanOne() {
        long maxAttempts = -ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE);

        assertThatThrownBy(() -> new MaxAttemptsStop<>(maxAttempts))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> new MaxAttemptsStop<>(0))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
