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

package org.flcit.commons.core.exception;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public abstract class BasicRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private final String code;

    /**
     * @param message
     */
    protected BasicRuntimeException(String message) {
        this(null, message);
    }

    /**
     * @param code
     * @param message
     */
    protected BasicRuntimeException(String code, String message) {
        super(message);
        this.code = code;
    }

    /**
     * @param cause
     */
    protected BasicRuntimeException(Throwable cause) {
        super(cause);
        this.code = null;
    }

    /**
     * @param message
     * @param cause
     */
    protected BasicRuntimeException(String message, Throwable cause) {
        this(null, message, cause);
    }

    /**
     * @param code
     * @param message
     * @param cause
     */
    protected BasicRuntimeException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    /**
     * @return
     */
    public String getCode() {
        return code;
    }

}
