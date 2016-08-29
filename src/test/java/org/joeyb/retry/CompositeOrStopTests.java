package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class CompositeOrStopTests {

    @Test
    public void compositeOrStop() {
        Attempt<Long> attempt = Attempts.exception(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                ThreadLocalRandom.current().nextLong(),
                new RuntimeException());
        Stop<Long> falseStop = a -> false;
        Stop<Long> trueStop = a -> true;

        Stop<Long> bothFalse = new CompositeOrStop<>(Lists.newArrayList(falseStop, falseStop));

        assertThat(bothFalse.stop(attempt)).isFalse();

        Stop<Long> firstTrue = new CompositeOrStop<>(Lists.newArrayList(trueStop, falseStop));

        assertThat(firstTrue.stop(attempt)).isTrue();

        Stop<Long> secondTrue = new CompositeOrStop<>(Lists.newArrayList(falseStop, trueStop));

        assertThat(secondTrue.stop(attempt)).isTrue();

        Stop<Long> bothTrue = new CompositeOrStop<>(Lists.newArrayList(trueStop, trueStop));

        assertThat(bothTrue.stop(attempt)).isTrue();
    }
}
