package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class ConstantWaitTests {

    @Test
    public void constantWait() {
        long waitTime = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);

        Attempt<Long> attempt = Attempts.exception(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                ThreadLocalRandom.current().nextLong(Long.MAX_VALUE),
                new RuntimeException());

        Wait<Long> constantWait = new ConstantWait<>(waitTime);

        assertThat(constantWait.waitTime(attempt)).isEqualTo(waitTime);
    }

    @Test
    public void throwsIfWaitTimeIsLessThanZero() {
        long waitTime = -ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);

        assertThatThrownBy(() -> new ConstantWait<>(waitTime))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
