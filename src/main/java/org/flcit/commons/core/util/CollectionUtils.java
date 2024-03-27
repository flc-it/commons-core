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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class CollectionUtils {

    private CollectionUtils() { }

    /**
     * @param list
     * @return
     */
    public static boolean isEmpty(Collection<?> list) {
        return list == null || list.isEmpty();
    }

    /**
     * @param <T>
     * @param list
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(final List<T> list) {
        return isEmpty(list) ? null : list.toArray((T[]) Array.newInstance(list.get(0).getClass(), 0));
    }

    /**
     * @param <T>
     * @param list
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T[] toArray(final Collection<T> list, Class<T> clazz) {
        return isEmpty(list) ? null : list.toArray((T[]) Array.newInstance(clazz, 0));
    }

    /**
     * @param list
     * @return
     */
    @SuppressWarnings("java:S1168")
    public static int[] toArrayNative(final Collection<Integer> list) {
        if (isEmpty(list)) {
            return null;
        }
        int nbNull = 0;
        int i = 0;
        final int[] res = new int[list.size()];
        for (Integer v: list) {
            if (v == null) {
                nbNull++;
                continue;
            }
            res[i++] = v.intValue();
        }
        return Arrays.copyOf(res, res.length - nbNull);
    }

    /**
     * @param <T>
     * @param value
     * @return
     */
    public static <T> List<T> toListIfNotNull(T value) {
        return value != null ? Collections.singletonList(value) : null;
    }

    /**
     * @param <T>
     * @param <R>
     * @param list
     * @param function
     * @param value
     * @return
     */
    public static <T, R> int indexOf(List<T> list, Function<T, R> function, R value) {
        return indexOf(list, listValue -> {
            final R v = function.apply(listValue);
            return value == v || (v != null && v.equals(value));
        });
    }

    /**
     * @param <T>
     * @param list
     * @param predicate
     * @return
     */
    public static <T> int indexOf(List<T> list, Predicate<T> predicate) {
        if (isEmpty(list)) {
            return -1;
        }
        for (int i = 0; i < list.size(); i++) {
            if (predicate.test(list.get(i))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param <T>
     * @param list
     * @param predicate
     * @return
     */
    public static <T> boolean exist(List<T> list, Predicate<T> predicate) {
        return indexOf(list, predicate) != -1;
    }

}
