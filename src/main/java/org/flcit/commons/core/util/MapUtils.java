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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class MapUtils {

    private MapUtils() { }

    /**
     * @param map
     * @return
     */
    public static boolean isEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    /**
     * @param values
     * @return
     */
    public static int size(Map<?, ?> values) {
        return values == null ? 0 : values.size();
    }

    /**
     * @param <T>
     * @param <U>
     * @param <R>
     * @param map
     * @param mapper
     * @param classType
     * @return
     */
    @SuppressWarnings("java:S1168")
    public static <T, U, R> R[] flatFilterAndMapToArray(Map<T, List<U>> map, BiFunction<T, U, R> mapper, Class<R> classType) {
        if (isEmpty(map)) {
            return null;
        }
        final List<R> list = new ArrayList<>();
        for (Entry<T, List<U>> entry : map.entrySet()) {
            for (U v : entry.getValue()) {
                list.add(mapper.apply(entry.getKey(), v));
            }
        }
        return CollectionUtils.toArray(list, classType);
    }

    /**
     * @param <K>
     * @param map
     * @param key
     * @return
     */
    public static <K> int indexOf(Map<K, ?> map, K key) {
        if (isEmpty(map)) {
            return -1;
        }
        int i = 0;
        for (K entry : map.keySet()) {
            if (entry == key || (entry != null && entry.equals(key))) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * @param <T>
     * @param <U>
     * @param map
     * @param predicate
     * @return
     */
    @SuppressWarnings("java:S1168")
    public static <T, U> Map<T, U> filter(Map<T, U> map, BiPredicate<T, U> predicate) {
        if (isEmpty(map)) {
            return null;
        }
        final Map<T, U> mapRes = map instanceof LinkedHashMap ? new LinkedHashMap<>(map.size()) : new HashMap<>(map.size());
        for (Entry<T, U> entry : map.entrySet()) {
            if (predicate.test(entry.getKey(), entry.getValue())) {
                mapRes.put(entry.getKey(), entry.getValue());
            }
        }
        return mapRes;
    }

    /**
     * @param <T>
     * @param <U>
     * @param map
     * @param predicate
     * @return
     */
    @SuppressWarnings("java:S1168")
    public static <T, U> Map<T, List<U>> filterList(Map<T, List<U>> map, BiPredicate<T, U> predicate) {
        if (isEmpty(map)) {
            return null;
        }
        final Map<T, List<U>> mapRes = map instanceof LinkedHashMap ? new LinkedHashMap<>(map.size()) : new HashMap<>(map.size());
        for (Entry<T, List<U>> entry : map.entrySet()) {
            if (CollectionUtils.isEmpty(entry.getValue())) {
                continue;
            }
            final List<U> list = new ArrayList<>(entry.getValue().size());
            for (U v : entry.getValue()) {
                if (predicate.test(entry.getKey(), v)) {
                    list.add(v);
                }
            }
            if (!CollectionUtils.isEmpty(list)) {
                mapRes.put(entry.getKey(), list);
            }
        }
        return mapRes;
    }

    /**
     * @param <T>
     * @param <U>
     * @param <R>
     * @param map
     * @param mapper
     * @param classType
     * @return
     */
    @SuppressWarnings("java:S1168")
    public static <T, U, R> R[] flatListAndMapToArray(Map<T, List<U>> map, BiFunction<T, U, R> mapper, Class<R> classType) {
        if (isEmpty(map)) {
            return null;
        }
        final List<R> list = new ArrayList<>();
        for (Entry<T, List<U>> entry : map.entrySet()) {
            for (U v : entry.getValue()) {
                list.add(mapper.apply(entry.getKey(), v));
            }
        }
        return CollectionUtils.toArray(list, classType);
    }

    /**
     * @param <T>
     * @param <U>
     * @param <R>
     * @param map
     * @param mapper
     * @param classType
     * @return
     */
    @SuppressWarnings("java:S1168")
    public static <T, U, R> R[] flatAndMapToArray(Map<T, U[]> map, BiFunction<T, U, R> mapper, Class<R> classType) {
        if (isEmpty(map)) {
            return null;
        }
        final List<R> list = new ArrayList<>();
        for (Entry<T, U[]> entry : map.entrySet()) {
            if (ArrayUtils.isEmpty(entry.getValue())) {
                continue;
            }
            for (U v : entry.getValue()) {
                list.add(mapper.apply(entry.getKey(), v));
            }
        }
        return CollectionUtils.toArray(list, classType);
    }

    /**
     * @param <T>
     * @param <U>
     * @param <R>
     * @param map
     * @param mapper
     * @return
     */
    public static <T, U, R> Map<T, R> mapValue(Map<T, U> map, BiFunction<T, U, R> mapper) {
        return mapValue(map, mapper, false);
    }

    /**
     * @param <T>
     * @param <U>
     * @param <R>
     * @param map
     * @param mapper
     * @param linked
     * @return
     */
    @SuppressWarnings("java:S1168")
    public static <T, U, R> Map<T, R> mapValue(Map<T, U> map, BiFunction<T, U, R> mapper, boolean linked) {
        if (isEmpty(map)) {
            return null;
        }
        final Map<T, R> res = linked ? new LinkedHashMap<>(map.size()) : new HashMap<>(map.size());
        for (Entry<T, U> entry : map.entrySet()) {
            res.put(entry.getKey(), mapper.apply(entry.getKey(), entry.getValue()));
        }
        return res;
    }

    /**
     * @param <T>
     * @param <U>
     * @param <R>
     * @param map
     * @param mapper
     * @param classType
     * @return
     */
    @SuppressWarnings({ "unchecked", "java:S1168" })
    public static <T, U, R> R[] mapToArray(Map<T, U> map, BiFunction<T, U, R> mapper, Class<R> classType) {
        if (isEmpty(map)) {
            return null;
        }
        final R[] res = (R[]) Array.newInstance(classType, map.size());
        int i = 0;
        for (Entry<T, U> entry : map.entrySet()) {
            res[i++] = mapper.apply(entry.getKey(), entry.getValue());
        }
        return res;
    }

    /**
     * @param <T>
     * @param <R>
     * @param map
     * @param classType
     * @return
     */
    @SuppressWarnings("java:S1168")
    public static <T, R> R[] flatToArray(Map<T, R[]> map, Class<R> classType) {
        if (isEmpty(map)) {
            return null;
        }
        final List<R> list = new ArrayList<>();
        for (Entry<T, R[]> entry : map.entrySet()) {
            if (ArrayUtils.isEmpty(entry.getValue())) {
                continue;
            }
            list.addAll(Arrays.asList(entry.getValue()));
        }
        return CollectionUtils.toArray(list, classType);
    }

    /**
     * @param <T>
     * @param <R>
     * @param map
     * @param classType
     * @return
     */
    @SuppressWarnings("java:S1168")
    public static <T, R> R[] flatListToArray(Map<T, List<R>> map, Class<R> classType) {
        if (isEmpty(map)) {
            return null;
        }
        final List<R> list = new ArrayList<>();
        for (Entry<T, List<R>> entry : map.entrySet()) {
            if (CollectionUtils.isEmpty(entry.getValue())) {
                continue;
            }
            list.addAll(entry.getValue());
        }
        return CollectionUtils.toArray(list, classType);
    }

    /**
     * @param <T>
     * @param <V>
     * @param array1
     * @param array2
     * @return
     */
    @SuppressWarnings("java:S1168")
    public static <T, V> Map<T, List<V>> convert(T[] array1, V[] array2) {
        if (ArrayUtils.isEmpty(array1)
                || ArrayUtils.isEmpty(array2)) {
            return null;
        }
        final Map<T, List<V>> map = new LinkedHashMap<>();
        for (int i = 0; i < array1.length && i < array2.length; i++) {
            List<V> values = map.get(array1[i]);
            if (values == null) {
                values = new ArrayList<>(1);
                map.put(array1[i], values);
            }
            values.add(array2[i]);
        }
        return map;
    }

    /**
     * @param <T>
     * @param <E>
     * @param list
     * @param key
     * @param value
     * @return
     */
    @SuppressWarnings({ "unchecked", "java:S1168" })
    public static <T, E> Map<T, List<E>> convert(List<Object[]> list, int key, int value) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        final Map<T, List<E>> res = new HashMap<>();
        for (Object[] obj : list) {
            List<E> l = res.get(obj[key]);
            if (l == null) {
                l = new ArrayList<>(1);
                res.put((T) obj[key], l);
            }
            l.add((E) obj[value]);
        }
        return res;
    }

}
