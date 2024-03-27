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

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class FunctionUtils {

    private FunctionUtils() { }

    /**
     * @param <T>
     * @param supplier
     * @return
     */
    public static <T> T resolve(Supplier<T> supplier) {
        return supplier != null ? supplier.get() : null;
    }

    /**
     * @param <T>
     * @param supplier
     * @param supplierNoSuchMethod
     * @return
     */
    public static <T> T safeNoSuchMethod(Supplier<T> supplier, Supplier<T> supplierNoSuchMethod) {
        try {
            return supplier.get();
        } catch (NoSuchMethodError e) {
            return supplierNoSuchMethod.get();
        }
    }

    /**
     * @param <T>
     * @param supplier
     * @return
     */
    public static <T> T safeCall(Supplier<T> supplier) {
        return safeCall(supplier, null, 30000L);
    }

    /**
     * @param <T>
     * @param supplier
     * @param maxRetry
     * @param delayRetry
     * @param exceptions
     * @return
     */
    public static <T> T safeCall(Supplier<T> supplier, Integer maxRetry, Long delayRetry, Class<?>... exceptions) {
        try {
            return supplier.get();
        } catch (Exception e) {
            if ((exceptions != null && !ArrayUtils.contains(exceptions, e.getClass())) || maxRetry != null && maxRetry <= 0) {
                throw e;
            }
            if (delayRetry != null && delayRetry > 0) {
                try {
                    Thread.sleep(delayRetry);
                } catch (InterruptedException e1) {
                    Thread.currentThread().interrupt();
                }
            }
            return safeCall(supplier, maxRetry != null ? --maxRetry : null, delayRetry, exceptions);
        }
    }

    /**
     * @param <T>
     * @param size
     * @param supplier
     * @param consumer
     */
    public static <T> void list(int size, IntFunction<T[]> supplier, Consumer<T[]> consumer) {
        list(size, supplier, t -> {
            consumer.accept(null);
            return null;
        });
    }

    /**
     * @param <T>
     * @param size
     * @param supplier
     * @param consumer
     */
    public static <T> void list(int size, IntFunction<T[]> supplier, Function<T[], Supplier<Boolean>> consumer) {
        T[] cases;
        Supplier<Boolean> end = null;
        do {
            while (end != null && BooleanUtils.isFalse(end.get())) {
                try {
                    Thread.sleep(15000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            cases = supplier.apply(size);
            if (!ArrayUtils.isEmpty(cases)) {
                end = consumer.apply(cases);
            }
        } while (!ArrayUtils.isEmpty(cases) && (size == -1 || cases.length == size));
    }

    /**
     * @param <T>
     * @param supplier
     * @param consumer
     */
    public static <T> void consumeIfNotNull(Supplier<T> supplier, Consumer<T> consumer) {
        final T value = supplier.get();
        if (value != null) {
            consumer.accept(value);
        }
    }

    /**
     * @param <T>
     * @param <R>
     * @param value
     * @param converter
     * @return
     */
    public static <T, R> R convertIfNotNull(T value, Function<T, R> converter) {
        return value == null || converter == null ? null : converter.apply(value);
    }

    /**
     * @param supplier
     * @param runnable
     * @param defaultRunnable
     */
    public static void ifTrue(BooleanSupplier supplier, Runnable runnable, Runnable defaultRunnable) {
        ifTrue(supplier.getAsBoolean(), runnable, defaultRunnable);
    }

    /**
     * @param value
     * @param runnable
     * @param defaultRunnable
     */
    public static void ifTrue(Boolean value, Runnable runnable, Runnable defaultRunnable) {
        if (BooleanUtils.isTrue(value)) {
            runnable.run();
        } else if (defaultRunnable != null) {
            defaultRunnable.run();
        }
    }

    /**
     * @param <T>
     * @param value
     * @param supplier
     * @param defaultSupplier
     * @return
     */
    public static <T> T ifTrue(BooleanSupplier value, Supplier<T> supplier, Supplier<T> defaultSupplier) {
        return ifTrue(value.getAsBoolean(), supplier, defaultSupplier);
    }

    /**
     * @param <T>
     * @param value
     * @param supplier
     * @param defaultSupplier
     * @return
     */
    public static <T> T ifTrue(Boolean value, Supplier<T> supplier, Supplier<T> defaultSupplier) {
        if (BooleanUtils.isTrue(value)) {
            return supplier.get();
        }
        return defaultSupplier != null ? defaultSupplier.get() : null;
    }

}
