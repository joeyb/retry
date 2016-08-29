package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.joeyb.retry.TestHelpers.assertClassOnlyHasPrivateConstructor;

import org.assertj.core.util.Lists;
import org.junit.Test;

public class AcceptsTests {

    @Test
    public void acceptsOnlyHasPrivateConstructor() {
        assertClassOnlyHasPrivateConstructor(Accepts.class);
    }

    @Test
    public void andAcceptVararg0() {
        Accept<Long> accept = Accepts.and();

        assertThat(accept).isInstanceOf(AnyAccept.class);
    }

    @Test
    public void andAcceptCollection0() {
        Accept<Long> accept = Accepts.and(Lists.newArrayList());

        assertThat(accept).isInstanceOf(AnyAccept.class);
    }

    @Test
    public void andAcceptVararg1() {
        Accept<Long> falseAccept = a -> false;

        Accept<Long> accept = Accepts.and(falseAccept);

        assertThat(accept).isEqualTo(falseAccept);
    }

    @Test
    public void andAcceptCollection1() {
        Accept<Long> falseAccept = a -> false;

        Accept<Long> accept = Accepts.and(Lists.newArrayList(falseAccept));

        assertThat(accept).isEqualTo(falseAccept);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void andAcceptVararg2() {
        Accept<Long> falseAccept = a -> false;
        Accept<Long> trueAccept = a -> true;

        Accept<Long> accept = Accepts.and(falseAccept, trueAccept);

        assertThat(accept).isInstanceOf(CompositeAndAccept.class);

        CompositeAndAccept<Long> andAccept = (CompositeAndAccept<Long>) accept;

        assertThat(andAccept.accepts()).containsExactlyInAnyOrder(falseAccept, trueAccept);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void andAcceptCollection2() {
        Accept<Long> falseAccept = a -> false;
        Accept<Long> trueAccept = a -> true;

        Accept<Long> accept = Accepts.and(Lists.newArrayList(falseAccept, trueAccept));

        assertThat(accept).isInstanceOf(CompositeAndAccept.class);

        CompositeAndAccept<Long> andAccept = (CompositeAndAccept<Long>) accept;

        assertThat(andAccept.accepts()).containsExactlyInAnyOrder(falseAccept, trueAccept);
    }

    @Test
    public void anyAccept() {
        Accept<Long> accept = Accepts.any();

        assertThat(accept).isInstanceOf(AnyAccept.class);
    }

    @Test
    public void nonNullAccept() {
        Accept<Long> accept = Accepts.nonNull();

        assertThat(accept).isInstanceOf(NonNullAccept.class);
    }

    @Test
    public void orAcceptVararg0() {
        Accept<Long> accept = Accepts.or();

        assertThat(accept).isInstanceOf(AnyAccept.class);
    }

    @Test
    public void orAcceptCollection0() {
        Accept<Long> accept = Accepts.or(Lists.newArrayList());

        assertThat(accept).isInstanceOf(AnyAccept.class);
    }

    @Test
    public void orAcceptVararg1() {
        Accept<Long> falseAccept = a -> false;

        Accept<Long> accept = Accepts.or(falseAccept);

        assertThat(accept).isEqualTo(falseAccept);
    }

    @Test
    public void orAcceptCollection1() {
        Accept<Long> falseAccept = a -> false;

        Accept<Long> accept = Accepts.or(Lists.newArrayList(falseAccept));

        assertThat(accept).isEqualTo(falseAccept);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void orAcceptVararg2() {
        Accept<Long> falseAccept = a -> false;
        Accept<Long> trueAccept = a -> true;

        Accept<Long> accept = Accepts.or(falseAccept, trueAccept);

        assertThat(accept).isInstanceOf(CompositeOrAccept.class);

        CompositeOrAccept<Long> orAccept = (CompositeOrAccept<Long>) accept;

        assertThat(orAccept.accepts()).containsExactlyInAnyOrder(falseAccept, trueAccept);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void orAcceptCollection2() {
        Accept<Long> falseAccept = a -> false;
        Accept<Long> trueAccept = a -> true;

        Accept<Long> accept = Accepts.or(Lists.newArrayList(falseAccept, trueAccept));

        assertThat(accept).isInstanceOf(CompositeOrAccept.class);

        CompositeOrAccept<Long> orAccept = (CompositeOrAccept<Long>) accept;

        assertThat(orAccept.accepts()).containsExactlyInAnyOrder(falseAccept, trueAccept);
    }
}
