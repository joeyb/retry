package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class NonNullResultAcceptTests {

    @Test
    public void nullResultIsNotAccepted() {
        NonNullAccept<Long> accept = new NonNullAccept<>();

        assertThat(accept.accept(null)).isFalse();
    }

    @Test
    public void nonNullResultIsAccepted() {
        NonNullAccept<Long> accept = new NonNullAccept<>();

        assertThat(accept.accept(ThreadLocalRandom.current().nextLong())).isTrue();
    }
}
