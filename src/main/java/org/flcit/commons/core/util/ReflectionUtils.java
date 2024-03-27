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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class ReflectionUtils {

    private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class<?>[0];
    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    private static final String SET = "set";
    private static final String GET = "get";
    private static final String IS = "is";
    private static final String ACCESS_ERROR_MESSAGE = "Could not access method or field: ";

    private ReflectionUtils() { }

    /**
     * @param clazz
     * @param type
     * @return
     */
    public static Field findField(Class<?> clazz, Class<?> type) {
        return findField(clazz, null, type);
    }

    /**
     * @param clazz
     * @param name
     * @return
     */
    public static Field findField(Class<?> clazz, String name) {
        return findField(clazz, name, null);
    }

    /**
     * @param clazz
     * @param name
     * @param type
     * @return
     */
    public static Field findField(Class<?> clazz, String name, Class<?> type) {
        Assert.notNull(clazz, "Class must not be null");
        Class<?> searchType = clazz;
        while (Object.class != searchType && searchType != null) {
            for (Field field : searchType.getDeclaredFields()) {
                if ((name == null || name.equals(field.getName())) &&
                        (type == null || type.equals(field.getType()))) {
                    return field;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    /**
     * @param target
     * @param name
     * @return
     */
    public static Object getSafeFieldValue(final Object target, final String name) {
        return getSafeFieldValue(target, name, null);
    }

    /**
     * @param <T>
     * @param target
     * @param name
     * @param fieldType
     * @return
     */
    public static <T> T getSafeFieldValue(final Object target, final String name, final Class<T> fieldType) {
        return target != null ? getSafeFieldValue(target, target.getClass(), name, fieldType) : null;
    }

    /**
     * @param <T>
     * @param target
     * @param clazz
     * @param name
     * @param fieldType
     * @return
     */
    public static <T> T getSafeFieldValue(final Object target, final Class<?> clazz, final String name, final Class<T> fieldType) {
        try {
            return fieldType != null ? getFieldValue(target, clazz, name, fieldType) : getFieldValue(target, clazz, name);
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * @param <T>
     * @param target
     * @param name
     * @return
     */
    public static <T> T getFieldValue(final Object target, final String name) {
        return target != null ? getFieldValue(target, target.getClass(), name) : null;
    }

    /**
     * @param <T>
     * @param target
     * @param name
     * @param fieldType
     * @return
     */
    public static <T> T getFieldValue(final Object target, final String name, final Class<T> fieldType) {
        return target != null ? getFieldValue(target, target.getClass(), name, fieldType) : null;
    }

    /**
     * @param <T>
     * @param target
     * @param clazz
     * @param name
     * @return
     */
    public static <T> T getFieldValue(final Object target, final Class<?> clazz, final String name) {
        return getFieldValue(target, findField(clazz, name));
    }

    /**
     * @param <T>
     * @param target
     * @param clazz
     * @param name
     * @param fieldType
     * @return
     */
    public static <T> T getFieldValue(final Object target, final Class<?> clazz, final String name, final Class<T> fieldType) {
        return getFieldValue(target, findField(clazz, name, fieldType));
    }

    /**
     * @param <T>
     * @param target
     * @param field
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(final Object target, final Field field) {
        return field == null || target == null ? null : (T) getSafeFieldIntern(makeAccessible(field), target);
    }

    private static Object getSafeFieldIntern(Field field, Object target) {
        try {
            return field.get(target);
        }
        catch (IllegalAccessException ex) {
            throw new IllegalStateException(ACCESS_ERROR_MESSAGE + ex.getMessage());
        }
    }

    /**
     * @param <T>
     * @param target
     * @param name
     * @param value
     */
    @SuppressWarnings("unchecked")
    public static <T> void setFieldValue(final Object target, final String name, final T value) {
        setFieldValue(target, name, value != null ? (Class<T>) value.getClass() : null, value);
    }

    /**
     * @param <T>
     * @param target
     * @param name
     * @param fieldType
     * @param value
     */
    public static <T> void setFieldValue(final Object target, final String name, final Class<T> fieldType, final T value) {
        if (target != null) {
            setFieldValue(target, target.getClass(), name, fieldType, value);
        }
    }

    /**
     * @param <T>
     * @param target
     * @param clazz
     * @param name
     * @param fieldType
     * @param value
     */
    public static <T> void setFieldValue(final Object target, final Class<?> clazz, final String name, final Class<T> fieldType, final T value) {
        setFieldValue(target, findField(clazz, name, fieldType), value);
    }

    /**
     * @param <T>
     * @param target
     * @param field
     * @param value
     */
    public static <T> void setFieldValue(final Object target, final Field field, final T value) {
        if (field != null && target != null) {
            setField(makeAccessible(field), target, value);
        }
    }

    /**
     * @param field
     * @param target
     * @param value
     */
    @SuppressWarnings("java:S3011")
    public static void setField(Field field, Object target, Object value) {
        try {
            field.set(target, value);
        }
        catch (IllegalAccessException ex) {
            throw new IllegalStateException(ACCESS_ERROR_MESSAGE + ex.getMessage());
        }
    }

    private static Field makeAccessible(final Field field) {
        if (field != null) {
            makeAccessibleIntern(field);
        }
        return field;
    }

    @SuppressWarnings("java:S3011")
    private static void makeAccessibleIntern(Field field) {
        if ((!Modifier.isPublic(field.getModifiers()) ||
                !Modifier.isPublic(field.getDeclaringClass().getModifiers()) ||
                Modifier.isFinal(field.getModifiers())) && !field.isAccessible()) {
            field.setAccessible(true);
        }
    }

    /**
     * @param target
     * @param name
     * @return
     */
    public static Object getSafeMethodValue(final Object target, final String name) {
        return target != null ? getSafeMethodValue(target, target.getClass(), name) : null;
    }

    /**
     * @param target
     * @param classObject
     * @param name
     * @return
     */
    public static Object getSafeMethodValue(final Object target, Class<?> classObject, final String name) {
        return getMethodValue(target, findMethod(classObject, name));
    }

    /**
     * @param target
     * @param method
     * @return
     */
    public static Object getMethodValue(final Object target, final Method method) {
        return method == null ? null : invokeMethod(method, target);
    }

    /**
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Method getterMethod(final Class<?> clazz, final String fieldName) {
        return getterMethod(clazz, findField(clazz, fieldName));
    }

    /**
     * @param clazz
     * @param field
     * @return
     */
    public static Method getterMethod(final Class<?> clazz, final Field field) {
        return field != null ? findMethod(clazz, getterMethodName(field)) : null;
    }

    /**
     * @param clazz
     * @param fieldName
     * @return
     */
    public static Method setterMethod(final Class<?> clazz, final String fieldName) {
        return setterMethod(clazz, findField(clazz, fieldName));
    }

    /**
     * @param clazz
     * @param fieldName
     * @param clazzField
     * @return
     */
    public static Method setterMethod(final Class<?> clazz, final String fieldName, final Class<?> clazzField) {
        return setterMethod(clazz, findField(clazz, fieldName, clazzField));
    }

    /**
     * @param clazz
     * @param field
     * @return
     */
    public static Method setterMethod(final Class<?> clazz, final Field field) {
        return field != null ? findMethod(clazz, setterMethodName(field), field.getType()) : null;
    }

    private static String getterMethodName(Field field) {
        return getterMethodName(field.getName(), field.getType());
    }

    private static String getterMethodName(String field, Class<?> type) {
        return (boolean.class == type ? IS : GET) + Character.toUpperCase(field.charAt(0)) + field.substring(1);
    }

    private static String setterMethodName(Field field) {
        return setterMethodName(field.getName());
    }

    private static String setterMethodName(String field) {
        return SET + Character.toUpperCase(field.charAt(0)) + field.substring(1);
    }

    /**
     * @param method
     * @param target
     * @return
     */
    public static Object invokeMethod(Method method, Object target) {
        return invokeMethod(method, target, EMPTY_OBJECT_ARRAY);
    }

    /**
     * @param method
     * @param target
     * @param args
     * @return
     */
    public static Object invokeMethod(Method method, Object target, Object... args) {
        try {
            return method.invoke(target, args);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(ACCESS_ERROR_MESSAGE + e.getMessage());
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof RuntimeException) {
                throw (RuntimeException) e.getTargetException();
            }
            throw new UndeclaredThrowableException(e);
        }
    }

    /**
     * @param clazz
     * @param name
     * @return
     */
    public static Method findMethod(Class<?> clazz, String name) {
        return findMethod(clazz, name, EMPTY_CLASS_ARRAY);
    }

    /**
     * @param clazz
     * @param name
     * @param paramTypes
     * @return
     */
    public static Method findMethod(Class<?> clazz, String name, Class<?>... paramTypes) {
        Assert.notNull(clazz, "Class must not be null");
        Assert.notNull(name, "Method name must not be null");
        Class<?> searchType = clazz;
        while (searchType != null && searchType != Object.class) {
            try {
                return searchType.getDeclaredMethod(name, paramTypes);
            } catch (NoSuchMethodException e) { /* DO NOTHING */ }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    /**
     * @param obj
     * @param field
     * @return
     */
    public static boolean fieldValueIsNull(Object obj, Field field) {
        return getFieldValue(obj, field) == null;
    }

    /**
     * @param field
     * @return
     */
    public static boolean isJavaBasicType(Field field) {
        return field != null && (isJavaSimpleType(field.getType()) || isArraySimpleType(field.getType()));
    }

    /**
     * @param field
     * @return
     */
    public static boolean isJavaSimpleType(Field field) {
        return field != null && isJavaSimpleType(field.getType());
    }

    private static boolean isJavaSimpleType(Class<?> clazz) {
        return clazz != null && !ClassUtils.isCollection(clazz) && (isJavaType(clazz) || ClassUtils.isEnum(clazz));
    }

    private static boolean isJavaType(Class<?> clazz) {
        return clazz != null && (clazz.isPrimitive() 
                || Date.class.isAssignableFrom(clazz)
                || Integer.class.isAssignableFrom(clazz)
                || BigInteger.class.isAssignableFrom(clazz)
                || Short.class.isAssignableFrom(clazz)
                || Long.class.isAssignableFrom(clazz)
                || Float.class.isAssignableFrom(clazz)
                || Double.class.isAssignableFrom(clazz)
                || Boolean.class.isAssignableFrom(clazz)
                || Byte.class.isAssignableFrom(clazz)
                || Character.class.isAssignableFrom(clazz)
                || String.class.isAssignableFrom(clazz)
        );
    }

    private static boolean isArraySimpleType(Class<?> clazz) {
        if (!ClassUtils.isArray(clazz)) {
            return false;
        }
        Class<?> clazze = clazz.getComponentType();
        while (clazze.isArray()) {
            clazze = clazze.getComponentType();
        }
        return isJavaType(clazze) || ClassUtils.isEnum(clazze);
    }

    /**
     * @param field
     * @return
     */
    public static boolean isStatic(Field field) {
        return field != null && Modifier.isStatic(field.getModifiers());
    }

    /**
     * @param <T>
     * @param field
     * @param annotationClass
     * @return
     */
    public static <T extends Annotation> boolean isAnnotationPresent(Field field, Class<T> annotationClass) {
        return field != null && field.isAnnotationPresent(annotationClass);
    }

    /**
     * @param <T>
     * @param field
     * @param annotationClass
     * @return
     */
    public static <T extends Annotation> T getAnnotation(Field field, Class<T> annotationClass) {
        return field.getAnnotation(annotationClass);
    }

    /**
     * @param annotation
     * @param name
     * @return
     */
    public static Object getSafeAnnotationValue(Annotation annotation, String name) {
        try {
            return getAnnotationValue(annotation, name);
        } catch (ReflectiveOperationException e) {
            return null;
        }
    }

    /**
     * @param annotation
     * @param name
     * @return
     * @throws ReflectiveOperationException
     */
    public static Object getAnnotationValue(Annotation annotation, String name) throws ReflectiveOperationException {
        return annotation != null && StringUtils.hasLength(name) ? annotation.annotationType().getMethod(name).invoke(annotation) : null;
    }

    /**
     * @param clazz
     * @param name
     * @return
     * @throws ReflectiveOperationException
     */
    public static Field getField(Class<?> clazz, String name) throws ReflectiveOperationException {
        return clazz.getDeclaredField(name);
    }

    /**
     * @param <T>
     * @param clazz
     * @param annotationClass
     * @return
     */
    public static <T extends Annotation> Field getFirstFieldWithAnnotation(Class<?> clazz, Class<T> annotationClass) {
        for (Field field : clazz.getDeclaredFields()) {
            if (isAnnotationPresent(field, annotationClass)) {
                return field;
            }
        }
        return null;
    }

    /**
     * @param <T>
     * @param clazz
     * @param annotationClass
     * @param name
     * @param value
     * @return
     */
    public static <T extends Annotation> Field getFirstFieldWithAnnotationAndValue(Class<?> clazz, Class<T> annotationClass, String name, Object value) {
        for (Field field : clazz.getDeclaredFields()) {
            final T annotation = getAnnotation(field, annotationClass);
            if (annotation == null) {
                continue;
            }
            try {
                if (value.equals(getAnnotationValue(annotation, name))) {
                    return field;
                }
            } catch (ReflectiveOperationException e) { /* DO NOTHING */ }

        }
        return null;
    }

    /**
     * @param clazz
     * @return
     */
    public static boolean hasSuperClass(Class<?> clazz) {
        return clazz != null
                && clazz.getSuperclass() != null
                && !Object.class.equals(clazz.getSuperclass());
    }

    /**
     * @param object
     * @return
     */
    public static int countValueNotNull(Object object) {
        int res = 0;
        for (Field field: object.getClass().getDeclaredFields()) {
            res += getFieldValue(object, field) != null ? 1 : 0;
        }
        return res;
    }

    /**
     * @param object
     * @param nullValue
     * @param emptyValue
     * @return
     */
    public static Map<String, Object> toMap(Object object, boolean nullValue, boolean emptyValue) {
        return toMap(object, nullValue, emptyValue, null, null);
    }

    /**
     * @param object
     * @param nullValue
     * @param emptyValue
     * @param annotationNameClass
     * @param annotationNameField
     * @return
     */
    @SuppressWarnings("java:S1168")
    public static Map<String, Object> toMap(Object object, boolean nullValue, boolean emptyValue, Class<? extends Annotation> annotationNameClass, String annotationNameField) {
        if (object == null) {
            return null;
        }
        final Map<String, Object> result = new HashMap<>();
        toMap(result, object, object.getClass(), nullValue, emptyValue, annotationNameClass, annotationNameField);
        Class<?> clazz = object.getClass();
        while ((clazz = clazz.getSuperclass()) != Object.class) {
            toMap(result, object, clazz, nullValue, emptyValue, annotationNameClass, annotationNameField);
        }
        return result;
    }

    @SuppressWarnings("java:S1168")
    private static Map<String, Object> toMap(final Map<String, Object> result, Object object, Class<?> clazz, boolean nullValue, boolean emptyValue, Class<? extends Annotation> annotationNameClass, String annotationNameField) {
        if (object == null) {
            return null;
        }
        doWithFields(clazz, field -> {
            final Object value = getFieldValue(object, field);
            if ((nullValue || value != null)
                    && (emptyValue || !ObjectUtils.isEmpty(value))) {
                result.put(getName(field, annotationNameClass, annotationNameField), value);
            }
        });
        return result;
    }

    /**
     * @param clazz
     * @return
     */
    public static String[] getAllFieldsName(Class<?> clazz) {
        final List<String> fieldsName = new ArrayList<>();
        doWithFields(clazz, ReflectionUtils::getName);
        return fieldsName.toArray(new String[0]);
    }

    private static String getName(Field field) {
        return field.getName();
    }

    private static String getName(Field field, Class<? extends Annotation> annotationClass, String annotationField) {
        return (String) ObjectUtils.getOrDefault(getSafeAnnotationValue(field.getAnnotation(annotationClass), annotationField), field.getName());
    }

    /**
     * @param <T>
     * @param value
     * @param clazz
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T> T convert(Object value, Class<T> clazz) {
        if (value == null || clazz == null || clazz.isAssignableFrom(value.getClass())) {
            return (T) value;
        }
        if (clazz.isPrimitive()) {
            clazz = (Class<T>) ClassUtils.objectClass(clazz);
        }
        try {
            return clazz.getDeclaredConstructor(value.getClass()).newInstance(value);
        } catch (ReflectiveOperationException e) {
            try {
                return (T) clazz.getDeclaredMethod("valueOf", value.getClass());
            } catch (ReflectiveOperationException e1) {
                return null;
            }
        }
    }

    private static void doWithFields(Class<?> clazz, FieldCallback fc) {
        doWithFields(clazz, fc, null);
    }

    /**
     * @param clazz
     * @param fc
     * @param ff
     */
    public static void doWithFields(Class<?> clazz, FieldCallback fc, FieldFilter ff) {
        Class<?> targetClass = clazz;
        do {
            Field[] fields = targetClass.getDeclaredFields();
            for (Field field : fields) {
                if (ff != null && !ff.matches(field)) {
                    continue;
                }
                try {
                    fc.doWith(field);
                }
                catch (IllegalAccessException ex) {
                    throw new IllegalStateException("Not allowed to access field '" + field.getName() + "': " + ex);
                }
            }
            targetClass = targetClass.getSuperclass();
        }
        while (targetClass != null && targetClass != Object.class);
    }

    @FunctionalInterface
    private static interface FieldCallback {
        void doWith(Field field) throws IllegalArgumentException, IllegalAccessException;
    }

    @FunctionalInterface
    private static interface FieldFilter {
        boolean matches(Field field);
    }

}
