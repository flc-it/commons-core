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

package org.flcit.commons.core.executor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class SingleTaskThreadExecutor implements Executor {

    private boolean execute;
    private final ExecutorService executorService = Executors.newFixedThreadPool(1);

    /**
     * Executes the given command at some time in the future.
     * <p>ExecutorService & Single Thread Pool is immediately shutdown after the single task.
     *
     * @param command the runnable task
     * @throws RejectedExecutionException if this task cannot be
     * accepted for execution
     * @throws NullPointerException if command is null
     * @see ExecutorService#execute(Runnable)
     * @see Executors#newFixedThreadPool(int) initialized with 1 nThreads
     */
    @Override
    public void execute(Runnable command) {
        if (this.execute) {
            throw new IllegalStateException("Task already executed !");
        }
        this.execute = true;
        try {
            executorService.execute(command);
        } finally {
            executorService.shutdown();
        }
    }

    /**
     * Force to shutdown the executor
     */
    @SuppressWarnings({ "java:S1113", "java:S1874" })
    @Override
    protected void finalize() {
        executorService.shutdown();
    }

}
