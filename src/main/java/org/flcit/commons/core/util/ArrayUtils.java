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
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class ArrayUtils {

    private ArrayUtils() { }

    /**
     * @param array
     * @return
     */
    public static boolean isEmpty(Object[] array) {
        return array == null || array.length == 0;
    }

    /**
     * @param <T>
     * @param array
     * @return
     */
    public static <T> int length(final T[] array) {
        return array == null ? 0 : array.length;
    }

    /**
     * @param <T>
     * @param objects
     * @return
     */
    @SafeVarargs
    public static <T> T[] toArray(T... objects) {
        return objects;
    }

    private static short[] toNativeArray(Short[] array) {
        final short[] res = new short[array.length];
        for (int i = 0; i < array.length; i++) {
            res[i] = array[i] != null ? array[i] : 0;
        }
        return res;
    }

    private static int[] toNativeArray(Integer[] array) {
        final int[] res = new int[array.length];
        for (int i = 0; i < array.length; i++) {
            res[i] = array[i] != null ? array[i] : 0;
        }
        return res;
    }

    private static long[] toNativeArray(Long[] array) {
        final long[] res = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            res[i] = array[i] != null ? array[i] : 0;
        }
        return res;
    }

    private static byte[] toNativeArray(Byte[] array) {
        final byte[] res = new byte[array.length];
        for (int i = 0; i < array.length; i++) {
            res[i] = array[i] != null ? array[i] : 0;
        }
        return res;
    }

    private static float[] toNativeArray(Float[] array) {
        final float[] res = new float[array.length];
        for (int i = 0; i < array.length; i++) {
            res[i] = array[i] != null ? array[i] : 0;
        }
        return res;
    }

    private static double[] toNativeArray(Double[] array) {
        final double[] res = new double[array.length];
        for (int i = 0; i < array.length; i++) {
            res[i] = array[i] != null ? array[i] : 0;
        }
        return res;
    }

    private static boolean[] toNativeArray(Boolean[] array) {
        final boolean[] res = new boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            if (array[i] != null) {
                res[i] = array[i];
            }
        }
        return res;
    }

    private static char[] toNativeArray(Character[] array) {
        final char[] res = new char[array.length];
        for (int i = 0; i < array.length; i++) {
            res[i] = array[i] != null ? array[i] : 0;
        }
        return res;
    }

    /**
     * @param <T>
     * @param objectArray
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T toNativeArray(Object objectArray) {
        if (objectArray instanceof Long[]) {
            return (T) toNativeArray((Long[]) objectArray);
        } else if (objectArray instanceof Integer[]) {
            return (T) toNativeArray((Integer[]) objectArray);
        } else if (objectArray instanceof Short[]) {
            return (T) toNativeArray((Short[]) objectArray);
        } else if (objectArray instanceof Double[]) {
            return (T) toNativeArray((Double[]) objectArray);
        } else if (objectArray instanceof Float[]) {
            return (T) toNativeArray((Float[]) objectArray);
        } else if (objectArray instanceof Byte[]) {
            return (T) toNativeArray((Byte[]) objectArray);
        } else if (objectArray instanceof Character[]) {
            return (T) toNativeArray((Character[]) objectArray);
        } else if (objectArray instanceof Boolean[]) {
            return (T) toNativeArray((Boolean[]) objectArray);
        } else {
            return (T) objectArray;
        }
    }

    private static boolean[] fromStringBoolean(String[] values) {
        final boolean[] array = new boolean[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = Boolean.valueOf(values[i]);
        }
        return array;
    }

    private static byte[] fromStringByte(String[] values) {
        final byte[] array = new byte[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = Byte.valueOf(values[i]);
        }
        return array;
    }

    private static short[] fromStringShort(String[] values) {
        final short[] array = new short[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = Short.valueOf(values[i]);
        }
        return array;
    }

    private static int[] fromStringInt(String[] values) {
        final int[] array = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = Integer.valueOf(values[i]);
        }
        return array;
    }

    private static long[] fromStringLong(String[] values) {
        final long[] array = new long[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = Long.valueOf(values[i]);
        }
        return array;
    }

    private static float[] fromStringFloat(String[] values) {
        final float[] array = new float[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = Float.valueOf(values[i]);
        }
        return array;
    }

    private static double[] fromStringDouble(String[] values) {
        final double[] array = new double[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = Double.valueOf(values[i]);
        }
        return array;
    }

    private static char[] fromStringChar(String[] values) {
        final char[] array = new char[values.length];
        for (int i = 0; i < values.length; i++) {
            array[i] = values[i].length() > 0 ? values[i].charAt(0) : Character.MIN_VALUE;
        }
        return array;
    }

    /**
     * @param <T>
     * @param string
     * @param arrayClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T fromString(String string, Class<T> arrayClass) {
        final String[] values = string.replaceAll("[\\[\\]]", StringUtils.EMPTY).split(",");
        if (boolean[].class.equals(arrayClass)) {
            return (T) fromStringBoolean(values);
        } else if (byte[].class.equals(arrayClass)) {
            return (T) fromStringByte(values);
        } else if (short[].class.equals(arrayClass)) {
            return (T) fromStringShort(values);
        } else if (int[].class.equals(arrayClass)) {
            return (T) fromStringInt(values);
        } else if (long[].class.equals(arrayClass)) {
            return (T) fromStringLong(values);
        } else if (float[].class.equals(arrayClass)) {
            return (T) fromStringFloat(values);
        } else if (double[].class.equals(arrayClass)) {
            return (T) fromStringDouble(values);
        } else if (char[].class.equals(arrayClass)) {
            return (T) fromStringChar(values);
        } else {
            return (T) values;
        }
    }

    private static Boolean[] toObjectArray(boolean[] array) {
        final Boolean[] res = new Boolean[array.length];
        for (int i = 0; i < array.length; i++) {
            res[i] = Boolean.valueOf(array[i]);
        }
        return res;
    }

    private static Byte[] toObjectArray(byte[] array) {
        final Byte[] res = new Byte[array.length];
        for (int i = 0; i < array.length; i++) {
            res[i] = Byte.valueOf(array[i]);
        }
        return res;
    }

    private static Short[] toObjectArray(short[] array) {
        final Short[] res = new Short[array.length];
        for (int i = 0; i < array.length; i++) {
            res[i] = Short.valueOf(array[i]);
        }
        return res;
    }

    private static Integer[] toObjectArray(int[] array) {
        final Integer[] res = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            res[i] = Integer.valueOf(array[i]);
        }
        return res;
    }

    private static Long[] toObjectArray(long[] array) {
        final Long[] res = new Long[array.length];
        for (int i = 0; i < array.length; i++) {
            res[i] = Long.valueOf(array[i]);
        }
        return res;
    }

    private static Float[] toObjectArray(float[] array) {
        final Float[] res = new Float[array.length];
        for (int i = 0; i < array.length; i++) {
            res[i] = Float.valueOf(array[i]);
        }
        return res;
    }

    private static Double[] toObjectArray(double[] array) {
        final Double[] res = new Double[array.length];
        for (int i = 0; i < array.length; i++) {
            res[i] = Double.valueOf(array[i]);
        }
        return res;
    }

    private static Character[] toObjectArray(char[] array) {
        final Character[] res = new Character[array.length];
        for (int i = 0; i < array.length; i++) {
            res[i] = Character.valueOf(array[i]);
        }
        return res;
    }

    private static Object[] toObjectArray(Object[] array) {
        return isEmpty(array) ? null : array;
    }

    /**
     * @param objectArray
     * @return
     */
    public static Object[] toObjectArray(Object objectArray) {
        if (objectArray instanceof boolean[]) {
            return toObjectArray((boolean[]) objectArray);
        } else if (objectArray instanceof byte[]) {
            return toObjectArray((byte[]) objectArray);
        } else if (objectArray instanceof short[]) {
            return toObjectArray((short[]) objectArray);
        } else if (objectArray instanceof int[]) {
            return toObjectArray((int[]) objectArray);
        } else if (objectArray instanceof long[]) {
            return toObjectArray((long[]) objectArray);
        } else if (objectArray instanceof float[]) {
            return toObjectArray((float[]) objectArray);
        } else if (objectArray instanceof double[]) {
            return toObjectArray((double[]) objectArray);
        } else if (objectArray instanceof char[]) {
            return toObjectArray((char[]) objectArray);
        } else {
            return toObjectArray((Object[]) objectArray);
        }
    }

    /**
     * @param <T>
     * @param array
     * @param filter
     * @return
     */
    public static <T> T[] filter(T[] array, Predicate<T> filter) {
        if (isEmpty(array)) {
            return array;
        }
        final List<T> res = new ArrayList<>();
        for (T r: array) {
            if (r != null && filter.test(r)) {
                res.add(r);
            }
        }
        return CollectionUtils.toArray(res);
    }

    /**
     * @param <T>
     * @param array
     * @param filter
     * @return
     */
    public static <T> T find(T[] array, Predicate<T> filter) {
        if (isEmpty(array)) {
            return null;
        }
        for (T r: array) {
            if (r != null && filter.test(r)) {
                return r;
            }
        }
        return null;
    }

    /**
     * @param <T>
     * @param array
     * @param filter
     * @return
     */
    public static <T> boolean exist(T[] array, Predicate<T> filter) {
        return find(array, filter) != null;
    }

    /**
     * @param <T>
     * @param array
     * @param maxLength
     * @return
     */
    public static <T> T[] limit(T[] array, Integer maxLength) {
        if (isEmpty(array) || maxLength == null) {
            return array;
        }
        return Arrays.copyOfRange(array, 0, Math.min(maxLength, array.length));
    }

    /**
     * @param <T>
     * @param values
     * @param value
     * @return
     */
    public static <T> T[] remove(T[] values, T value) {
        if (isEmpty(values)) {
            return values;
        }
        final List<T> list = new ArrayList<>();
        for (T o: values) {
            if (o != value && (o == null || !o.equals(value))) {
                list.add(o);
            }
        }
        return CollectionUtils.toArray(list);
    }

    /**
     * @param <T>
     * @param array
     * @param toRemove
     * @return
     */
    public static <T> T[] removeIfNotContains(T[] array, T[] toRemove) {
        return remove(array, toRemove, false);
    }

    /**
     * @param <T>
     * @param array
     * @param toRemove
     * @return
     */
    public static <T> T[] remove(T[] array, T[] toRemove) {
        return remove(array, toRemove, true);
    }

    private static <T> T[] remove(T[] array, T[] toRemove, boolean contains) {
        if (isEmpty(array)
                || isEmpty(toRemove)) {
            return array;
        }
        final List<T> r = new ArrayList<>(contains ? array.length - toRemove.length : Math.min(array.length, toRemove.length));
        for (T obj : array) {
            boolean containsObj = contains(toRemove, obj);
            if ((contains && !containsObj) || (!contains && containsObj)) {
                r.add(obj);
            }
        }
        return CollectionUtils.toArray(r);
    }

    /**
     * @param <T>
     * @param <R>
     * @param array
     * @param predicate
     * @param map
     * @return
     */
    @SuppressWarnings("java:S1168")
    public static <T, R> R[] filterAndMap(T[] array, Predicate<T> predicate, Function<T, R> map) {
        if (isEmpty(array)) {
            return null;
        }
        final List<R> res = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            if (predicate.test(array[i])) {
                res.add(map.apply(array[i]));
            }
        }
        return CollectionUtils.toArray(res);
    }

    /**
     * @param <T>
     * @param <R>
     * @param array
     * @param map
     * @param classType
     * @return
     */
    @SuppressWarnings({ "unchecked", "java:S1168" })
    public static <T, R> R[] map(T[] array, Function<T, R> map, Class<R> classType) {
        if (isEmpty(array)) {
            return null;
        }
        final R[] res = (R[]) Array.newInstance(classType, array.length);
        for (int i = 0; i < array.length; i++) {
            res[i] = map.apply(array[i]);
        }
        return res;
    }

    /**
     * @param <T>
     * @param <R>
     * @param list
     * @param map
     * @param classType
     * @return
     */
    @SuppressWarnings({ "unchecked", "java:S1168" })
    public static <T, R> R[] map(List<T> list, Function<T, R> map, Class<R> classType) {
        if (CollectionUtils.isEmpty(list)) {
            return null;
        }
        final R[] res = (R[]) Array.newInstance(classType, list.size());
        for (int i = 0; i < list.size(); i++) {
            res[i] = map.apply(list.get(i));
        }
        return res;
    }

    /**
     * @param <T>
     * @param <R>
     * @param array
     * @param map
     * @return
     */
    @SuppressWarnings("java:S1168")
    public static <T, R> R sum(T[] array, BiFunction<T, R, R> map) {
        if (isEmpty(array)) {
            return null;
        }
        R res = null;
        for (int i = 0; i < array.length; i++) {
            res = map.apply(array[i], res);
        }
        return res;
    }

    /**
     * @param <T>
     * @param array
     * @param value
     * @return
     */
    public static <T> boolean contains(T[] array, T value) {
        return indexOf(array, value) != -1;
    }

    /**
     * @param <T>
     * @param array
     * @param value
     * @return
     */
    public static <T> int indexOf(T[] array, T value) {
        if (isEmpty(array)) {
            return -1;
        }
        for (int i = 0; i < array.length; i++) {
            if (value == array[i] || (array[i] != null && array[i].equals(value))) {
                return i;
            }
        }
        return -1;
    }

    /**
     * @param <T>
     * @param values
     * @return
     */
    public static <T> T getFirst(T[] values) {
        return !isEmpty(values) ? values[0] : null;
    }

    /**
     * @param <T>
     * @param arrays
     * @return
     */
    @SuppressWarnings({ "unchecked", "java:S1168" })
    public static <T> T[] concat(T[]... arrays) {
        if (isEmpty(arrays)) {
            return null;
        }
        T[] res = arrays[0];
        for (int i = 1 ; i < arrays.length; i++) {
            res = concat(res, arrays[i]);
        }
        return res;
    }

    /**
     * @param <T>
     * @param array1
     * @param list
     * @param clazz
     * @return
     */
    public static <T> T[] concat(T[] array1, Collection<T> list, Class<T> clazz) {
        if (isEmpty(array1)) {
            return CollectionUtils.toArray(list, clazz);
        }
        if (CollectionUtils.isEmpty(list)) {
            return array1;
        }
        return concat(array1, CollectionUtils.toArray(list, clazz));
    }

    /**
     * @param <T>
     * @param array1
     * @param array2
     * @return
     */
    public static <T> T[] concat(T[] array1, T[] array2) {
        if (isEmpty(array1)) {
            return array2;
        }
        if (isEmpty(array2)) {
            return array1;
        }
        final T[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    /**
     * @param <T>
     * @param array1
     * @param array2
     * @return
     */
    public static <T> T[] concatWithoutDouble(T[] array1, T[] array2) {
        if (isEmpty(array1)) {
            return array2;
        }
        if (isEmpty(array2)) {
            return array1;
        }
        final List<T> list = new ArrayList<>();
        for (T o: array1) {
            if (o != null && !list.contains(o) ) {
                list.add(o);
            }
        }
        for (T o: array2) {
            if (o != null && !list.contains(o) ) {
                list.add(o);
            }
        }
        return CollectionUtils.toArray(list);
    }

    /**
     * @param array1
     * @param array2
     * @return
     */
    public static double[] concat(double[] array1, double[] array2) {
        if (ObjectUtils.isEmpty(array1)) {
            return array2;
        }
        if (ObjectUtils.isEmpty(array2)) {
            return array1;
        }
        final double[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    /**
     * @param array
     * @param v
     * @return
     */
    @SuppressWarnings("java:S4973")
    public static boolean containsIgnoreCase(final String[] array, final String v) {
        if (array == null) {
            return false;
        }
        for (final String e : array) {
            if ((e == v) || (v != null && v.equalsIgnoreCase(e))) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param <T>
     * @param array
     * @param v
     * @return
     */
    public static <T> boolean containsOne(final T[] array, final T[] v) {
        if (array == null || ArrayUtils.isEmpty(v)) {
            return false;
        }
        for (final T e : array) {
            if (contains(v, e)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param array
     * @param v
     * @return
     */
    public static boolean contains(final int[] array, final int v) {
        if (array == null) {
            return false;
        }
        for (final int e : array) {
            if (v == e) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param <T>
     * @param array
     * @param v
     * @return
     */
    public static <T> boolean containsAll(final T[] array, final T[] v) {
        if (ArrayUtils.isEmpty(v)) {
            return true;
        }
        if (array == null) {
            return false;
        }
        for (final T e : v) {
            if (!contains(array, e)) {
                return false;
            }
        }
        return true;
    }

}
