package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.joeyb.retry.TestHelpers.assertClassOnlyHasPrivateConstructor;

import org.junit.Test;

public class WaitsTests {

    @Test
    public void waitsOnlyHasPrivateConstructor() {
        assertClassOnlyHasPrivateConstructor(Waits.class);
    }

    @Test
    public void noWait() {
        Wait<Long> wait = Waits.noWait();

        assertThat(wait).isInstanceOf(NoWait.class);
    }
}
