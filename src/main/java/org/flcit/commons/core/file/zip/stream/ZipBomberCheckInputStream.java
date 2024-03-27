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

package org.flcit.commons.core.file.zip.stream;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.flcit.commons.core.file.zip.exception.ZipBomberException;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public class ZipBomberCheckInputStream extends ZipInputStream {

    private int totalSize = 0;
    private int totalEntry = 0;
    private ZipEntry zipEntry;
    private int thresholdEntries = 1000;
    private int thresholdSize = 100000000; // 100mo
    private double thresholdRatio = 10;

    /**
     * @param in
     */
    public ZipBomberCheckInputStream(InputStream in) {
        super(in);
    }

    /**
     * @param in
     * @param charset
     */
    public ZipBomberCheckInputStream(InputStream in, Charset charset) {
        super(in, charset);
    }

    /**
     * @param in
     * @param thresholdEntries
     * @param thresholdSize
     * @param thresholdRatio
     */
    public ZipBomberCheckInputStream(InputStream in, int thresholdEntries, int thresholdSize, double thresholdRatio) {
        super(in);
        this.thresholdEntries = thresholdEntries;
        this.thresholdSize = thresholdSize;
        this.thresholdRatio = thresholdRatio;
        this.asserts();
    }

    /**
     * @param in
     * @param charset
     * @param thresholdEntries
     * @param thresholdSize
     * @param thresholdRatio
     */
    public ZipBomberCheckInputStream(InputStream in, Charset charset, int thresholdEntries, int thresholdSize, double thresholdRatio) {
        super(in, charset);
        this.thresholdEntries = thresholdEntries;
        this.thresholdSize = thresholdSize;
        this.thresholdRatio = thresholdRatio;
        this.asserts();
    }

    private void asserts() {
        assert(this.thresholdEntries < 100000);
        assert(this.thresholdSize < 1000000000);
        assert(this.thresholdRatio < 20);
    }

    @Override
    public ZipEntry getNextEntry() throws IOException {
        this.zipEntry = super.getNextEntry();
        if (this.zipEntry != null
                // Validation of the number of entries in the archive
                && ++totalEntry > thresholdEntries) {
            throw new ZipBomberException();
        }
        return this.zipEntry;
    }

    @Override
    public void closeEntry() throws IOException {
        super.closeEntry();
        this.zipEntry = null;
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        final int res = super.read(b, off, len);
        if (res > 0) {
            this.totalSize += res;
            // Validation of the total size of the uncompressed data
            if (this.totalSize > this.thresholdSize) {
                throw new ZipBomberException();
            }
        } else if (res == -1
                // Validation of the ratio between the compressed and uncompressed archive entry
                && this.zipEntry.getSize() / this.zipEntry.getCompressedSize() > this.thresholdRatio) {
            throw new ZipBomberException();
        }
        return res;
    }

}