/*
 * Copyright 2002-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flcit.commons.core.util;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.flcit.commons.core.executor.SingleTaskThreadExecutor;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class AsyncUtils {

    private static final Logger LOG = Logger.getLogger(AsyncUtils.class.getName());

    private AsyncUtils() { }

    /**
     * @param <T>
     * @param list
     * @param executor
     * @param consumer
     * @return
     */
    public static <T> Supplier<Boolean> consume(T[] list, Executor executor, Consumer<T> consumer) {
        final int[] length = new int[] { list.length, 0};
        for (T elem : list) {
            executor.execute(() -> {
                consumer.accept(elem);
                synchronized (length) {
                    length[1] = ++length[1];
                }
            });
        }
        return () -> length[0] == length[1];
    }

    /**
     * @param <T>
     * @param supplier
     * @param consumer
     */
    public static <T> void getSyncErrorAsync(Supplier<T> supplier, Consumer<T> consumer) {
        getSyncErrorAsync(supplier, 30000L, consumer);
    }

    /**
     * @param <T>
     * @param supplier
     * @param sleepOnError
     * @param consumer
     */
    public static <T> void getSyncErrorAsync(Supplier<T> supplier, Long sleepOnError, Consumer<T> consumer) {
        getSyncErrorAsync(AsyncUtils::getSingleTaskThreadExecutor, supplier, sleepOnError, consumer, AsyncUtils::logError);
    }

    private static Executor getSingleTaskThreadExecutor() {
        return new SingleTaskThreadExecutor();
    }

    private static void logError(Exception e) {
        LOG.log(Level.SEVERE, "AsyncUtils", e);
    }

    /**
     * @param <T>
     * @param executor
     * @param supplier
     * @param sleepOnError
     * @param consumer
     * @param error
     */
    public static <T> void getSyncErrorAsync(Supplier<Executor> executor, Supplier<T> supplier, Long sleepOnError, Consumer<T> consumer, Consumer<RuntimeException> error) {
       try {
           consumer.accept(supplier.get());
       } catch (RuntimeException e) {
           error.accept(e);
           executor.get().execute(() -> getSync(supplier, sleepOnError, consumer, error));
       }
    }

    private static <T> void getSync(final Supplier<T> supplier, final Long sleepOnError, final Consumer<T> consumer, final Consumer<RuntimeException> error) {
        T value = null;
        do {
           try {
               value = supplier.get();
           } catch (RuntimeException e) {
               error.accept(e);
               if (sleepOnError != null) {
                   ThreadUtils.sleep(sleepOnError);
               }
           }
        } while (value == null);
        consumer.accept(value);
    }

    /**
     * @param runnable
     * @param executor
     * @param firstAsync
     * @param firstDelay
     * @param count
     * @param delay
     * @param retry
     */
    public static void doAsyncRetryable(final Runnable runnable, final ScheduledExecutorService executor, final boolean firstAsync, final long firstDelay, final int count, final long delay, final Predicate<RuntimeException> retry) {
        if (firstAsync) {
            doAsync(() -> doAsyncRetryable(runnable, executor, count, delay, retry), executor, firstDelay);
        } else {
            doAsyncRetryable(runnable, executor, count, delay, retry);
        }
    }

    /**
     * @param runnable
     * @param executor
     * @param delay
     * @param retry
     */
    public static void doAsyncRetryable(final Runnable runnable, final ScheduledExecutorService executor, final long delay, final Predicate<RuntimeException> retry) {
        doAsyncRetryable(runnable, executor, Integer.MAX_VALUE, delay, retry);
    }

    /**
     * @param runnable
     * @param runnableRetry
     * @param executor
     * @param delay
     * @param retry
     */
    public static void doAsyncRetryable(final Runnable runnable, final Runnable runnableRetry, final ScheduledExecutorService executor, final long delay, final Predicate<RuntimeException> retry) {
        doAsyncRetryable(runnable, runnableRetry, executor, Integer.MAX_VALUE, delay, retry);
    }

    /**
     * @param runnable
     * @param executor
     * @param count
     * @param delay
     * @param retry
     */
    public static void doAsyncRetryable(final Runnable runnable, final ScheduledExecutorService executor, final int count, final long delay, final Predicate<RuntimeException> retry) {
        doAsyncRetryable(runnable, runnable, executor, count, delay, retry);
    }

    /**
     * @param runnable
     * @param runnableRetry
     * @param executor
     * @param count
     * @param delay
     * @param retry
     */
    public static void doAsyncRetryable(final Runnable runnable, final Runnable runnableRetry, final ScheduledExecutorService executor, final int count, final long delay, final Predicate<RuntimeException> retry) {
        doAsyncRetryable(runnable, runnableRetry, executor, count, delay, retry, null, null);
    }

    /**
     * @param runnable
     * @param runnableRetry
     * @param executor
     * @param count
     * @param delay
     * @param retry
     * @param runnableEndRetry
     * @param consumerError
     */
    @SuppressWarnings("java:S107")
    public static void doAsyncRetryable(final Runnable runnable, final Runnable runnableRetry, final ScheduledExecutorService executor, final int count, final long delay, final Predicate<RuntimeException> retry, final Runnable runnableEndRetry, final Consumer<RuntimeException> consumerError) {
        try {
            runnable.run();
        } catch (RuntimeException e) {
            if (retry != null && retry.test(e)) {
                if (count > 0) {
                    doAsync(() -> doAsyncRetryable(ObjectUtils.getOrDefault(runnableRetry, runnable), executor, count - 1, delay, retry), executor, delay);
                } else if (runnableEndRetry != null) {
                    runnableEndRetry.run();
                }
            } else if (consumerError != null) {
                consumerError.accept(e);
            } else {
                throw e;
            }
        }
    }

    /**
     * @param runnable
     * @param executor
     * @param delay
     */
    public static void doAsync(final Runnable runnable, final ScheduledExecutorService executor, final long delay) {
        if (delay > 0) {
            executor.schedule(runnable, delay, TimeUnit.MILLISECONDS);
        } else {
            executor.execute(runnable);
        }
    }

}
