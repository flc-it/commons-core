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
import java.util.List;
import java.util.stream.Collectors;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class ClassUtils {

    private ClassUtils() { }

    /**
     * @param <T>
     * @param value
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T> getSafe(T value) {
        return value != null ? (Class<T>) value.getClass() : null;
    }

    /**
     * @param <T>
     * @param values
     * @return
     */
    public static <T> List<Class<T>> listSafe(Collection<T> values) {
        return CollectionUtils.isEmpty(values) ? values.stream().map(ClassUtils::getSafe).collect(Collectors.toList()) : null;
    }

    /**
     * @param clazz
     * @param className
     * @return
     */
    public static boolean equals(Class<?> clazz, String className) {
        return clazz != null && clazz.getName().equals(className);
    }

    /**
     * @param clazz
     * @param classType
     * @return
     */
    public static boolean equals(Class<?> clazz, Class<?> classType) {
        return clazz != null && clazz.equals(classType);
    }

    /**
     * @param value
     * @param className
     * @return
     */
    public static boolean isClass(Object value, String className) {
        return value != null && equals(value.getClass(), className);
    }

    /**
     * @param value
     * @param classType
     * @return
     */
    public static boolean isClass(Object value, Class<?> classType) {
        return value != null && equals(value.getClass(), classType);
    }

    /**
     * @param className
     * @param classType
     * @return
     */
    public static boolean safeIsAssignableFrom(String className, Class<?> classType) {
        if (classType == null) {
            return false;
        }
        final Class<?> classTarget = find(className);
        return classTarget != null && classTarget.isAssignableFrom(classType);
    }

    /**
     * @param className
     * @return
     */
    public static Class<?> find(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    /**
     * @param className
     * @return
     */
    public static boolean exist(String className) {
        return find(className) != null;
    }

    /**
     * @param <T>
     * @param clazz
     * @return
     * @throws ClassNotFoundException
     */
    @SuppressWarnings("unchecked")
    public static <T> Class<T[]> getArrayClass(Class<T> clazz) throws ClassNotFoundException {
        return (Class<T[]>) Class.forName("[L" + clazz.getName() + ';');
    }

    /**
     * @param <T>
     * @param clazz
     * @return
     */
    public static <T> Class<T[]> getSafeArrayClass(Class<T> clazz) {
        try {
            return getArrayClass(clazz);
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * @param clazz
     * @return
     */
    public static Class<?> objectClass(Class<?> clazz) {
        if (clazz == null || !clazz.isPrimitive()) {
            return clazz;
        }
        if (clazz == char.class) {
            return Character.class;
        } else if (clazz == int.class) {
            return Integer.class;
        } else if (clazz == boolean.class) {
            return Boolean.class;
        } else if (clazz == byte.class) {
            return Byte.class;
        } else if (clazz == double.class) {
            return Double.class;
        } else if (clazz == float.class) {
            return Float.class;
        } else if (clazz == long.class) {
            return Long.class;
        } else if (clazz == short.class) {
            return Short.class;
        } else {
            return clazz;
        }
    }

    /**
     * @param clazz
     * @return
     */
    public static boolean isCollection(Class<?> clazz) {
        return clazz != null && Collection.class.isAssignableFrom(clazz);
    }

    /**
     * @param clazz
     * @return
     */
    public static boolean isEnum(Class<?> clazz) {
        return clazz != null && clazz.isEnum();
    }

    /**
     * @param clazz
     * @return
     */
    public static boolean isArray(Class<?> clazz) {
        return clazz != null && clazz.isArray();
    }

    /**
     * @param clazz
     * @return
     * @throws ClassNotFoundException
     */
    public static Class<?> classForName(String clazz) throws ClassNotFoundException {
        if (!StringUtils.hasLength(clazz)) {
            return null;
        }
        switch (clazz) {
        case "boolean":
            return boolean.class;
        case "[Z":
            return boolean[].class;
        case "[[Z":
            return boolean[][].class;
        case "byte":
            return byte.class;
        case "[B":
            return byte[].class;
        case "[[B":
            return byte[][].class;
        case "short":
            return short.class;
        case "[S":
            return short[].class;
        case "[[S":
            return short[][].class;
        case "int":
            return int.class;
        case "[I":
            return int[].class;
        case "[[I":
            return int[][].class;
        case "long":
            return long.class;
        case "[J":
            return long[].class;
        case "[[J":
            return long[][].class;
        case "float":
            return float.class;
        case "[F":
            return float[].class;
        case "[[F":
            return float[][].class;
        case "double":
            return double.class;
        case "[D":
            return double[].class;
        case "[[D":
            return double[][].class;
        case "char":
            return char.class;
        case "[C":
            return char[].class;
        case "[[C":
            return char[][].class;
        case "void":
            return void.class;
        default:
            return Class.forName(clazz);
        }
    }

}
