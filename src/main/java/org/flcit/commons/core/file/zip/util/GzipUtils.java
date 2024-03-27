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

package org.flcit.commons.core.file.zip.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;

import org.flcit.commons.core.file.util.FileUtils;
import org.flcit.commons.core.file.util.MediaType;
import org.flcit.commons.core.util.StreamUtils;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class GzipUtils {

    private static final int BUFFER_SIZE = 16384;
    private static final String EXTENSION_GZIP = ".gz";

    private GzipUtils() { }

    /**
     * @param source
     * @return
     */
    public static boolean isGzip(Path source) {
        return source != null && isGzip(source.getFileName().toString());
    }

    private static boolean isGzip(String filename) {
        return filename != null && filename.toLowerCase().endsWith(EXTENSION_GZIP);
    }

    /**
     * @param filename
     * @param contentType
     * @return
     */
    public static boolean isGzip(String filename, String contentType) {
        return isGzip(filename) || isGzipByContentType(contentType);
    }

    private static boolean isGzipByContentType(String contentType) {
        return MediaType.APPLICATION_GZIP_VALUE.equals(contentType);
    }

    /**
     * @param source
     * @return
     * @throws IOException
     */
    public static Path unzip(Path source) throws IOException {
        return unzip(source, source.resolveSibling(FileUtils.removeExtension(source.getFileName().toString())), true);
    }

    /**
     * @param source
     * @param target
     * @param replaceIfExisting
     * @return
     * @throws IOException
     */
    public static Path unzip(Path source, Path target, boolean replaceIfExisting) throws IOException {
        if (!Files.isRegularFile(source)) {
            return null;
        }
        try (InputStream is = Files.newInputStream(source)) {
            return unzip(is, target, replaceIfExisting);
        }
    }

    /**
     * @param is
     * @param target
     * @param replaceIfExisting
     * @return
     * @throws IOException
     */
    public static Path unzip(InputStream is, Path target, boolean replaceIfExisting) throws IOException {
        try {
            if (replaceIfExisting) {
                Files.deleteIfExists(target);
            }
            try (GZIPInputStream gis = new GZIPInputStream(is, BUFFER_SIZE)) {
                try (OutputStream os = Files.newOutputStream(target)) {
                    StreamUtils.copy(gis, os, BUFFER_SIZE);
                }
            }
            return target;
        } catch (Exception e) {
            Files.deleteIfExists(target);
            throw e;
        }
    }

}
