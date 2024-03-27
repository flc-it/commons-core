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
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.flcit.commons.core.file.util.FileUtils;
import org.flcit.commons.core.file.util.MediaType;
import org.flcit.commons.core.file.zip.exception.FileZipNotFoundException;
import org.flcit.commons.core.file.zip.exception.ZipBuildException;
import org.flcit.commons.core.file.zip.stream.ZipBomberCheckInputStream;
import org.flcit.commons.core.functional.consumer.ConsumerIOException;
import org.flcit.commons.core.util.StreamUtils;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class ZipUtils {

    private static final int BUFFER_SIZE = 16384;
    private static final int THRESHOLD_ENTRIES = 10000;
    private static final int THRESHOLD_SIZE = 1000000000; // 1 GB
    private static final double THRESHOLD_RATIO = 10;
    private static final String EXTENSION_ZIP = ".zip";

    private ZipUtils() { }

    /**
     * @param source
     * @return
     */
    public static boolean isZip(Path source) {
        return source != null && isZip(source.getFileName().toString());
    }

    private static boolean isZip(String filename) {
        return filename != null && filename.toLowerCase().endsWith(EXTENSION_ZIP);
    }

    /**
     * @param filename
     * @param contentType
     * @return
     */
    public static boolean isZip(String filename, String contentType) {
        return isZip(filename) || isZipByContentType(contentType);
    }

    private static boolean isZipByContentType(String contentType) {
        return MediaType.APPLICATION_ZIP_VALUE.equals(contentType);
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
            if (Files.isDirectory(target)) {
                if (replaceIfExisting) {
                    FileUtils.deleteRecursively(target);
                } else {
                    throw new FileAlreadyExistsException(target.toString());
                }
            }
            Files.createDirectories(target);
            unzip(is, zipEntry -> {
                Path file = newFile(target, zipEntry.getKey());
                if (zipEntry.getKey().isDirectory()) {
                    Files.createDirectories(file);
                } else {
                    // fix for Windows-created archives
                    Path parent = file.getParent();
                    if (!Files.isDirectory(parent)) {
                        Files.createDirectories(parent);
                    }
                    try (OutputStream os = Files.newOutputStream(file, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
                        StreamUtils.copy(zipEntry.getValue(), os, BUFFER_SIZE);
                    }
                }
            });
            return target;
        } catch (Exception e) {
            FileUtils.deleteRecursively(target);
            throw e;
        }
    }

    /**
     * @param is
     * @return
     * @throws IOException
     */
    public static Entry<ZipEntry, InputStream> unzipFirstFile(InputStream is) throws IOException {
        return unzipFirstFile(is, null);
    }

    /**
     * @param is
     * @param filter
     * @return
     * @throws IOException
     */
    public static Entry<ZipEntry, InputStream> unzipFirstFileOnName(InputStream is, Predicate<String> filter) throws IOException {
        return unzipFirstFile(is, e -> filter.test(e.getName()));
    }

    /**
     * @param is
     * @param filter
     * @return
     * @throws IOException
     */
    @SuppressWarnings("java:S2093")
    public static Entry<ZipEntry, InputStream> unzipFirstFile(InputStream is, Predicate<ZipEntry> filter) throws IOException {
        SimpleEntry<ZipEntry, InputStream> entry = null;
        ZipInputStream zis = null;
        try {
            zis = getZipBomberCheckInputStream(is);
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                if (!zipEntry.isDirectory()
                        && (filter == null || filter.test(zipEntry))) {
                    entry = new SimpleEntry<>(zipEntry, zis);
                    break;
                }
            }
        } finally {
            if (entry == null
                    && zis != null) {
                zis.close();
            }
        }
        if (entry != null) {
            return entry;
        } else {
            throw new FileZipNotFoundException();
        }
    }

    /**
     * @param is
     * @param consumer
     * @throws IOException
     */
    public static void unzipConsumeFirstFile(InputStream is, ConsumerIOException<Entry<ZipEntry, InputStream>> consumer) throws IOException {
        unzip(is, consumer, null);
    }

    /**
     * @param is
     * @param consumer
     * @param filter
     * @throws IOException
     */
    public static void unzipFirstFile(InputStream is, ConsumerIOException<Entry<ZipEntry, InputStream>> consumer, Predicate<ZipEntry> filter) throws IOException {
        final Entry<ZipEntry, InputStream> entry = unzipFirstFile(is, filter);
        try {
            consumer.accept(entry);
        } finally {
            entry.getValue().close();
        }
    }

    /**
     * @param is
     * @param consumer
     * @throws IOException
     */
    public static void unzipFiles(InputStream is, ConsumerIOException<Entry<ZipEntry, InputStream>> consumer) throws IOException {
        unzip(is, consumer, entry -> !entry.isDirectory());
    }

    /**
     * @param is
     * @param consumer
     * @throws IOException
     */
    public static void unzip(InputStream is, ConsumerIOException<Entry<ZipEntry, InputStream>> consumer) throws IOException {
        unzip(is, consumer, null);
    }

    /**
     * @param is
     * @param consumer
     * @param filter
     * @throws IOException
     */
    public static void unzip(InputStream is, ConsumerIOException<Entry<ZipEntry, InputStream>> consumer, Predicate<ZipEntry> filter) throws IOException {
        try (ZipInputStream zis = getZipBomberCheckInputStream(is)) {
            ZipEntry zipEntry;
            while ((zipEntry = zis.getNextEntry()) != null) {
                if (filter == null || filter.test(zipEntry)) {
                    consumer.accept(new SimpleEntry<>(zipEntry, zis));
                }
            }
        }
    }

    /**
     * @param source
     * @param os
     * @throws IOException
     */
    public static void zip(final Path source, final OutputStream os) throws IOException {
        try (ZipOutputStream zs = new ZipOutputStream(os)) {
            try (Stream<Path> walk = Files.walk(source)) {
                walk.filter(path -> !Files.isDirectory(path))
                .forEach(path -> {
                    ZipEntry zipEntry = new ZipEntry(source.relativize(path).toString());
                    try {
                        zs.putNextEntry(zipEntry);
                        if (!Files.isDirectory(path)) {
                            Files.copy(path, zs);
                        }
                        zs.closeEntry();
                    } catch (IOException e) {
                        throw new ZipBuildException(e);
                    }
                });
            }
        }
    }

    private static Path newFile(Path destination, ZipEntry zipEntry) throws IOException {
        Path newFile = destination.resolve(zipEntry.getName());
        // Avoid zip slip vulnerability => https://snyk.io/research/zip-slip-vulnerability
        if (!newFile.startsWith(destination)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }
        return newFile;
    }

    /**
     * @param is
     * @return
     */
    public static ZipInputStream getZipBomberCheckInputStream(InputStream is) {
        return getZipBomberCheckInputStream(is, THRESHOLD_ENTRIES, THRESHOLD_SIZE, THRESHOLD_RATIO);
    }

    /**
     * @param is
     * @param thresholdEntries
     * @param thresholdSize
     * @param thresholdRatio
     * @return
     */
    public static ZipInputStream getZipBomberCheckInputStream(InputStream is, int thresholdEntries, int thresholdSize, double thresholdRatio) {
        return new ZipBomberCheckInputStream(is, thresholdEntries, thresholdSize, thresholdRatio);
    }

}
