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

import java.math.BigInteger;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class MathUtils {

    private MathUtils() { }

    /**
     * @param n1
     * @param n2
     * @return
     */
    public static BigInteger add(BigInteger n1, BigInteger n2) {
        if (n1 == null) {
            return n2;
        }
        return n2 == null ? n1 : n1.add(n2);
    }

}
