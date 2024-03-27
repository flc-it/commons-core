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

package org.flcit.commons.core.file.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;

import org.flcit.commons.core.util.StringUtils;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class FileUtils {

    private static final String EXTENSION_TEMP = ".tmp";

    private FileUtils() { }

    /**
     * @param filename
     * @return
     */
    public static String getContentType(String filename) {
        return ContentTypeUtils.get(filename);      
    }

    /**
     * @param filename
     * @param maxLength
     * @return
     */
    public static String limitLength(String filename, int maxLength) {
        if (filename == null || filename.length() <= maxLength) {
            return filename;
        }
        final int i = filename.lastIndexOf('.');
        if (i == -1) {
            return filename.substring(0, maxLength);
        } else {
            final String ext = filename.substring(i);
            return filename.substring(0, maxLength - ext.length()) + ext;
        }
    }

    /**
     * @param originalName
     * @param newName
     * @return
     */
    public static String replaceFilename(String originalName, String newName) {
        if (!StringUtils.hasLength(originalName)) {
            return null;
        }
        if (!StringUtils.hasLength(newName)) {
            return originalName;
        }
        final String extension = getExtension(originalName);
        return newName.endsWith(extension) ? newName : newName.concat(extension);
    }

    /**
     * @param filename
     * @return
     */
    public static String addUniqueId(String filename) {
        return addUniqueId(filename, null);
    }

    /**
     * @param filename
     * @param maxLength
     * @return
     */
    public static String addUniqueId(String filename, Integer maxLength) {
        String id = UUID.randomUUID().toString();
        if (!StringUtils.hasLength(filename)) {
            return id;
        }
        String res = id.concat("_").concat(filename);
        if (maxLength != null) {
            res = limitLength(res, maxLength);
        }
        return res;
    }

    /**
     * @param filename
     * @return
     */
    public static String removeUniqueId(String filename) {
        if (!StringUtils.hasLength(filename)) {
            return null;
        }
        return filename.substring(filename.indexOf('_') + 1);
    }

    /**
     * @param path
     * @param max
     * @return
     * @throws IOException
     */
    @SuppressWarnings("java:S2677")
    public static boolean checkMaxLines(Path path, int max) throws IOException {
        try (BufferedReader br = Files.newBufferedReader(path)) {
            long lines = 0;
            while (br.readLine() != null && ++lines <= max);
            return lines <= max;
        }
    }

    /**
     * @param path
     * @return
     */
    public static boolean isTempFile(Path path) {
        return path != null && path.toString().endsWith(EXTENSION_TEMP);
    }

    /**
     * @param path
     * @return
     */
    public static Path addTempExtension(Path path) {
        if (path == null || path.toString().endsWith(EXTENSION_TEMP)) {
            return path;
        }
        return path.resolveSibling(path.getFileName() + EXTENSION_TEMP);
    }

    /**
     * @param filename
     * @return
     */
    public static String addTempExtension(String filename) {
        if (filename == null || filename.endsWith(EXTENSION_TEMP)) {
            return filename;
        }
        return filename + EXTENSION_TEMP;
    }

    /**
     * @param path
     * @return
     */
    public static Path removeTempExtension(Path path) {
        if (path == null || !path.toString().endsWith(EXTENSION_TEMP)) {
            return path;
        }
        final String filename = path.getFileName().toString();
        return path.resolveSibling(filename.substring(0, filename.length() - 4));
    }

    /**
     * @param source
     * @return
     * @throws IOException
     */
    public static Path removeTempExtensionAndMove(Path source) throws IOException {
        final Path target = removeTempExtension(source);
        if (target != null) {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
        }
        return target;
    }

    /**
     * @param source
     * @param target
     * @throws IOException
     */
    public static void moveDirectory(Path source, Path target) throws IOException {
        if (source == null || target == null) {
            return;
        }
        if (Files.isDirectory(target)) {
            emptyDirectory(target);
        }
        Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
    }

    /**
     * @param directory
     * @throws IOException
     */
    public static void emptyDirectory(Path directory) throws IOException {
        if (directory == null) {
            return;
        }
        try (Stream<Path> walk = Files.walk(directory)) {
            walk.skip(1).sorted(Comparator.reverseOrder())
            .forEach(FileUtils::deleteSafely);
        }
    }

    /**
     * @param path
     */
    public static void deleteSafely(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) { /*DO NOTHING */ }
    }

    /**
     * @param file
     * @param newExtension
     * @return
     */
    public static Path setExtension(Path file, String newExtension) {
        return file.resolveSibling(removeExtension(file.getFileName().toString()) + newExtension);
    }

    /**
     * @param root
     * @param file
     * @return
     */
    public static Path resolveSafely(Path root, String file) {
        final Path path = Paths.get(file);
        return path.startsWith(root) ? path : root.resolve(path);
    }

    /**
     * @param folderPath
     * @param filename
     * @return
     */
    public static String resolveRelative(String folderPath, String filename) {
         return org.flcit.commons.core.util.StringUtils.concatWithIfMissing(folderPath, org.flcit.commons.core.util.StringUtils.SLASH, filename);
    }

    /**
     * @param path
     * @return
     */
    public static String getExtension(String path) {
        return getExtension(path, false);
    }

    /**
     * @param path
     * @param dot
     * @return
     */
    public static String getExtension(String path, boolean dot) {
        if (!StringUtils.hasLength(path)) {
            return null;
        }
        final int extIndex = path.lastIndexOf(StringUtils.DOT);
        if (extIndex == -1) {
            return null;
        }
        return path.substring(dot ? extIndex : extIndex + 1);
    }

    /**
     * @param path
     * @return
     */
    public static String removeExtension(String path) {
        if (!StringUtils.hasLength(path)) {
            return path;
        }
        final int extIndex = path.lastIndexOf(StringUtils.DOT);
        return extIndex == -1 ? path : path.substring(0, extIndex);
    }

    /**
     * @param path
     * @return
     */
    public static String getFilename(String path) {
        if (!StringUtils.hasLength(path)) {
            return null;
        }
        final int folderIndex = path.lastIndexOf(StringUtils.SLASH);
        return folderIndex == -1 ? path : path.substring(folderIndex + 1); 
    }

    /**
     * @param root
     * @return
     * @throws IOException
     */
    public static boolean deleteRecursively(Path root) throws IOException {
        if (root == null
                || !Files.exists(root)) {
            return false;
        }
        Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }
            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
        return true;
    }

}
