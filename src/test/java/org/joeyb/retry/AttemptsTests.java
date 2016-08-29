package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.joeyb.retry.TestHelpers.assertClassOnlyHasPrivateConstructor;

import org.junit.Test;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class AttemptsTests {

    @Test
    public void attemptsOnlyHasPrivateConstructor() {
        assertClassOnlyHasPrivateConstructor(Attempts.class);
    }

    @Test
    public void exceptionAttempt() {
        long attemptNumber = ThreadLocalRandom.current().nextLong(1, 100);
        long delaySinceFirstAttempt = ThreadLocalRandom.current().nextLong(1000, 10000);
        Throwable exception = new RuntimeException(UUID.randomUUID().toString());

        Attempt<?> attempt = Attempts.exception(attemptNumber, delaySinceFirstAttempt, exception);

        assertThat(attempt).isNotNull();
        assertThat(attempt.attemptNumber()).isEqualTo(attemptNumber);
        assertThat(attempt.delaySinceFirstAttempt()).isEqualTo(delaySinceFirstAttempt);
        assertThat(attempt.exception()).isInstanceOf(RuntimeException.class).hasMessage(exception.getMessage());
        assertThatThrownBy(attempt::get).isInstanceOf(RuntimeException.class).hasMessage(exception.getMessage());
        assertThat(attempt.hasException()).isTrue();
        assertThat(attempt.hasResult()).isFalse();
        assertThatThrownBy(attempt::result).isInstanceOf(IllegalStateException.class);
    }

    @Test
    public void resultAttempt() throws Throwable {
        long attemptNumber = ThreadLocalRandom.current().nextLong(1, 100);
        long delaySinceFirstAttempt = ThreadLocalRandom.current().nextLong(1000, 10000);
        Long result = ThreadLocalRandom.current().nextLong();

        Attempt<?> attempt = Attempts.result(attemptNumber, delaySinceFirstAttempt, result);

        assertThat(attempt).isNotNull();
        assertThat(attempt.attemptNumber()).isEqualTo(attemptNumber);
        assertThat(attempt.delaySinceFirstAttempt()).isEqualTo(delaySinceFirstAttempt);
        assertThatThrownBy(attempt::exception).isInstanceOf(IllegalStateException.class);
        assertThat(attempt.get()).isEqualTo(result);
        assertThat(attempt.hasException()).isFalse();
        assertThat(attempt.hasResult()).isTrue();
        assertThat(attempt.result()).isEqualTo(result);
    }
}
