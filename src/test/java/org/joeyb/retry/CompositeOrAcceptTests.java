package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;

import org.assertj.core.util.Lists;
import org.junit.Test;

public class CompositeOrAcceptTests {

    @Test
    public void compositeOrAccept() {
        Accept<Long> falseAccept = a -> false;
        Accept<Long> trueAccept = a -> true;

        Accept<Long> bothFalse = new CompositeOrAccept<>(Lists.newArrayList(falseAccept, falseAccept));

        assertThat(bothFalse.accept(null)).isFalse();

        Accept<Long> firstTrue = new CompositeOrAccept<>(Lists.newArrayList(trueAccept, falseAccept));

        assertThat(firstTrue.accept(null)).isTrue();

        Accept<Long> secondTrue = new CompositeOrAccept<>(Lists.newArrayList(falseAccept, trueAccept));

        assertThat(secondTrue.accept(null)).isTrue();

        Accept<Long> bothTrue = new CompositeOrAccept<>(Lists.newArrayList(trueAccept, trueAccept));

        assertThat(bothTrue.accept(null)).isTrue();
    }
}
