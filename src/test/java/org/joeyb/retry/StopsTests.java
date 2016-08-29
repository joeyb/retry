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
    public void compositeStopVararg() {
        Attempt<Long> attempt = Attempts.exception(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                ThreadLocalRandom.current().nextLong(),
                new RuntimeException());
        Stop<Long> falseStop = a -> false;
        Stop<Long> trueStop = a -> true;

        Stop<Long> bothFalse = Stops.composite(falseStop, falseStop);

        assertThat(bothFalse.stop(attempt)).isFalse();

        Stop<Long> firstTrue = Stops.composite(trueStop, falseStop);

        assertThat(firstTrue.stop(attempt)).isTrue();

        Stop<Long> secondTrue = Stops.composite(falseStop, trueStop);

        assertThat(secondTrue.stop(attempt)).isTrue();

        Stop<Long> bothTrue = Stops.composite(trueStop, trueStop);

        assertThat(bothTrue.stop(attempt)).isTrue();
    }

    @Test
    public void compositeStopCollection() {
        Attempt<Long> attempt = Attempts.exception(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                ThreadLocalRandom.current().nextLong(),
                new RuntimeException());
        Stop<Long> falseStop = a -> false;
        Stop<Long> trueStop = a -> true;

        Stop<Long> bothFalse = Stops.composite(Lists.newArrayList(falseStop, falseStop));

        assertThat(bothFalse.stop(attempt)).isFalse();

        Stop<Long> firstTrue = Stops.composite(Lists.newArrayList(trueStop, falseStop));

        assertThat(firstTrue.stop(attempt)).isTrue();

        Stop<Long> secondTrue = Stops.composite(Lists.newArrayList(falseStop, trueStop));

        assertThat(secondTrue.stop(attempt)).isTrue();

        Stop<Long> bothTrue = Stops.composite(Lists.newArrayList(trueStop, trueStop));

        assertThat(bothTrue.stop(attempt)).isTrue();
    }

    @Test
    public void maxAttemptsStop() {
        long maxAttempts = ThreadLocalRandom.current().nextLong(10, Long.MAX_VALUE - 1);

        Attempt<Long> attemptBeforeMax = Attempts.exception(
                maxAttempts - 1,
                ThreadLocalRandom.current().nextLong(),
                new RuntimeException());

        Attempt<Long> attemptAtMax = Attempts.exception(
                maxAttempts,
                ThreadLocalRandom.current().nextLong(),
                new RuntimeException());

        Attempt<Long> attemptAfterMax = Attempts.exception(
                maxAttempts + 1,
                ThreadLocalRandom.current().nextLong(),
                new RuntimeException());

        Stop<Long> stop = Stops.maxAttempts(maxAttempts);

        assertThat(stop.stop(attemptBeforeMax)).isFalse();
        assertThat(stop.stop(attemptAtMax)).isTrue();
        assertThat(stop.stop(attemptAfterMax)).isTrue();
    }

    @Test
    public void maxDelayAfterStartStop() {
        long maxDelayAfterStart = ThreadLocalRandom.current().nextLong(10, Long.MAX_VALUE - 1);

        Attempt<Long> attemptBeforeMax = Attempts.exception(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                maxDelayAfterStart - 1,
                new RuntimeException());

        Attempt<Long> attemptAtMax = Attempts.exception(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                maxDelayAfterStart,
                new RuntimeException());

        Attempt<Long> attemptAfterMax = Attempts.exception(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                maxDelayAfterStart + 1,
                new RuntimeException());

        Stop<Long> stop = Stops.maxDelaySinceFirstAttempt(maxDelayAfterStart);

        assertThat(stop.stop(attemptBeforeMax)).isFalse();
        assertThat(stop.stop(attemptAtMax)).isTrue();
        assertThat(stop.stop(attemptAfterMax)).isTrue();
    }

    @Test
    public void neverStop() {
        Attempt<Long> attempt = Attempts.exception(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                ThreadLocalRandom.current().nextLong(),
                new RuntimeException());

        Stop<Long> stop = Stops.never();

        assertThat(stop.stop(attempt)).isFalse();
    }
}
