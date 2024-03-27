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

package org.flcit.commons.core.functional.callable;

import java.util.concurrent.Callable;

/**
 * @param <T>
 * @since 
 * @author Florian Lestic
 */
public class CallableThrowable<T> implements Callable<T> {

    private final CallableThrowableI<T> callable;

    /**
     * @param callable
     */
    public CallableThrowable(CallableThrowableI<T> callable) {
        this.callable = callable;
    }

    @Override
    public T call() throws Exception {
        try {
            return this.callable.call();
        } catch (Error e) {
            final Exception exc = new Exception(e.getMessage(), e);
            exc.setStackTrace(e.getStackTrace());
            throw exc;
        } catch (Throwable e) {
            throw (Exception) e;
        }
    }

    @FunctionalInterface
    public static interface CallableThrowableI<V> {
        @SuppressWarnings("java:S112")
        V call() throws Throwable;
    }

}
