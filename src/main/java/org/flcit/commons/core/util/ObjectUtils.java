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

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class ObjectUtils {

    private ObjectUtils() { }

    /**
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof Optional) {
            return !((Optional<?>) obj).isPresent();
        }
        if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        }
        if (obj instanceof Collection) {
            return ((Collection<?>) obj).isEmpty();
        }
        if (obj instanceof Map) {
            return ((Map<?, ?>) obj).isEmpty();
        }
        return false;
    }

    /**
     * @param <T>
     * @param value
     * @param nullValue
     * @return
     */
    public static <T> T nullIfEquals(T value, T nullValue) {
        return value != null && !value.equals(nullValue) ? value : null;
    }

    /**
     * @param <T>
     * @param value
     * @param defaultValue
     * @return
     */
    public static <T> T getOrDefault(T value, T defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * @param <T>
     * @param value
     * @param defaultValue
     * @return
     */
    public static <T> boolean hasOrDefault(T value, T defaultValue) {
        return value != null || defaultValue != null;
    }

    /**
     * @param <T>
     * @param values
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFirstNonNull(T... values) {
        for (T value : values) {
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * @param <T>
     * @param value
     * @param supplierDefault
     * @return
     */
    public static <T> T getOrDefault(T value, Supplier<T> supplierDefault) {
        if (value != null) {
            return value;
        }
        return supplierDefault != null ? supplierDefault.get() : null;
    }

    /**
     * @param <T>
     * @param suppliersDefault
     * @return
     */
    @SafeVarargs
    public static <T> T getFirstNotNull(Supplier<T>... suppliersDefault) {
        return getOrDefault(null, suppliersDefault);
    }

    /**
     * @param <T>
     * @param value
     * @param suppliersDefault
     * @return
     */
    @SafeVarargs
    public static <T> T getOrDefault(T value, Supplier<T>... suppliersDefault) {
        if (value != null
                || ArrayUtils.isEmpty(suppliersDefault)) {
            return value;
        }
        for (Supplier<T> supplier : suppliersDefault) {
            final T obj = supplier.get();
            if (obj != null) {
                return obj;
            }
        }
        return null;
    }

    /**
     * @param <T>
     * @param value
     * @param supplierDefault
     * @return
     */
    public static <T> T safeGetOrDefault(T value, Supplier<T> supplierDefault) {
        try {
            return getOrDefault(value, supplierDefault);
        } catch(Exception e) {
            return null;
        }
    }

}
