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

package org.flcit.commons.core.file.csv.util;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import org.flcit.commons.core.file.csv.domain.Delimiter;
import org.flcit.commons.core.file.csv.domain.Enclosure;
import org.flcit.commons.core.util.ReflectionUtils;
import org.flcit.commons.core.util.StringUtils;
import org.flcit.commons.core.util.ArrayUtils;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class CsvUtils {

    private CsvUtils() { }

    /**
     * @param clazz
     * @param outputStream
     * @throws IOException
     */
    public static void writeHeader(Class<?> clazz, OutputStream outputStream) throws IOException {
        writeHeader(clazz, outputStream, Delimiter.SEMICOLON);
    }

    /**
     * @param clazz
     * @param outputStream
     * @param delimiter
     * @throws IOException
     */
    public static void writeHeader(Class<?> clazz, OutputStream outputStream, Delimiter delimiter) throws IOException {
        final String[] fieldsName = ReflectionUtils.getAllFieldsName(clazz);
        if (ArrayUtils.isEmpty(fieldsName)) {
            return;
        }
        outputStream.write(String.join(delimiter.toString(), fieldsName).getBytes());
    }

    /**
     * @param os
     * @param rs
     * @throws SQLException
     * @throws IOException
     */
    public static void write(OutputStream os, ResultSet rs) throws SQLException, IOException {
        write(os, rs, Delimiter.SEMICOLON, Enclosure.DOUBLE_QUOTE);
    }

    /**
     * @param os
     * @param rs
     * @param delimiter
     * @param enclosure
     * @throws SQLException
     * @throws IOException
     */
    public static void write(OutputStream os, ResultSet rs, Delimiter delimiter, Enclosure enclosure) throws SQLException, IOException {
        if (rs.next()) {
            writeHeader(os, rs.getMetaData(), delimiter, enclosure);
            os.write(StringUtils.CRLF.getBytes());
            writeValues(os, rs, delimiter, enclosure);
        }
        while (rs.next()) {
            os.write(StringUtils.CRLF.getBytes());
            writeValues(os, rs, delimiter, enclosure);
        }
    }

    private static void writeValues(OutputStream os, ResultSet rs, Delimiter delimiter, Enclosure enclosure) throws SQLException, IOException {
        final int size = rs.getMetaData().getColumnCount();
        if (size == 0) {
            return;
        }
        writeValue(os, rs, 1, enclosure);
        for (int i = 2; i <= size; i++) {
            write(os, delimiter.toString());
            writeValue(os, rs, i, enclosure);
        }
    }

    private static void writeHeader(OutputStream os, ResultSetMetaData metadata, Delimiter delimiter, Enclosure enclosure) throws SQLException, IOException {
        final int size = metadata.getColumnCount();
        if (size == 0) {
            return;
        }
        writeHeader(os, metadata, 1, enclosure);
        for (int i = 2; i <= size; i++) {
            write(os, delimiter.toString());
            writeHeader(os, metadata, i, enclosure);
        }
    }

    private static void writeHeader(OutputStream os, ResultSetMetaData metadata, int index, Enclosure enclosure) throws SQLException, IOException {
        write(os, metadata.getColumnLabel(index), enclosure);
    }

    private static void writeValue(OutputStream os, ResultSet rs, int index, Enclosure enclosure) throws SQLException, IOException {
        write(os, StringUtils.convert(rs.getObject(index)), enclosure);
    }

    private static void write(OutputStream os, String value, Enclosure enclosure) throws IOException {
        write(os, enclosure != null ? enclosure + StringUtils.nullIfNullValue(value) + enclosure : StringUtils.nullIfNullValue(value));
    }

    private static void write(OutputStream os, String value) throws IOException {
        if (value != null) {
            os.write(value.getBytes());
        }
    }

}
