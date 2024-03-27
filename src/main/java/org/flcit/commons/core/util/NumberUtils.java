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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class NumberUtils {

    /**
     * 
     */
    public static final Integer INTEGER_0 = 0;
    /**
     * 
     */
    public static final Integer INTEGER_1 = 1;
    /**
     * 
     */
    public static final Integer INTEGER_10 = 10;
    /**
     * 
     */
    public static final Integer INTEGER_100 = 100;
    /**
     * 
     */
    public static final Integer INTEGER_1000 = 1000;

    private static final String DOUBLE_CLASS = "java.lang.Double";
    private static final String FLOAT_CLASS = "java.lang.Float";
    private static final String INTEGER_CLASS = "java.lang.Integer";
    private static final String LONG_CLASS = "java.lang.Long";
    private static final String SHORT_CLASS = "java.lang.Short";
    private static final String BIGINTEGER_CLASS = "java.math.BigInteger";
    private static final String BIGDECIMAL_CLASS = "java.math.BigDecimal";

    private NumberUtils() { }

    /**
     * @param value
     * @return
     */
    public static double round(double value) {
        return round(value, 2);
    }

    private static double round(double value, int decimal) {
        int m = 1;
        for (int i = 0; i < decimal; i++) {
            m *= 10;
        }
        return Math.round(value * m) / (double) m;
    }

    /**
     * @param value
     * @return
     */
    public static double doubleValue(Number value) {
        return value == null ? 0 : value.doubleValue();
    }

    /**
     * @param value
     * @return
     */
    public static long longValue(Number value) {
        return value == null ? 0 : value.longValue();
    }

    /**
     * @param value
     * @return
     */
    public static BigInteger toBigInteger(Long value) {
        return value == null ? null : BigInteger.valueOf(value);
    }

    /**
     * @param value
     * @return
     */
    public static BigInteger toBigInteger(String value) {
        return value == null ? null : new BigInteger(value);
    }

    /**
     * @param value
     * @return
     */
    public static Integer toInteger(Number value) {
        return value == null ? null : value.intValue();
    }

    /**
     * @param value
     * @return
     */
    public static Long toLong(Number value) {
        return value == null ? null : value.longValue();
    }

    /**
     * @param value
     * @return
     */
    public static Long toLong(Optional<Integer> value) {
        return toLong(OptionalUtils.value(value));
    }

    /**
     * @param <T>
     * @param value
     * @param targetClass
     * @return
     */
    public static <T extends Number> T convertSafe(String value, Class<T> targetClass) {
        try {
            return convert(value, targetClass);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * @param <T>
     * @param value
     * @param targetClass
     * @return
     */
    public static <T extends Number> T convert(String value, Class<T> targetClass) {
        return !StringUtils.hasLength(value) ? null : parseNumber(value, targetClass);
    }

    /**
     * @param <T>
     * @param value
     * @param targetClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number> T convertIndependant(String value, Class<T> targetClass) {
        if (value == null
                || value.isEmpty()) {
            return null;
        }
        switch (targetClass.getName()) {
        case DOUBLE_CLASS:
            return (T) Double.valueOf(value);
        case FLOAT_CLASS:
            return (T) Float.valueOf(value);
        case INTEGER_CLASS:
            return (T) Integer.valueOf(value);
        case LONG_CLASS:
            return (T) Long.valueOf(value);
        case SHORT_CLASS:
            return (T) Short.valueOf(value);
        case BIGINTEGER_CLASS:
            return (T) new BigInteger(value);
        case BIGDECIMAL_CLASS:
            return (T) new BigDecimal(value);
        default : return null;
        }
    }

    /**
     * @param <T>
     * @param value
     * @param targetClass
     * @return
     */
    @SuppressWarnings("unchecked")
    public static <T extends Number> T convert(Number value, Class<T> targetClass) {
        if (value == null
                || targetClass.isAssignableFrom(value.getClass())) {
            return (T) value;
        }
        switch (targetClass.getName()) {
        case DOUBLE_CLASS:
            return (T) (Double) value.doubleValue();
        case FLOAT_CLASS:
            return (T) (Float) value.floatValue();
        case INTEGER_CLASS:
            return (T) (Integer) value.intValue();
        case LONG_CLASS:
            return (T) (Long) value.longValue();
        case SHORT_CLASS:
            return (T) (Short) value.shortValue();
        case BIGINTEGER_CLASS:
            return (T) BigInteger.valueOf(value.longValue());
        case BIGDECIMAL_CLASS:
            return (T) BigDecimal.valueOf(value.doubleValue());
        default : return null;
        }
    }

    private static boolean isHexNumber(String value) {
        int index = (value.startsWith("-") ? 1 : 0);
        return (value.startsWith("0x", index) || value.startsWith("0X", index) || value.startsWith("#", index));
    }

    @SuppressWarnings({ "unchecked", "java:S3776" })
    private static <T extends Number> T parseNumber(String text, Class<T> targetClass) {
        Assert.notNull(text, "Text must not be null");
        Assert.notNull(targetClass, "Target class must not be null");
        String trimmed = StringUtils.noWhitespace(text);

        if (Byte.class == targetClass) {
            return (T) (isHexNumber(trimmed) ? Byte.decode(trimmed) : Byte.valueOf(trimmed));
        }
        else if (Short.class == targetClass) {
            return (T) (isHexNumber(trimmed) ? Short.decode(trimmed) : Short.valueOf(trimmed));
        }
        else if (Integer.class == targetClass) {
            return (T) (isHexNumber(trimmed) ? Integer.decode(trimmed) : Integer.valueOf(trimmed));
        }
        else if (Long.class == targetClass) {
            return (T) (isHexNumber(trimmed) ? Long.decode(trimmed) : Long.valueOf(trimmed));
        }
        else if (BigInteger.class == targetClass) {
            return (T) (isHexNumber(trimmed) ? decodeBigInteger(trimmed) : new BigInteger(trimmed));
        }
        else if (Float.class == targetClass) {
            return (T) Float.valueOf(trimmed);
        }
        else if (Double.class == targetClass) {
            return (T) Double.valueOf(trimmed);
        }
        else if (BigDecimal.class == targetClass || Number.class == targetClass) {
            return (T) new BigDecimal(trimmed);
        }
        throw new IllegalArgumentException(
                "Cannot convert String [" + text + "] to target class [" + targetClass.getName() + "]");
    }

    private static BigInteger decodeBigInteger(String value) {
        int radix = 10;
        int index = 0;
        boolean negative = false;

        // Handle minus sign, if present.
        if (value.startsWith("-")) {
            negative = true;
            index++;
        }

        // Handle radix specifier, if present.
        if (value.startsWith("0x", index) || value.startsWith("0X", index)) {
            index += 2;
            radix = 16;
        }
        else if (value.startsWith("#", index)) {
            index++;
            radix = 16;
        }
        else if (value.startsWith("0", index) && value.length() > 1 + index) {
            index++;
            radix = 8;
        }

        BigInteger result = new BigInteger(value.substring(index), radix);
        return (negative ? result.negate() : result);
    }

}
