package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.joeyb.retry.TestHelpers.assertClassOnlyHasPrivateConstructor;

import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class WaitsTests {

    @Test
    public void waitsOnlyHasPrivateConstructor() {
        assertClassOnlyHasPrivateConstructor(Waits.class);
    }

    @Test
    public void noWait() {
        Attempt<Long> attempt = Attempts.exception(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                ThreadLocalRandom.current().nextLong(),
                new RuntimeException());

        Wait<Long> noWait = Waits.noWait();

        assertThat(noWait.waitTime(attempt)).isEqualTo(0);
    }
}
