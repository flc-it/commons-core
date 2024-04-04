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

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class PropertyUtils {

    private PropertyUtils() { }

    /**
     * @param properties
     * @param property
     * @return
     */
    public static Boolean getBoolean(Properties properties, String property) {
        return toBoolean(properties.get(property));
    }

    /**
     * @param properties
     * @param property
     * @param defaultValue
     * @return
     */
    public static Boolean getBoolean(Properties properties, String property, Boolean defaultValue) {
        return toBoolean(properties.getOrDefault(property, defaultValue));
    }

    private static Boolean toBoolean(Object value) {
        return BooleanUtils.convert(value);
    }

    /**
     * @param <T>
     * @param properties
     * @param property
     * @param classType
     * @return
     */
    public static <T extends Number> T getNumber(Properties properties, String property, Class<T> classType) {
        return toNumber(properties.get(property), classType);
    }

    /**
     * @param <T>
     * @param properties
     * @param property
     * @param defaultValue
     * @param classType
     * @return
     */
    public static <T extends Number> T getNumber(Properties properties, String property, T defaultValue, Class<T> classType) {
        return toNumber(properties.getOrDefault(property, defaultValue), classType);
    }

    private static <T extends Number> T toNumber(Object value, Class<T> classType) {
        return NumberUtils.convert(value, classType);
    }

    /**
     * @param properties
     * @param property
     * @return
     */
    public static List<String> getStringList(Properties properties, String property) {
        return getStringList(properties, property, null);
    }

    /**
     * @param properties
     * @param property
     * @param defaultValue
     * @return
     */
    public static List<String> getStringList(Properties properties, String property, List<String> defaultValue) {
        return toList(properties.getProperty(property), defaultValue);
    }

    /**
     * @param value
     * @return
     */
    public static List<String> toList(String value) {
        return toList(value, null);
    }

    /**
     * @param value
     * @param defaultValue
     * @return
     */
    public static List<String> toList(String value, List<String> defaultValue) {
        return value != null ? Arrays.asList(toArray(value)) : defaultValue;
    }

    /**
     * @param properties
     * @param property
     * @return
     */
    public static String[] getStringArray(Properties properties, String property) {
        return getStringArray(properties, property, null);
    }

    /**
     * @param properties
     * @param property
     * @param defaultValue
     * @return
     */
    public static String[] getStringArray(Properties properties, String property, String[] defaultValue) {
        return toArray(properties.getProperty(property), defaultValue);
    }

    private static String[] toArray(String value) {
        return toArray(value, null);
    }

    /**
     * @param value
     * @param defaultValue
     * @return
     */
    private static String[] toArray(String value, String[] defaultValue) {
        return value != null ? value.split(StringUtils.COMMA) : defaultValue;
    }

    /**
     * @param path
     * @return
     * @throws IOException
     */
    public static Map<String, String> load(Path path) throws IOException {
        try (InputStream inputStream = Files.newInputStream(path)) {
            return load(inputStream);
        }
    }

    /**
     * @param inputStream
     * @return
     * @throws IOException
     */
    @SuppressWarnings("java:S1168")
    public static Map<String, String> load(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return null;
        }
        final Properties properties = new Properties();
        properties.load(inputStream);
        if (properties.isEmpty()) {
            return null;
        }
        final Map<String, String> propertyMap = new HashMap<>(properties.size());
        for (String propertyName : properties.stringPropertyNames()) {
            propertyMap.put(propertyName, properties.getProperty(propertyName));
        }
        return propertyMap;
    }

}
