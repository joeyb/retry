package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class ResultAcceptTests {

    @Test
    public void nullResultIsAccepted() {
        AnyAccept<Long> accept = new AnyAccept<>();

        assertThat(accept.accept(null)).isTrue();
    }

    @Test
    public void nonNullResultIsAccepted() {
        AnyAccept<Long> accept = new AnyAccept<>();

        assertThat(accept.accept(ThreadLocalRandom.current().nextLong())).isTrue();
    }
}
