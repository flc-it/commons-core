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
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.UnmappableCharacterException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class StringUtils {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    /**
     * 
     */
    public static final String NULL = "null";
    /**
     * 
     */
    public static final String EMPTY = "";
    /**
     * 
     */
    public static final String SPACE = " ";
    /**
     * 
     */
    public static final String CRLF = "\r\n";
    /**
     * 
     */
    public static final String DOT = ".";
    /**
     * 
     */
    public static final String PIPE = "|";
    /**
     * 
     */
    public static final String COMMA = ",";
    /**
     * 
     */
    public static final String COLON = ":";
    /**
     * 
     */
    public static final String AMPERSAND = "&";
    /**
     * 
     */
    public static final String QUESTION_MARK = "?";
    /**
     * 
     */
    public static final String EQUAL = "=";
    /**
     * 
     */
    public static final String SLASH = "/";
    /**
     * 
     */
    public static final String SEMICOLON = ";";
    /**
     * 
     */
    public static final String QUOTE = "'";
    /**
     * 
     */
    public static final String DOUBLE_QUOTE = "\"";

    private StringUtils() { }

    /**
     * @param value
     * @return
     */
    public static boolean hasLength(String value) {
        return value != null && !value.isEmpty();
    }

    /**
     * @param value
     * @return
     */
    public static String upperTrim(String value) {
        return upper(trim(value));
    }

    /**
     * @param value
     * @return
     */
    public static String lowerTrim(String value) {
        return lower(trim(value));
    }

    /**
     * @param value
     * @return
     */
    public static String upperNoSpace(String value) {
        return upper(noSpace(value));
    }

    /**
     * @param value
     * @return
     */
    public static String lowerNoSpace(String value) {
        return lower(noSpace(value));
    }

    /**
     * @param value
     * @return
     */
    public static String upper(String value) {
        return value == null ? null : value.toUpperCase();
    }

    /**
     * @param value
     * @return
     */
    public static String lower(String value) {
        return value == null ? null : value.toLowerCase();
    }

    /**
     * @param value
     * @return
     */
    public static String trim(String value) {
        return value == null ? null : value.trim();
    }

    /**
     * @param value
     * @return
     */
    public static String noSpace(String value) {
        if (value == null) {
            return null;
        }
        final char[] values = value.toCharArray();
        final CharBuffer buf =  CharBuffer.allocate(values.length);
        for (char c : values) {
            if (c != 20) {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    /**
     * @param value
     * @return
     */
    public static String noWhitespace(String value) {
        if (value == null) {
            return null;
        }
        final char[] values = value.toCharArray();
        final CharBuffer buf =  CharBuffer.allocate(values.length);
        for (char c : values) {
            if (!Character.isWhitespace(c)) {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    /**
     * @param value
     * @param maxLength
     * @return
     */
    public static String limitLength(String value, int maxLength) {
        return exceed(value, maxLength) ? value.substring(0, maxLength) : value;
    }

    /**
     * @param value
     * @return
     */
    public static int length(String value) {
        return value == null ? 0 : value.length();
    }

    /**
     * @param value
     * @param maxLength
     * @return
     */
    public static boolean exceed(String value, int maxLength) {
        return length(value) > maxLength;
    }

    /**
     * @param delimiter
     * @param values
     * @return
     */
    public static String join(CharSequence delimiter, Iterable<?> values) {
        return join(delimiter, values, false);
    }

    /**
     * @param delimiter
     * @param values
     * @param nullValues
     * @return
     */
    public static String join(CharSequence delimiter, Iterable<?> values, boolean nullValues) {
        if (values == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object obj : values) {
            if (obj == null && !nullValues) {
                continue;
            }
            if (!first) {
                sb.append(delimiter);
            } else {
                first = false;
            }
            sb.append(obj != null ? obj.toString() : NULL);
        }
        return sb.toString();
    }

    /**
     * @param <T>
     * @param delimiter
     * @param values
     * @return
     */
    public static <T> String join(CharSequence delimiter, T[] values) {
        return join(delimiter, values, false);
    }

    /**
     * @param <T>
     * @param delimiter
     * @param values
     * @param nullValues
     * @return
     */
    public static <T> String join(CharSequence delimiter, T[] values, boolean nullValues) {
        if (values == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (T obj : values) {
            if (obj == null && !nullValues) {
                continue;
            }
            if (!first) {
                sb.append(delimiter);
            } else {
                first = false;
            }
            sb.append(obj != null ? obj.toString() : NULL);
        }
        return sb.toString();
    }

    /**
     * @param value
     * @return
     */
    public static String normalizeAndUpperToASCII(String value) {
        if (value == null) {
            return null;
        }
        final char[] characters = value.trim().toCharArray();
        if (characters == null || characters.length < 1) {
            return null;
        }
        final char[] values = value.toCharArray();
        final CharBuffer buf =  CharBuffer.allocate(values.length);
        for (char c : values) {
            if (isASCII(c)) {
                buf.append(c);
            }
        }
        return upper(buf.toString());
    }

    /**
     * @param prefix
     * @param value
     * @return
     */
    public static String prefixIfMissingAndHasLength(String prefix, String value) {
        return !hasLength(value) ? value : prefixIfMissing(prefix, value);
    }

    /**
     * @param prefix
     * @param value
     * @return
     */
    public static String prefixIfMissing(String prefix, String value) {
        if (value == null) {
            return prefix;
        }
        return prefix == null || value.startsWith(prefix) ? value : prefix + value;
    }

    /**
     * @param value
     * @param suffix
     * @return
     */
    public static String appendIfMissing(String value, String suffix) {
        if (value == null) {
            return suffix;
        }
        return suffix == null || value.endsWith(suffix) ? value : value + suffix;
    }

    /**
     * @param prefix
     * @param separator
     * @param suffix
     * @return
     */
    public static String concatWithIfMissing(String prefix, String separator, String suffix) {
        if (!hasLength(prefix)) {
            return suffix;
        } else if (!hasLength(suffix)) {
            return prefix;
        }
        final boolean prefixSeparator = prefix.endsWith(separator);
        final boolean suffixSeparator = suffix.startsWith(separator);
        if (!prefixSeparator && !suffixSeparator) {
            return prefix + separator + suffix;
        }
        return prefixSeparator && suffixSeparator ? prefix + suffix.substring(separator.length()) : prefix + suffix;
    }

    /**
     * @param value
     * @param onlyPrintable
     * @return
     */
    public static boolean isCp1252(char[] value, boolean onlyPrintable) {
        if (value == null) {
            return true;
        }
        for (int i = 0; i < value.length; i++) {
            if (!(onlyPrintable ? isPrintableCp1252(value[i]) : isCp1252(value[i]))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param value
     * @param onlyPrintable
     * @return
     */
    public static boolean isIso88591(char[] value, boolean onlyPrintable) {
        if (value == null) {
            return true;
        }
        for (int i = 0; i < value.length; i++) {
            if (!(onlyPrintable ? isPrintableIso88591(value[i]) : isIso88591(value[i]))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param value
     * @return
     */
    public static boolean isHex(int value) {
        return (value > 47 && value < 58) || (value > 64 && value < 71);
    }

    /**
     * @param value
     * @return
     */
    public static boolean isASCII(int value) {
        return value >= 0 && value < 128;
    }

    /**
     * @param value
     * @return
     */
    public static boolean isPrintableASCII(int value) {
        return value > 31 && value < 127;
    }

    /**
     * @param value
     * @return
     */
    public static boolean isPrintableIso88591(int value) {
        return value > 31 && value < 256 && !(value > 126 && value < 160);
    }

    /**
     * @param value
     * @return
     */
    public static boolean isPrintableCp1252(int value) {
        return value > 31 && value < 256;
    }

    /**
     * @param value
     * @return
     */
    public static boolean isCp1252(int value) {
        return value < 256;
    }

    /**
     * @param value
     * @return
     */
    public static boolean isIso88591(int value) {
        return value < 256 && !(value > 126 && value < 160);
    }

    /**
     * @param value
     * @return
     */
    public static String toIso88591(String value) {
        if (value == null) {
            return null;
        }
        final StringBuilder sb = new StringBuilder(value.length());
        char c;
        for (int i = 0; i < value.length(); i++) {
            c = value.charAt(i);
            if (isIso88591(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * @param bytes
     * @return
     */
    public static String encodeToHex(byte[] bytes) {
        final char[] hexChars = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            int v = bytes[i] & 0xFF;
            hexChars[i * 2] = HEX_ARRAY[v >>> 4];
            hexChars[i * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    /**
     * @param value
     * @return
     * @throws IOException
     */
    public static byte[] decodeFromHexToBytes(String value) throws IOException {
        final int l = value.length();
        final byte[] data = new byte[l / 2];
        for (int i = 0; i < l; i += 2) {
            final char char1 = value.charAt(i);
            final char char2 = value.charAt(i + 1);
            if (!isHex(char1)) {
                throw new UnmappableCharacterException(i + 1);
            }
            if (!isHex(char2)) {
                throw new UnmappableCharacterException(i + 2);
            }
            data[i / 2] = (byte) ((Character.digit(char1, 16) << 4)
                    + Character.digit(char2, 16));
        }
        return data;
    }

    /**
     * @param value
     * @param charset
     * @return
     * @throws IOException
     */
    public static String decodeFromHex(String value, Charset charset) throws IOException {
        final byte[] data = decodeFromHexToBytes(value);
        return data.length == 0 ? null : new String(data, charset);
    }

    /**
     * @param value
     * @param onlyPrintable
     * @return
     */
    public static boolean isASCII(byte[] value, boolean onlyPrintable) {
        if (value == null) {
            return true;
        }
        for (int i = 0; i < value.length; i++) {
            if (!(onlyPrintable ? isPrintableASCII(value[i]) : isASCII(value[i]))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param value
     * @param charset
     * @return
     * @throws IOException
     */
    public static byte[] getBytes(String value, Charset charset) throws IOException {
        return getBytes(CharBuffer.wrap(value), charset);
    }

    /**
     * @param value
     * @param charset
     * @return
     * @throws IOException
     */
    public static byte[] getBytes(CharBuffer value, Charset charset) throws IOException {
        return charset.newEncoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT)
                .encode(value).array();
    }

    /**
     * @param name
     * @param startsWith
     * @return
     */
    public static boolean startsWithOne(String name, String... startsWith) {
        if (name == null || startsWith == null) {
            return false;
        }
        for (String sw : startsWith) {
            if (name.startsWith(sw)) {
                return true;
            }
        }
        return false;
    }

    private static String wordToCamelCase(String word) {
        if (!hasLength(word)) {
            return null;
        }
        return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
    }

    /**
     * @param value
     * @return
     */
    public static String toCamelCase(String value) {
        if (!hasLength(value)) {
            return null;
        }
        final StringBuilder sb = new StringBuilder();
        final String[] split = value.split("_");
        for (String word: split) {
            sb.append(wordToCamelCase(word));
        }
        return sb.toString();
    }

    /**
     * @param value
     * @return
     */
    public static String convert(Object value) {
        return convert(value, null);
    }

    /**
     * @param value
     * @return
     */
    public static String convertOrEmpty(Object value) {
        return convert(value, StringUtils.EMPTY);
    }

    /**
     * @param value
     * @return
     */
    public static String convertOrNull(Object value) {
        return convert(value, StringUtils.NULL);
    }

    private static String convert(Object value, String valueIfNull) {
        return value != null ? value.toString() : valueIfNull;
    }

    /**
     * @param <T>
     * @param value
     * @return
     */
    public static <T extends Number> String convert(Optional<T> value) {
        return convert(OptionalUtils.value(value));
    }

    /**
     * @param v
     * @return
     */
    @SuppressWarnings("java:S1168")
    public static String[] convertToArray(Object[] v) {
        if (ArrayUtils.isEmpty(v)) {
            return null;
        }
        final String[] res = new String[v.length];
        for (int i = 0 ; i < v.length; i++) {
            res[i] = convert(v[i]);
        }
        return res;
    }

    /**
     * @param value
     * @return
     */
    public static Short toShort(String value) {
        if (value == null) {
            return null;
        }
        return Short.valueOf(value);
    }

    /**
     * @param value
     * @return
     */
    public static String nullIfEmpty(String value) {
        return hasLength(value) ? value : null;
    }

    /**
     * @param value
     * @return
     */
    public static String nullIfNullValue(String value) {
        return NULL.equals(value) ? null : value;
    }

    /**
     * @param value
     * @return
     */
    public static String emptyIfNull(String value) {
        return value == null || value.equalsIgnoreCase(NULL) ? EMPTY : value;
    }

    /**
     * @param values
     * @return
     */
    public static String firstHasLength(String... values) {
        if (ArrayUtils.isEmpty(values)) {
            return null;
        }
        for (String value: values) {
            if (hasLength(value)) {
                return value;
            }
        }
        return null;
    }

    /**
     * @param value
     * @param suffix
     * @return
     */
    public static String suffix(String value, String suffix) {
        return value == null || value.endsWith(suffix) ? value : value + suffix;
    }

    /**
     * @param value
     * @param separator
     * @return
     */
    public static Entry<String, String> splitEntry(String value, String separator) {
        if (value == null) {
            return null;
        }
        final int index = value.indexOf(separator);
        return index == -1 ? null : new SimpleEntry<>(value.substring(0, index), value.substring(index + separator.length()));
    }

}
