package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class CompositeAndStopTests {

    @Test
    public void compositeAndStop() {
        Attempt<Long> attempt = Attempts.exception(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                ThreadLocalRandom.current().nextLong(),
                new RuntimeException());
        Stop<Long> falseStop = a -> false;
        Stop<Long> trueStop = a -> true;

        Stop<Long> bothFalse = new CompositeAndStop<>(Lists.newArrayList(falseStop, falseStop));

        assertThat(bothFalse.stop(attempt)).isFalse();

        Stop<Long> firstTrue = new CompositeAndStop<>(Lists.newArrayList(trueStop, falseStop));

        assertThat(firstTrue.stop(attempt)).isFalse();

        Stop<Long> secondTrue = new CompositeAndStop<>(Lists.newArrayList(falseStop, trueStop));

        assertThat(secondTrue.stop(attempt)).isFalse();

        Stop<Long> bothTrue = new CompositeAndStop<>(Lists.newArrayList(trueStop, trueStop));

        assertThat(bothTrue.stop(attempt)).isTrue();
    }
}
