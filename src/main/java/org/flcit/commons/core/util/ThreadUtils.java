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

import java.util.concurrent.TimeUnit;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class ThreadUtils {

    private ThreadUtils() { }

    /**
     * @param threads
     * @return
     */
    public static boolean oneIsAlive(Thread... threads) {
        if (ArrayUtils.isEmpty(threads)) {
            return false;
        }
        for (Thread t : threads) {
            if (t.isAlive()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param threads
     */
    public static void sleepUntilAlive(Thread... threads) {
        if (ArrayUtils.isEmpty(threads)) {
            return;
        }
        while (ThreadUtils.oneIsAlive(threads)) {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    /**
     * @param millis
     */
    public static void sleep(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
