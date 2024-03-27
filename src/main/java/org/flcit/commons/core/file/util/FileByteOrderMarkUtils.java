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

import java.util.Arrays;
import java.util.List;

import org.flcit.commons.core.util.ArrayUtils;
import org.flcit.commons.core.util.StringUtils;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class FileByteOrderMarkUtils {

    private static final int[] BYTE_ORDER_BOM = new int[] { 0xFEFF, 0xFFFE };
    private static final List<int[]> BYTE_SEQUENCE_ORDER_BOM = Arrays.asList(
            // UTF-8
            new int[] { 0xEF, 0xBB, 0xBF },
            // UTF-16 Big Endian
            new int[] { 0xFE, 0xFF },
            // UTF-16 Little Endian
            new int[] { 0xFF, 0xFE },
            // UTF-32 Big Endian
            new int[] { 0x00, 0x00, 0xFE, 0xFF },
            // UTF-32 Little Endian
            new int[] { 0xFF, 0xFE, 0x00, 0x00 }
    );

    private FileByteOrderMarkUtils() { }

    /**
     * @param value
     * @return
     */
    public static String removeByteOrderMark(String value) {
        if (!StringUtils.hasLength(value)) {
            return value;
        }
        int nbByteOrderMask = 0;
        for (char c : value.toCharArray()) {
            if (isByteOrderMark(c)) {
                nbByteOrderMask++;
            } else {
                break;
            }
        }
        return nbByteOrderMask == 0 ? value : value.substring(nbByteOrderMask);
    }

    /**
     * @param c
     * @return
     */
    public static boolean isByteOrderMark(int c) {
        return ArrayUtils.contains(BYTE_ORDER_BOM, c);
    }

    /**
     * @param c
     * @param order
     * @return
     */
    public static boolean isByteOrderMarkSequence(int c, int order) {
        for (int[] i : BYTE_SEQUENCE_ORDER_BOM) {
            if (i.length <= order) {
                continue;
            }
            if (i[order] == c) {
                return true;
            }
        }
        return false;
    }

    /**
     * @param c
     * @return
     */
    public static boolean isByteOrderMarkSequence(int[] c) {
        for (int[] i : BYTE_SEQUENCE_ORDER_BOM) {
            if (i.length != c.length) {
                continue;
            }
            if (Arrays.equals(i, c)) {
                return true;
            }
        }
        return false;
    }

}
