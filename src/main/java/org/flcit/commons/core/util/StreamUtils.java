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

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class StreamUtils {

    private StreamUtils() { }

    /**
     * Copy the contents of the given InputStream to the given OutputStream.
     * <p>Leaves both streams open when done.
     * @param in the InputStream to copy from
     * @param out the OutputStream to copy to
     * @param bufferSize the buffer size to use
     * @return the number of bytes copied
     * @throws IOException in case of I/O errors
     */
    public static int copy(InputStream in, OutputStream out, int bufferSize) throws IOException {
        int byteCount = 0;
        byte[] buffer = new byte[bufferSize];
        int bytesRead;
        while ((bytesRead = in.read(buffer)) != -1) {
            out.write(buffer, 0, bytesRead);
            byteCount += bytesRead;
        }
        out.flush();
        return byteCount;
    }

}
