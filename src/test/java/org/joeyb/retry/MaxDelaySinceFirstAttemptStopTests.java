package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class MaxDelaySinceFirstAttemptStopTests {

    @Test
    public void maxDelaySinceFirstAttemptStop() {
        long maxDelay = ThreadLocalRandom.current().nextLong(10, Long.MAX_VALUE - 1);

        Attempt<Long> attemptBeforeMax = Attempts.exception(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                maxDelay - 1,
                new RuntimeException());

        Attempt<Long> attemptAtMax = Attempts.exception(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                maxDelay,
                new RuntimeException());

        Attempt<Long> attemptAfterMax = Attempts.exception(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                maxDelay + 1,
                new RuntimeException());

        MaxDelaySinceFirstAttemptStop<Long> stop = new MaxDelaySinceFirstAttemptStop<>(maxDelay);

        assertThat(stop.stop(attemptBeforeMax)).isFalse();
        assertThat(stop.stop(attemptAtMax)).isTrue();
        assertThat(stop.stop(attemptAfterMax)).isTrue();
        assertThat(stop.maxDelaySinceFirstAttempt()).isEqualTo(maxDelay);
    }

    @Test
    public void throwsIfMaxDelayIsLessThanZero() {
        long maxDelay = -ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);

        assertThatThrownBy(() -> new MaxDelaySinceFirstAttemptStop<>(maxDelay))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
