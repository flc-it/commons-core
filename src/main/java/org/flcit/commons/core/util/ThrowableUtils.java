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
public final class ThrowableUtils {

    private ThrowableUtils() { }

    /**
     * @param <T>
     * @param active
     * @param throwable
     * @param message
     * @throws T
     */
    @SuppressWarnings("java:S1874")
    public static <T extends Throwable> void throwable(final boolean active, final Class<T> throwable, final String message) throws T {
        if (!active || throwable == null) {
            return;
        }
        try {
            throw StringUtils.hasLength(message) ? throwable.getDeclaredConstructor(String.class).newInstance(message) : throwable.newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(message, e);
        }
    }

}
