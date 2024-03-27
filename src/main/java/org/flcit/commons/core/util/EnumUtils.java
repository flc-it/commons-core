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
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class EnumUtils {

    private static final String MESSAGE_VALUE_NULL_OR_EMPTY = "Value is null or Empty";

    /**
     * 
     */
    public static final Comparator<Enum<?>> ordinalComparator = (Enum<?> o1, Enum<?> o2) -> Integer.compare(o1.ordinal(), o2.ordinal());

    private EnumUtils() { }

    /**
     * @param <T>
     * @param array
     * @param clazz
     * @return
     */
    public static <T extends Enum<T>> T[] convert(String[] array, Class<T[]> clazz) {
        return convert(array, clazz, false);
    }

    /**
     * @param <T>
     * @param array
     * @param clazz
     * @return
     */
    public static <T extends Enum<T>> T[] convertOnToString(String[] array, Class<T[]> clazz) {
        return convert(array, clazz, true);
    }

    @SuppressWarnings({ "unchecked", "java:S1168" })
    private static <T extends Enum<T>> T[] convert(String[] array, Class<T[]> clazz, boolean onToString) {
        if (ArrayUtils.isEmpty(array)
                || clazz == null) {
            return null;
        }
        final T[] res = clazz.cast(Array.newInstance(clazz.getComponentType(), array.length));
        for (int i = 0; i < array.length; i++) {
            if (!StringUtils.hasLength(array[i])) {
                continue;
            }
            res[i] = onToString ? convertOnToString(array[i], (Class<T>) clazz.getComponentType())
                    : Enum.valueOf((Class<T>) clazz.getComponentType(), array[i]);
        }
        return res;
    }

    /**
     * @param <T>
     * @param array
     * @param clazz
     * @return
     */
    public static <T extends Enum<T>> List<T> convert(List<String> array, Class<T> clazz) {
        return convert(array, clazz, false);
    }

    /**
     * @param <T>
     * @param array
     * @param clazz
     * @return
     */
    public static <T extends Enum<T>> List<T> convertOnToString(List<String> array, Class<T> clazz) {
        return convert(array, clazz, true);
    }

    @SuppressWarnings("java:S1168")
    private static <T extends Enum<T>> List<T> convert(List<String> array, Class<T> clazz, boolean onToString) {
        if (CollectionUtils.isEmpty(array) || clazz == null) {
            return null;
        }
        final List<T> res = new ArrayList<>(array.size());
        for (String val : array) {
            if (!StringUtils.hasLength(val)) {
                continue;
            }
            res.add(onToString ? convertOnToString(val, clazz)
                    : Enum.valueOf(clazz, val));
        }
        return res;
    }

    /**
     * @param <T>
     * @param name
     * @param clazz
     * @return
     */
    public static <T extends Enum<T>> T convert(String name, Class<T> clazz) {
        return name == null ? null : Enum.valueOf(clazz, name);
    }

    /**
     * @param <T>
     * @param name
     * @param clazz
     * @return
     */
    public static <T extends Enum<T>> T convertSafe(String name, Class<T> clazz) {
        return convertSafe(name, clazz, null);
    }

    /**
     * @param <T>
     * @param name
     * @param clazz
     * @param defaultValue
     * @return
     */
    public static <T extends Enum<T>> T convertSafe(String name, Class<T> clazz, T defaultValue) {
        try {
            return convert(name, clazz);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    /**
     * @param <T>
     * @param value
     * @param clazz
     * @return
     */
    public static <T extends Enum<?>> T convertOnToString(final String value, Class<T> clazz) {
        Assert.hasLength(value, MESSAGE_VALUE_NULL_OR_EMPTY);
        for (T v : values(clazz)) {
            if (value.equals(v.toString())) {
                return v;
            }
        }
        throw new IllegalArgumentException("No enum toString " + clazz.getCanonicalName() + "." + value);
    }

    /**
     * @param <T>
     * @param value
     * @param clazz
     * @return
     */
    public static <T extends Enum<?>> T convertOnNameOrToString(final String value, Class<T> clazz) {
        Assert.hasLength(value, MESSAGE_VALUE_NULL_OR_EMPTY);
        for (T v : values(clazz)) {
            if (value.equals(v.name())
                    || value.equals(v.toString())) {
                return v;
            }
        }
        throw new IllegalArgumentException("No enum constant or toString " + clazz.getCanonicalName() + "." + value);
    }

    /**
     * @param <T>
     * @param value
     * @param field
     * @param clazz
     * @param ignoreCase
     * @return
     */
    public static <T extends Enum<?>> T convert(final String value, final Field field, Class<T> clazz, boolean ignoreCase) {
        Assert.hasLength(value, MESSAGE_VALUE_NULL_OR_EMPTY);
        for (T v : values(clazz)) {
            final String val = ReflectionUtils.getFieldValue(v, field);
            final boolean equal = ignoreCase ? value.equalsIgnoreCase(val) : value.equals(val);
            if (equal) {
                return v;
            }
        }
        throw new IllegalArgumentException("No enum field " + clazz.getCanonicalName() + "." + field.getName() + "=" + value);
    }

    /**
     * @param <T>
     * @param value
     * @param clazz
     * @return
     */
    public static <T extends Enum<?>> T convertSafeOnToString(final String value, Class<T> clazz) {
        return convertSafeOnToString(value, clazz, null);
    }

    /**
     * @param <T>
     * @param value
     * @param clazz
     * @param defaultValue
     * @return
     */
    public static <T extends Enum<?>> T convertSafeOnToString(final String value, Class<T> clazz, T defaultValue) {
        try {
            return value == null ? null : convertOnToString(value, clazz);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    /**
     * @param <T>
     * @param value
     * @param clazz
     * @return
     */
    public static <T extends Enum<?>> T convertSafeOnNameOrToString(final String value, Class<T> clazz) {
        return convertSafeOnNameOrToString(value, clazz, null);
    }

    /**
     * @param <T>
     * @param value
     * @param clazz
     * @param defaultValue
     * @return
     */
    public static <T extends Enum<?>> T convertSafeOnNameOrToString(final String value, Class<T> clazz, T defaultValue) {
        try {
            return value == null ? null : convertOnNameOrToString(value, clazz);
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    /**
     * @param <T>
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Enum<?>> T[] values(Class<T> clazz) {
        try {
            return (T[]) clazz.getDeclaredMethod("values").invoke(null);
        } catch (ReflectiveOperationException e) { throw new IllegalStateException(e); }
    }

    /**
     * @param e
     * @return
     */
    public static String toString(Enum<?> e) {
        return e == null ? null : e.name();
    }

    /**
     * @param e
     * @return
     */
    @SuppressWarnings("java:S1168")
    public static String[] toStringArray(Enum<?>[] e) {
        if (ArrayUtils.isEmpty(e)) {
            return null;
        }
        final String[] res = new String[e.length];
        for (int i = 0 ; i < e.length; i++) {
            res[i] = toString(e[i]);
        }
        return res;
    }

}
