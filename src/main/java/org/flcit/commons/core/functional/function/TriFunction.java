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

import java.util.Objects;
import java.util.function.Function;

/**
 * @param <T>
 * @param <U>
 * @param <Z>
 * @param <R>
 * @since 
 * @author Florian Lestic
 */
@FunctionalInterface
public interface TriFunction<T, U, Z, R> {

    /**
     * Applies this function to the given arguments.
     *
     * @param t the first function argument
     * @param u the second function argument
     * @param z the third function argument
     * @return the function result
     */
    /**
     * @param t
     * @param u
     * @param z
     * @return
     */
    R apply(T t, U u, Z z);

    /**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     *
     * @param <V> the type of output of the {@code after} function, and of the
     *           composed function
     * @param after the function to apply after this function is applied
     * @return a composed function that first applies this function and then
     * applies the {@code after} function
     * @throws NullPointerException if after is null
     */
    /**
     * @param <V>
     * @param after
     * @return
     */
    default <V> TriFunction<T, U, Z, V> andThen(Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (T t, U u, Z z) -> after.apply(apply(t, u, z));
    }

}

