package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class CompositeOrAcceptTests {

    @Test
    public void compositeOrAccept() {
        Attempt<Long> attempt = Attempts.exception(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                ThreadLocalRandom.current().nextLong(),
                new RuntimeException());
        Accept<Long> falseAccept = a -> false;
        Accept<Long> trueAccept = a -> true;

        Accept<Long> bothFalse = new CompositeOrAccept<>(Lists.newArrayList(falseAccept, falseAccept));

        assertThat(bothFalse.accept(attempt)).isFalse();

        Accept<Long> firstTrue = new CompositeOrAccept<>(Lists.newArrayList(trueAccept, falseAccept));

        assertThat(firstTrue.accept(attempt)).isTrue();

        Accept<Long> secondTrue = new CompositeOrAccept<>(Lists.newArrayList(falseAccept, trueAccept));

        assertThat(secondTrue.accept(attempt)).isTrue();

        Accept<Long> bothTrue = new CompositeOrAccept<>(Lists.newArrayList(trueAccept, trueAccept));

        assertThat(bothTrue.accept(attempt)).isTrue();
    }
}
