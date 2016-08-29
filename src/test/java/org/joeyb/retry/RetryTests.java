package org.joeyb.retry;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;

import org.junit.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class RetryTests {

    @Test
    public void eventuallySuccessfulCallableRetriesExpectedNumberOfTimesBeforeSucceeding() {
        int attemptsBeforeSuccess = ThreadLocalRandom.current().nextInt(10, 100);
        long expectedResult = ThreadLocalRandom.current().nextLong();
        long waitTime = ThreadLocalRandom.current().nextLong();

        MemoizingAccept<Long> memoizingAccept = new MemoizingAccept<>(Accepts.any());
        MemoizingBlock memoizingBlock = new MemoizingBlock();
        MemoizingStop<Long> memoizingStop = new MemoizingStop<>(Stops.maxAttempts(attemptsBeforeSuccess + 1));
        MemoizingWait<Long> memoizingWait = new MemoizingWait<>(attempt -> waitTime);

        Retry<Long> retry = new Retry<>(memoizingAccept, memoizingBlock, memoizingStop, memoizingWait);

        Long actualResult = retry.call(new EventuallySuccessfulCallable<>(attemptsBeforeSuccess, expectedResult));

        assertThat(actualResult).isEqualTo(expectedResult);

        assertThat(memoizingAccept.results)
                .hasSize(1)
                .contains(expectedResult);

        assertThat(memoizingBlock.waitTimes)
                .containsOnly(waitTime)
                .hasSize(attemptsBeforeSuccess);

        assertThat(memoizingStop.attempts)
                .hasSize(attemptsBeforeSuccess);

        // The memoizingStop's attempts collection should contain attempts numbered 1 to attemptsBeforeSuccess.
        assertThat(memoizingStop.attempts.stream().map(Attempt::attemptNumber).collect(Collectors.toList()))
                .containsExactlyElementsOf(
                        LongStream.rangeClosed(1, attemptsBeforeSuccess).boxed().collect(Collectors.toList()));

        ensureAttemptDelaysSinceFirstAttemptAreIncreasing(memoizingStop.attempts);

        assertThat(memoizingWait.attempts)
                .hasSize(attemptsBeforeSuccess);

        // The memoizingWait's attempts collection should contain attempts numbered 1 to attemptsBeforeSuccess.
        assertThat(memoizingWait.attempts.stream().map(Attempt::attemptNumber).collect(Collectors.toList()))
                .containsExactlyElementsOf(
                        LongStream.rangeClosed(1, attemptsBeforeSuccess).boxed().collect(Collectors.toList()));

        ensureAttemptDelaysSinceFirstAttemptAreIncreasing(memoizingWait.attempts);
    }

    @Test
    public void callableThatSucceedsButIsNotAcceptedBeforeStopThrowsRetryException() {
        long maxAttempts = ThreadLocalRandom.current().nextInt(10, 100);

        MemoizingAccept<Long> memoizingAccept = new MemoizingAccept<>(r -> r > maxAttempts);
        MemoizingBlock memoizingBlock = new MemoizingBlock();
        MemoizingStop<Long> memoizingStop = new MemoizingStop<>(Stops.maxAttempts(maxAttempts));
        MemoizingWait<Long> memoizingWait = new MemoizingWait<>(Waits.noWait());

        Retry<Long> retry = new Retry<>(memoizingAccept, memoizingBlock, memoizingStop, memoizingWait);

        assertThatThrownBy(() -> retry.call(new IncrementingCallable()))
                .isInstanceOf(RetryException.class)
                .matches(e -> ((RetryException) e).attempt().attemptNumber() == maxAttempts)
                .matches(e -> ((RetryException) e).attempt().hasResult());
    }

    @Test
    public void callableThatDoesNotSucceedBeforeStopThrowsRetryException() {
        int attemptsBeforeSuccess = ThreadLocalRandom.current().nextInt(10, 100);
        long expectedResult = ThreadLocalRandom.current().nextLong();
        long maxAttempts = attemptsBeforeSuccess - 1;
        long waitTime = ThreadLocalRandom.current().nextLong();

        MemoizingAccept<Long> memoizingAccept = new MemoizingAccept<>(Accepts.any());
        MemoizingBlock memoizingBlock = new MemoizingBlock();
        MemoizingStop<Long> memoizingStop = new MemoizingStop<>(Stops.maxAttempts(maxAttempts));
        MemoizingWait<Long> memoizingWait = new MemoizingWait<>(attempt -> waitTime);

        Retry<Long> retry = new Retry<>(memoizingAccept, memoizingBlock, memoizingStop, memoizingWait);

        assertThatThrownBy(() -> retry.call(new EventuallySuccessfulCallable<>(attemptsBeforeSuccess, expectedResult)))
                .isInstanceOf(RetryException.class)
                .matches(e -> ((RetryException) e).attempt().attemptNumber() == maxAttempts)
                .matches(e -> ((RetryException) e).attempt().hasException());
    }

    @Test
    public void builderBuildWithoutAdditionalConfig() {
        Retry<Long> retry = Retry.<Long>newBuilder()
                .build();

        assertThat(retry.getBlock()).isInstanceOf(ThreadSleepBlock.class);
        assertThat(retry.getStop()).isInstanceOf(NeverStop.class);
        assertThat(retry.getWait()).isInstanceOf(NoWait.class);
    }

    @Test
    public void builderAcceptBlockStopAndWaitMethodsSetGivenImplementations() {
        int attemptsBeforeSuccess = ThreadLocalRandom.current().nextInt(10, 100);
        long waitTime = ThreadLocalRandom.current().nextLong();

        MemoizingAccept<Long> memoizingAccept = new MemoizingAccept<>(Accepts.any());
        MemoizingBlock memoizingBlock = new MemoizingBlock();
        MemoizingStop<Long> memoizingStop = new MemoizingStop<>(Stops.maxAttempts(attemptsBeforeSuccess + 1));
        MemoizingWait<Long> memoizingWait = new MemoizingWait<>(attempt -> waitTime);

        Retry<Long> retry = Retry.<Long>newBuilder()
                .accept(memoizingAccept)
                .block(memoizingBlock)
                .stop(memoizingStop)
                .wait(memoizingWait)
                .build();

        assertThat(retry.getAccept()).isEqualTo(memoizingAccept);
        assertThat(retry.getBlock()).isEqualTo(memoizingBlock);
        assertThat(retry.getStop()).isEqualTo(memoizingStop);
        assertThat(retry.getWait()).isEqualTo(memoizingWait);
    }

    @Test
    public void builderAcceptAnyResult() {
        Retry<Long> retry = Retry.<Long>newBuilder()
                .acceptAnyResult()
                .build();

        assertThat(retry.getAccept()).isInstanceOf(AnyAccept.class);
    }

    @Test
    public void builderAcceptNonNullResult() {
        Retry<Long> retry = Retry.<Long>newBuilder()
                .acceptNonNullResult()
                .build();

        assertThat(retry.getAccept()).isInstanceOf(NonNullAccept.class);
    }

    @Test
    public void builderMaxAttempts() {
        long maxAttempts = ThreadLocalRandom.current().nextInt(10, 10000);

        Retry<Long> retry = Retry.<Long>newBuilder()
                .maxAttempts(maxAttempts)
                .build();

        assertThat(retry.getStop()).isInstanceOf(MaxAttemptsStop.class);

        MaxAttemptsStop<Long> maxAttemptsStop = (MaxAttemptsStop<Long>) retry.getStop();

        assertThat(maxAttemptsStop.maxAttempts()).isEqualTo(maxAttempts);
    }

    @Test
    public void builderMaxDelaySinceFirstAttempt() {
        long maxDelay = ThreadLocalRandom.current().nextLong(10, 10000);

        Retry<Long> retry = Retry.<Long>newBuilder()
                .maxDelaySinceFirstAttempt(maxDelay)
                .build();

        assertThat(retry.getStop()).isInstanceOf(MaxDelaySinceFirstAttemptStop.class);

        MaxDelaySinceFirstAttemptStop<Long> maxDelayStop = (MaxDelaySinceFirstAttemptStop<Long>) retry.getStop();

        assertThat(maxDelayStop.maxDelaySinceFirstAttempt()).isEqualTo(maxDelay);
    }

    @Test
    public void builderNeverStop() {
        Retry<Long> retry = Retry.<Long>newBuilder()
                .neverStop()
                .build();

        assertThat(retry.getStop()).isInstanceOf(NeverStop.class);
    }

    private void ensureAttemptDelaysSinceFirstAttemptAreIncreasing(ConcurrentLinkedQueue<Attempt<Long>> attempts) {
        long last = 0;

        for (Attempt<?> attempt : attempts) {
            assertThat(attempt.delaySinceFirstAttempt()).isGreaterThanOrEqualTo(last);

            last = attempt.delaySinceFirstAttempt();
        }
    }

    private static class MemoizingAccept<V> implements Accept<V> {

        private final Accept<V> accept;

        private final ConcurrentLinkedQueue<V> results = new ConcurrentLinkedQueue<>();

        MemoizingAccept(Accept<V> accept) {
            this.accept = accept;
        }

        @Override
        public boolean accept(V result) {
            results.add(result);
            return accept.accept(result);
        }
    }

    private static class MemoizingBlock implements Block {

        private final ConcurrentLinkedQueue<Long> waitTimes = new ConcurrentLinkedQueue<>();

        @Override
        public void block(long waitTime) throws InterruptedException {
            waitTimes.add(waitTime);
        }
    }

    private static class MemoizingStop<V> implements Stop<V> {

        private final ConcurrentLinkedQueue<Attempt<V>> attempts = new ConcurrentLinkedQueue<>();

        private final Stop<V> stop;

        MemoizingStop(Stop<V> stop) {
            this.stop = stop;
        }

        @Override
        public boolean stop(Attempt<V> attempt) {
            attempts.add(attempt);
            return stop.stop(attempt);
        }
    }

    private static class MemoizingWait<V> implements Wait<V> {

        private final ConcurrentLinkedQueue<Attempt<V>> attempts = new ConcurrentLinkedQueue<>();

        private final Wait<V> wait;

        MemoizingWait(Wait<V> wait) {
            this.wait = wait;
        }

        @Override
        public long waitTime(Attempt<V> attempt) {
            attempts.add(attempt);
            return wait.waitTime(attempt);
        }
    }

    private static class EventuallySuccessfulCallable<V> implements Callable<V> {

        private final int attemptsBeforeSuccess;
        private final V result;

        private long attempts;

        EventuallySuccessfulCallable(int attemptsBeforeSuccess, V result) {
            this.attemptsBeforeSuccess = attemptsBeforeSuccess;
            this.result = result;
        }

        @Override
        public V call() throws Exception {
            attempts++;

            if (attempts > attemptsBeforeSuccess) {
                return result;
            }

            throw new RuntimeException();
        }
    }

    private static class IncrementingCallable implements Callable<Long> {

        private long current;

        @Override
        public Long call() throws Exception {
            current++;

            return current;
        }
    }
}
