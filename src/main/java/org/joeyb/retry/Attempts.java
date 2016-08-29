package org.joeyb.retry;

import javax.annotation.concurrent.Immutable;

class Attempts {

    static <V> Attempt<V> exception(long attemptNumber, long delaySinceFirstAttempt, Throwable exception) {
        return new ExceptionAttempt<>(attemptNumber, delaySinceFirstAttempt, exception);
    }

    static <V> Attempt<V> result(long attemptNumber, long delaySinceFirstAttempt, V result) {
        return new ResultAttempt<>(attemptNumber, delaySinceFirstAttempt, result);
    }

    @Immutable
    private static class ExceptionAttempt<V> implements Attempt<V> {

        private final long attemptNumber;
        private final long delaySinceFirstAttempt;
        private final Throwable exception;

        ExceptionAttempt(long attemptNumber, long delaySinceFirstAttempt, Throwable exception) {
            this.attemptNumber = attemptNumber;
            this.delaySinceFirstAttempt = delaySinceFirstAttempt;
            this.exception = exception;
        }

        @Override
        public long attemptNumber() {
            return attemptNumber;
        }

        @Override
        public long delaySinceFirstAttempt() {
            return delaySinceFirstAttempt;
        }

        @Override
        public Throwable exception() {
            return exception;
        }

        @Override
        public boolean hasException() {
            return true;
        }

        @Override
        public boolean hasResult() {
            return false;
        }

        @Override
        public V result() {
            throw new IllegalStateException("The attempt failed, so there is no result");
        }
    }

    @Immutable
    private static class ResultAttempt<V> implements Attempt<V> {

        private final long attemptNumber;
        private final long delaySinceFirstAttempt;
        private final V result;

        ResultAttempt(long attemptNumber, long delaySinceFirstAttempt, V result) {
            this.attemptNumber = attemptNumber;
            this.delaySinceFirstAttempt = delaySinceFirstAttempt;
            this.result = result;
        }

        @Override
        public long attemptNumber() {
            return attemptNumber;
        }

        @Override
        public long delaySinceFirstAttempt() {
            return delaySinceFirstAttempt;
        }

        @Override
        public Throwable exception() {
            throw new IllegalStateException("The attempt succeeded, so there is no exception");
        }

        @Override
        public boolean hasException() {
            return false;
        }

        @Override
        public boolean hasResult() {
            return true;
        }

        @Override
        public V result() {
            return result;
        }
    }

    private Attempts() { }
}
