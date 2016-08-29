package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.assertj.core.util.Lists;
import org.junit.Test;

public class CompositeAndAcceptTests {

    @Test
    public void compositeAndAccept() {
        Accept<Long> falseAccept = a -> false;
        Accept<Long> trueAccept = a -> true;

        Accept<Long> bothFalse = new CompositeAndAccept<>(Lists.newArrayList(falseAccept, falseAccept));

        assertThat(bothFalse.accept(null)).isFalse();

        Accept<Long> firstTrue = new CompositeAndAccept<>(Lists.newArrayList(trueAccept, falseAccept));

        assertThat(firstTrue.accept(null)).isFalse();

        Accept<Long> secondTrue = new CompositeAndAccept<>(Lists.newArrayList(falseAccept, trueAccept));

        assertThat(secondTrue.accept(null)).isFalse();

        Accept<Long> bothTrue = new CompositeAndAccept<>(Lists.newArrayList(trueAccept, trueAccept));

        assertThat(bothTrue.accept(null)).isTrue();
    }
}
