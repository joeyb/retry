retry
=====

`retry` is a small, dependency-free Java library for configuring retry strategies. It is heavily inspired by [`guava-retrying`](https://github.com/rholder/guava-retrying), without its dependency on Guava.

## Setup

`retry` is available via Maven Central. To include it in a Gradle project, add the following dependency:

```groovy
compile "org.joeyb.retry:retry:$retryVersion"
```

## Usage

A `Retry<V>` instance is configured with a particular retry strategy, and can be used by passing a `Callable<V>` to its `call` method:

```java
Retry<V> retry = ...

V result = retry.call(() -> service.performUnreliableRequest());
```

The retry strategy is dictated by 4 basic components:

### Accept

The acceptance strategy (defined by the [`Accept`](src/main/java/org/joeyb/retry/Accept.java) interface) is used to specify whether or not the result from an attempted execution of the underlying `Callable<V>` should be accepted. For example, `Accepts.any()` returns an `Accept` instance that accepts any value.

### Block

The blocking strategy (defined by the [`Block`](src/main/java/org/joeyb/retry/Block.java) interface) is used to specify how the retryer should block its thread while waiting for the next attempt. The default is a `Thread.sleep()`-based implementation, which should be sufficient for most use-cases.

### Stop

The stopping strategy (defined by the [`Stop`](src/main/java/org/joeyb/retry/Stop.java) interface) is used to specify when the retryer should give up. The [`Stops`](src/main/java/org/joeyb/retry/Stops.java) class provides some common implementations, including stopping based on a maximum number of attempts or a maximum time since start.

### Wait

The wait strategy (defined by the [`Wait`](src/main/java/org/joeyb/retry/Wait.java) interface) is used to specify how long the retryer should wait between each attempt.

## `Retry<V>` Builder

The `Retry<V>` class provides a fluent builder for constructing retry strategies. For example, you can build a simple retry strategy that retries indefinitely, with no waiting between attempts, until it gets back a non-null value like this:

```java
Retry<Long> retry = Retry.<Long>newBuilder()
  .acceptNonNullResult()
  .build()
```
