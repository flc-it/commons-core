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

import java.util.Collection;
import java.util.Iterator;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class IterableUtils {

    private IterableUtils() { }

    /**
     * @param <T>
     * @param list
     * @return
     */
    public static <T> boolean isNullOrEmpty(final Iterable<T> list) {
        return list == null || isNullOrEmpty(list.iterator());
    }

    /**
     * @param <T>
     * @param list
     * @return
     */
    public static <T> boolean isNullOrEmpty(final Iterator<T> list) {
        return list == null || !list.hasNext();
    }

    /**
     * @param <T>
     * @param iterator
     * @return
     */
    public static <T> T getFirst(Iterator<T> iterator) {
        if (iterator == null || !iterator.hasNext()) {
            return null;
        }
        return iterator.next();
    }

    /**
     * @param <T>
     * @param iterable
     * @return
     */
    public static <T> T getFirst(Iterable<T> iterable) {
        if (iterable == null) {
            return null;
        }
        return getFirst(iterable.iterator());
    }

    /**
     * @param iterable
     * @return
     */
    public static int size(Iterable<?> iterable) {
        if (iterable == null) {
            return 0;
        }
        if (iterable instanceof Collection) {
            return ((Collection<?>) iterable).size();
        }
        int i = 0;
        for (@SuppressWarnings("unused") Object t: iterable) {
            i++;
        }
        return i;
    }

    /**
     * @param <T>
     * @param array
     * @param v
     * @return
     */
    public static <T> boolean containsOne(final Iterable<T> array, final T[] v) {
        if (array == null || ArrayUtils.isEmpty(v)) {
            return false;
        }
        for (final T e : array) {
            if (ArrayUtils.contains(v, e)) {
                return true;
            }
        }
        return false;
    }

}
