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

package org.flcit.commons.core.functional.function;

import java.io.IOException;
import java.util.Objects;

/**
 * @param <T>
 * @param <R>
 * @since 
 * @author Florian Lestic
 */
@FunctionalInterface
public interface FunctionIOException<T, R> {

    /**
     * @param t
     * @return
     * @throws IOException
     */
    R apply(T t) throws IOException;

    /**
     * @param <V>
     * @param before
     * @return
     * @throws IOException
     */
    default <V> FunctionIOException<V, R> compose(FunctionIOException<? super V, ? extends T> before) throws IOException {
        Objects.requireNonNull(before);
        return (V v) -> apply(before.apply(v));
    }

    /**
     * @param <V>
     * @param after
     * @return
     */
    default <V> FunctionIOException<T, V> andThen(FunctionIOException<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t) -> after.apply(apply(t));
    }

    /**
     * @param <T>
     * @return
     */
    static <T> FunctionIOException<T, T> identity() {
        return t -> t;
    }

}
