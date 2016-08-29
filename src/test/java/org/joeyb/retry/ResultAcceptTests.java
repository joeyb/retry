package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class ResultAcceptTests {

    @Test
    public void nullResultIsAccepted() {
        Attempt<Long> attempt = Attempts.result(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                ThreadLocalRandom.current().nextLong(),
                null);

        ResultAccept<Long> accept = new ResultAccept<>();

        assertThat(accept.accept(attempt)).isTrue();
    }

    @Test
    public void nonNullResultIsAccepted() {
        Attempt<Long> attempt = Attempts.result(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                ThreadLocalRandom.current().nextLong(),
                ThreadLocalRandom.current().nextLong());

        ResultAccept<Long> accept = new ResultAccept<>();

        assertThat(accept.accept(attempt)).isTrue();
    }

    @Test
    public void exceptionIsNotAccepted() {
        Attempt<Long> attempt = Attempts.exception(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                ThreadLocalRandom.current().nextLong(),
                new RuntimeException());

        ResultAccept<Long> accept = new ResultAccept<>();

        assertThat(accept.accept(attempt)).isFalse();
    }
}
