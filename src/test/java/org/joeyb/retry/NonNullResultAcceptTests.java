package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class NonNullResultAcceptTests {

    @Test
    public void nullResultIsNotAccepted() {
        Attempt<Long> attempt = Attempts.result(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                ThreadLocalRandom.current().nextLong(),
                null);

        NonNullResultAccept<Long> accept = new NonNullResultAccept<>();

        assertThat(accept.accept(attempt)).isFalse();
    }

    @Test
    public void nonNullResultIsAccepted() {
        Attempt<Long> attempt = Attempts.result(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                ThreadLocalRandom.current().nextLong(),
                ThreadLocalRandom.current().nextLong());

        NonNullResultAccept<Long> accept = new NonNullResultAccept<>();

        assertThat(accept.accept(attempt)).isTrue();
    }

    @Test
    public void exceptionIsNotAccepted() {
        Attempt<Long> attempt = Attempts.exception(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                ThreadLocalRandom.current().nextLong(),
                new RuntimeException());

        NonNullResultAccept<Long> accept = new NonNullResultAccept<>();

        assertThat(accept.accept(attempt)).isFalse();
    }
}
