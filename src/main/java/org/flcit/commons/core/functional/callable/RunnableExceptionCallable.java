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

import org.flcit.commons.core.functional.runnable.RunnableException;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public class RunnableExceptionCallable implements Callable<Void> {

    private final RunnableException runnable;

    /**
     * @param runnable
     */
    public RunnableExceptionCallable(RunnableException runnable) {
        this.runnable = runnable;
    }

    @Override
    public Void call() throws Exception {
        this.runnable.run();
        return null;
    }

}
