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
    public void constant() {
        long waitTime = ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);

        Wait<Long> wait = Waits.constant(waitTime);

        assertThat(wait).isInstanceOf(ConstantWait.class);

        ConstantWait<Long> constantWait = (ConstantWait<Long>) wait;

        assertThat(constantWait.waitTime()).isEqualTo(waitTime);
    }

    @Test
    public void noWait() {
        Wait<Long> wait = Waits.noWait();

        assertThat(wait).isInstanceOf(NoWait.class);
    }
}
