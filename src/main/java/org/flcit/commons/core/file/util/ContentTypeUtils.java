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

import org.flcit.commons.core.util.StringUtils;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class ContentTypeUtils {

    private ContentTypeUtils() { }

    /**
     * @param filename
     * @return
     */
    public static String get(String filename) {
        final String extension = FileUtils.getExtension(filename);
        if (!StringUtils.hasLength(extension)) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        switch (extension) {
            case "pdf": return MediaType.APPLICATION_PDF_VALUE;
            case "json": return MediaType.APPLICATION_JSON_VALUE;
            case "doc": return MediaType.APPLICATION_WORD_DOC_VALUE;
            case "docx": return MediaType.APPLICATION_WORD_DOCX_VALUE;
            case "zip": return MediaType.APPLICATION_ZIP_VALUE;
            case "odt": return MediaType.APPLICATION_OPENDOCUMENT_ODT_VALUE;
            case "txt": return MediaType.TEXT_PLAIN_VALUE;
            case "htm": return MediaType.TEXT_HTML_VALUE;
            case "html": return MediaType.TEXT_HTML_VALUE;
            case "xml": return MediaType.TEXT_XML_VALUE;
            case "csv": return MediaType.TEXT_CSV_VALUE;
            case "md": return MediaType.TEXT_MARKDOWN_VALUE;
            case "png": return MediaType.IMAGE_PNG_VALUE;
            case "jpg": return MediaType.IMAGE_JPEG_VALUE;
            case "jpeg": return MediaType.IMAGE_JPEG_VALUE;
            case "gif": return MediaType.IMAGE_GIF_VALUE;
            default: return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }

}
