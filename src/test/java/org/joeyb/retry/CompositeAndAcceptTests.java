package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.assertj.core.util.Lists;
import org.junit.Test;

import java.util.concurrent.ThreadLocalRandom;

public class CompositeAndAcceptTests {

    @Test
    public void compositeAndAccept() {
        Attempt<Long> attempt = Attempts.exception(
                ThreadLocalRandom.current().nextLong(1, Long.MAX_VALUE),
                ThreadLocalRandom.current().nextLong(),
                new RuntimeException());
        Accept<Long> falseAccept = a -> false;
        Accept<Long> trueAccept = a -> true;

        Accept<Long> bothFalse = new CompositeAndAccept<>(Lists.newArrayList(falseAccept, falseAccept));

        assertThat(bothFalse.accept(attempt)).isFalse();

        Accept<Long> firstTrue = new CompositeAndAccept<>(Lists.newArrayList(trueAccept, falseAccept));

        assertThat(firstTrue.accept(attempt)).isFalse();

        Accept<Long> secondTrue = new CompositeAndAccept<>(Lists.newArrayList(falseAccept, trueAccept));

        assertThat(secondTrue.accept(attempt)).isFalse();

        Accept<Long> bothTrue = new CompositeAndAccept<>(Lists.newArrayList(trueAccept, trueAccept));

        assertThat(bothTrue.accept(attempt)).isTrue();
    }
}
