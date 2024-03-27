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

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class SystemPropertyUtils {

    private SystemPropertyUtils() { }

    /**
     * @param <T>
     * @param property
     * @param targetType
     * @return
     */
    public static <T extends Enum<T>> T getEnum(String property, Class<T> targetType) {
        final String value = get(property);
        return !StringUtils.hasLength(value) ? null : Enum.valueOf(targetType, value);
    }

    /**
     * @param property
     * @return
     */
    public static String get(String property) {
        return get(property, null);
    }

    /**
     * @param property
     * @param defaultIfNotExists
     * @return
     */
    public static String get(String property, String defaultIfNotExists) {
        final String value = System.getProperty(property);
        return !StringUtils.hasLength(value) ? defaultIfNotExists : value;
    }

    /**
     * @param property
     * @return
     */
    public static String[] getArray(String property) {
        final String value = get(property);
        return value != null ? value.split(",") : null;
    }

    /**
     * @param property
     * @param defaultIfNotExists
     * @return
     */
    public static boolean getBoolean(String property, boolean defaultIfNotExists) {
        final String value = get(property);
        return !StringUtils.hasLength(value) ? defaultIfNotExists : "true".equalsIgnoreCase(value);
    }

    /**
     * @param property
     * @param defaultIfNotExists
     * @return
     */
    public static Long getLong(String property, Long defaultIfNotExists) {
        final String value = get(property);
        return !StringUtils.hasLength(value) ? defaultIfNotExists : Long.valueOf(value);
    }

    /**
     * @param property
     * @param defaultIfNotExists
     * @return
     */
    public static Integer getInteger(String property, Integer defaultIfNotExists) {
        final String value = get(property);
        return !StringUtils.hasLength(value) ? defaultIfNotExists : Integer.valueOf(value);
    }

}
