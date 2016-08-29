package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.joeyb.retry.TestHelpers.assertClassOnlyHasPrivateConstructor;

import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class StopsTests {

    @Test
    public void stopsOnlyHasPrivateConstructor() {
        assertClassOnlyHasPrivateConstructor(Stops.class);
    }

    @Test
    public void andStopVararg0() {
        Stop<Long> stop = Stops.and();

        assertThat(stop).isInstanceOf(NeverStop.class);
    }

    @Test
    public void andStopCollection0() {
        Stop<Long> stop = Stops.and(Lists.newArrayList());

        assertThat(stop).isInstanceOf(NeverStop.class);
    }

    @Test
    public void andStopVararg1() {
        Stop<Long> falseStop = a -> false;

        Stop<Long> stop = Stops.and(falseStop);

        assertThat(stop).isEqualTo(falseStop);
    }

    @Test
    public void andStopCollection1() {
        Stop<Long> falseStop = a -> false;

        Stop<Long> stop = Stops.and(Lists.newArrayList(falseStop));

        assertThat(stop).isEqualTo(falseStop);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void andStopVararg2() {
        Stop<Long> falseStop = a -> false;
        Stop<Long> trueStop = a -> true;

        Stop<Long> stop = Stops.and(falseStop, trueStop);

        assertThat(stop).isInstanceOf(CompositeAndStop.class);

        CompositeAndStop<Long> andStop = (CompositeAndStop<Long>) stop;

        assertThat(andStop.stops()).containsExactlyInAnyOrder(falseStop, trueStop);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void andStopCollection2() {
        Stop<Long> falseStop = a -> false;
        Stop<Long> trueStop = a -> true;

        Stop<Long> stop = Stops.and(Lists.newArrayList(falseStop, trueStop));

        assertThat(stop).isInstanceOf(CompositeAndStop.class);

        CompositeAndStop<Long> andStop = (CompositeAndStop<Long>) stop;

        assertThat(andStop.stops()).containsExactlyInAnyOrder(falseStop, trueStop);
    }

    @Test
    public void maxAttemptsStop() {
        long maxAttempts = ThreadLocalRandom.current().nextLong(10, Long.MAX_VALUE - 1);

        Stop<Long> stop = Stops.maxAttempts(maxAttempts);

        assertThat(stop).isInstanceOf(MaxAttemptsStop.class);

        MaxAttemptsStop<Long> maxAttemptsStop = (MaxAttemptsStop<Long>) stop;

        assertThat(maxAttemptsStop.maxAttempts()).isEqualTo(maxAttempts);
    }

    @Test
    public void maxDelayAfterStartStop() {
        long maxDelay = ThreadLocalRandom.current().nextLong(10, Long.MAX_VALUE - 1);

        Stop<Long> stop = Stops.maxDelaySinceFirstAttempt(maxDelay);

        assertThat(stop).isInstanceOf(MaxDelaySinceFirstAttemptStop.class);

        MaxDelaySinceFirstAttemptStop<Long> maxDelayStop = (MaxDelaySinceFirstAttemptStop<Long>) stop;

        assertThat(maxDelayStop.maxDelaySinceFirstAttempt()).isEqualTo(maxDelay);
    }

    @Test
    public void neverStop() {
        Stop<Long> stop = Stops.never();

        assertThat(stop).isInstanceOf(NeverStop.class);
    }

    @Test
    public void orStopVararg0() {
        Stop<Long> stop = Stops.or();

        assertThat(stop).isInstanceOf(NeverStop.class);
    }

    @Test
    public void orStopCollection0() {
        Stop<Long> stop = Stops.or(Lists.newArrayList());

        assertThat(stop).isInstanceOf(NeverStop.class);
    }

    @Test
    public void orStopVararg1() {
        Stop<Long> falseStop = a -> false;

        Stop<Long> stop = Stops.or(falseStop);

        assertThat(stop).isEqualTo(falseStop);
    }

    @Test
    public void orStopCollection1() {
        Stop<Long> falseStop = a -> false;

        Stop<Long> stop = Stops.or(Lists.newArrayList(falseStop));

        assertThat(stop).isEqualTo(falseStop);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void orStopVararg2() {
        Stop<Long> falseStop = a -> false;
        Stop<Long> trueStop = a -> true;

        Stop<Long> stop = Stops.or(falseStop, trueStop);

        assertThat(stop).isInstanceOf(CompositeOrStop.class);

        CompositeOrStop<Long> orStop = (CompositeOrStop<Long>) stop;

        assertThat(orStop.stops()).containsExactlyInAnyOrder(falseStop, trueStop);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void orStopCollection2() {
        Stop<Long> falseStop = a -> false;
        Stop<Long> trueStop = a -> true;

        Stop<Long> stop = Stops.or(Lists.newArrayList(falseStop, trueStop));

        assertThat(stop).isInstanceOf(CompositeOrStop.class);

        CompositeOrStop<Long> orStop = (CompositeOrStop<Long>) stop;

        assertThat(orStop.stops()).containsExactlyInAnyOrder(falseStop, trueStop);
    }
}
