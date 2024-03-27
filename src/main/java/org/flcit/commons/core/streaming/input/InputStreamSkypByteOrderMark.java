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

package org.flcit.commons.core.streaming.input;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.flcit.commons.core.file.util.FileByteOrderMarkUtils;
import org.flcit.commons.core.util.CollectionUtils;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public class InputStreamSkypByteOrderMark extends InputStream {

    private boolean first = true;
    private List<Integer> buffer;
    private final InputStream inputStream;

    /**
     * @param inputStream
     */
    public InputStreamSkypByteOrderMark(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public int read() throws IOException {
        if (first) {
            final int cacheResult = cacheFirstBytes();
            if (cacheResult != -2) {
                return cacheResult;
            }
        }
        return CollectionUtils.isEmpty(this.buffer) ? inputStream.read() : nextByteInBuffer();
    }

    private int nextByteInBuffer() {
        final int i = buffer.remove(0);
        if (CollectionUtils.isEmpty(buffer)) {
            this.buffer = null;
        }
        return i;
    }

    private int cacheFirstBytes() throws IOException {
        final List<Integer> buff = new ArrayList<>(4);
        int order = 0;
        int res;
        while ((res = buffOrSend(buff, order++)) == -2 && buff.size() < 4);
        first = false;
        return res;
    }

    private final int buffOrSend(final List<Integer> buff, final int order) throws IOException {
        final int i = inputStream.read();
        if (i == -1
                || !FileByteOrderMarkUtils.isByteOrderMarkSequence(i, order)) {
            if (CollectionUtils.isEmpty(buff)
                    || FileByteOrderMarkUtils.isByteOrderMarkSequence(CollectionUtils.toArrayNative(buff))) {
                return i;
            } else {
                this.buffer.add(i);
                this.buffer = buff;
                return this.buffer.remove(0);
            }
        } else {
            buff.add(i);
            return -2;
        }
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if (off < 0 || len < 0 || len > b.length - off) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return 0;
        }
        int lenOrigin = len;
        if (first) {
            final int res = read();
            if (res == -1) {
                return -1;
            } else {
                b[off++] = (byte) res;
                len--;
            }
        }
        while (!CollectionUtils.isEmpty(this.buffer) && len-- > 0) {
            final int res = read();
            b[off++] = (byte) res;
            len--;
        }
        if (len == lenOrigin) {
            return this.inputStream.read(b, off, len);
        } else if (len > 0) {
            final int nb = this.inputStream.read(b, off, len);
            return nb == -1 ? (lenOrigin - len) : (lenOrigin - len) + nb;
        } else {
            return lenOrigin - len;
        }
    }

}
