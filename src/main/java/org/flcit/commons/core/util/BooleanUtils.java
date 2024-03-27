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

import java.util.function.Supplier;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class BooleanUtils {

    private BooleanUtils() { }

    /**
     * @param value
     * @return
     */
    public static boolean isTrue(Boolean value) {
        return Boolean.TRUE.equals(value);
    }

    /**
     * @param value
     * @return
     */
    public static boolean isFalse(Boolean value) {
        return value == null || Boolean.FALSE.equals(value);
    }

    /**
     * @param value
     * @return
     */
    public static boolean isFalse(Supplier<Boolean> value) {
        return value == null || isFalse(FunctionUtils.resolve(value));
    }

    /**
     * @param value
     * @return
     */
    public static boolean isTrue(Supplier<Boolean> value) {
        return value != null && isTrue(FunctionUtils.resolve(value));
    }

    /**
     * @param value
     * @param defaultValue
     * @return
     */
    @SuppressWarnings("java:S4276")
    public static boolean isTrueOrNullAndTrue(final Supplier<Boolean> value, final Supplier<Boolean> defaultValue) {
        return isTrueOrNullAndTrue(value.get(), defaultValue.get());
    }

    /**
     * @param value
     * @param defaultValue
     * @return
     */
    public static boolean isTrueOrNullAndTrue(final Boolean value, final Boolean defaultValue) {
        return BooleanUtils.isTrue(value)
                || (value == null && BooleanUtils.isTrue(defaultValue));
    }

    /**
     * @param values
     * @return
     */
    @SuppressWarnings("java:S4276")
    @SafeVarargs
    public static boolean hasOneTrue(Supplier<Boolean>... values) {
        if (!ArrayUtils.isEmpty(values)) {
            for (Supplier<Boolean> value: values) {
                if (isTrue(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param values
     * @return
     */
    public static boolean hasOneTrue(Boolean... values) {
        if (!ArrayUtils.isEmpty(values)) {
            for (Boolean value: values) {
                if (isTrue(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * @param value
     * @return
     */
    @SuppressWarnings("java:S2447")
    public static Boolean parse(String value) {
        if (!StringUtils.hasLength(value)) {
            return null;
        }
        if (value.equalsIgnoreCase(Boolean.FALSE.toString())) {
            return Boolean.FALSE;
        }
        return value.equalsIgnoreCase(Boolean.TRUE.toString()) ? Boolean.TRUE : null;
    }

}
