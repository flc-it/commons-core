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

import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class UriUtils {

    private static final Pattern PATTERN_PROTOCOL = Pattern.compile("[a-zA-Z0-9+-.]*://");
    private static final Pattern PATTERN_HOST = Pattern.compile("://[a-zA-Z0-9-._~!$\\()*+,;=]*[/:]");
    private static final Pattern PATTERN_PORT = Pattern.compile(":\\d+/");

    private UriUtils() { }

    /**
     * @param base
     * @param complete
     * @return
     */
    public static boolean isInstance(URI base, URI complete) {
        return complete.getScheme().equalsIgnoreCase(base.getScheme())
                && complete.getHost().equalsIgnoreCase(base.getHost())
                && complete.getPort() == base.getPort();
    }

    /**
     * @param base
     * @param service
     * @return
     */
    public static String resolveRelative(String base, String service) {
        return isRelative(service) ? makeResolveRelative(base, service) : concat(base, service);
    }

    /**
     * @param base
     * @param service
     * @return
     */
    public static String resolveRelative(URI base, URI service) {
        final String serviceString = StringUtils.convert(service);
        return isRelative(serviceString) ? makeResolveRelative(base, service) : concat(base, serviceString);
    }

    /**
     * @param base
     * @param service
     * @return
     */
    public static String resolveRelative(String base, URI service) {
        final String serviceString = StringUtils.convert(service);
        return isRelative(serviceString) ? makeResolveRelative(base, service) : concat(base, serviceString);
    }

    /**
     * @param base
     * @param service
     * @return
     */
    public static String resolveRelative(URI base, String service) {
        return isRelative(service) ? makeResolveRelative(base, service) : concat(base, service);
    }

    private static boolean isRelative(String service) {
        return service != null && service.startsWith(StringUtils.DOT);
    }

    private static String makeResolveRelative(String base, String service) {
        if (!StringUtils.hasLength(base)) {
            return service;
        }
        return makeResolveRelative(URI.create(base), service);
    }

    private static String makeResolveRelative(String base, URI service) {
        return !StringUtils.hasLength(base) ? service.toString() : makeResolveRelative(URI.create(base), service);
    }

    private static String makeResolveRelative(URI base, String service) {
        return base == null ? service : makeResolveRelative(base, URI.create(service));
    }

    private static String makeResolveRelative(URI base, URI service) {
        return base == null ? service.toString() : base.resolve(service).toString();
    }

    private static String concat(URI base, String service) {
        return base != null ? concat(base.toString(), service) : service;
    }

    private static String concat(String base, String service) {
        return StringUtils.concatWithIfMissing(base, StringUtils.SLASH, service);
    }

    /**
     * @param base
     * @param complete
     * @param newBase
     * @return
     */
    public static URI changeInstance(String base, String complete, String newBase) {
        if (!StringUtils.hasLength(base)
                || !StringUtils.hasLength(newBase)) {
            return StringUtils.hasLength(complete) ? URI.create(complete) : null;
        }
        return URI.create(concat(newBase, complete.substring(base.length())));
    }

    /**
     * @param base
     * @param complete
     * @param newBase
     * @return
     */
    public static URI changeInstance(URI base, URI complete, URI newBase) {
        if (base == null || newBase == null || complete == null) {
            return complete;
        }
        return appendSeparator(newBase).resolve(base.relativize(complete));
    }

    private static URI appendSeparator(URI url) {
        if (url == null) {
            return null;
        }
        final String urlString = url.toString();
        return urlString.endsWith(StringUtils.SLASH) ? url : URI.create(urlString + StringUtils.SLASH);
    }

    /**
     * @param url
     * @param newProtocol
     * @param newHost
     * @param newPort
     * @param removePort
     * @return
     */
    public static String changeUrl(String url, String newProtocol, String newHost, Integer newPort, boolean removePort) {
        if (newProtocol != null) {
            url = PATTERN_PROTOCOL.matcher(url).replaceFirst(newProtocol + "://");
        }
        if (newHost != null) {
            final Matcher m = PATTERN_HOST.matcher(url);
            if (m.find()) {
                final String group = m.group();
                final String replace = "://" + newHost + group.charAt(group.length() - 1);
                url = m.replaceFirst(replace);
            }
        }
        if (newPort != null) {
            url = PATTERN_PORT.matcher(url).replaceFirst(":" + newPort + "/");
        }
        if (removePort) {
            url = PATTERN_PORT.matcher(url).replaceFirst("/");
        }
        return url;
    }

    /**
     * @param protocol
     * @return
     */
    public static boolean isProtocolValid(final String protocol) {
        if (protocol == null) {
            return true;
        }
        for (int i = 0 ; i < protocol.length(); i++) {
            if (!isAlpha(protocol.charAt(i))
                    && !isDigit(protocol.charAt(i))
                    && protocol.charAt(i) != '+'
                    && protocol.charAt(i) != '-'
                    && protocol.charAt(i) != '.') {
                return false;
            }
        }
        return true;
    }

    /**
     * @param host
     * @return
     */
    public static boolean isHostValid(final String host) {
        if (host == null) {
            return true;
        }
        for (int i = 0 ; i < host.length(); i++) {
            if (!isUnreserved(host.charAt(i))
                    && !isSubDelimiter(host.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param pathSegment
     * @return
     */
    public static boolean isPathSegmentValid(final String pathSegment) {
        if (pathSegment == null) {
            return true;
        }
        for (int i = 0 ; i < pathSegment.length(); i++) {
            if (!isPchar(pathSegment.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * Indicates whether the given character is in the {@code ALPHA} set.
     * RFC 3986, appendix A
     */
    private static boolean isAlpha(int c) {
        return (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z');
    }

    /**
     * Indicates whether the given character is in the {@code DIGIT} set.
     * RFC 3986, appendix A
     */
    private static boolean isDigit(int c) {
        return (c >= '0' && c <= '9');
    }

    /**
     * Indicates whether the given character is in the {@code sub-delims} set.
     * RFC 3986, appendix A
     */
    private static boolean isSubDelimiter(int c) {
        return ('!' == c || '$' == c || '&' == c || '\'' == c || '(' == c || ')' == c || '*' == c || '+' == c ||
                ',' == c || ';' == c || '=' == c);
    }

    /**
     * Indicates whether the given character is in the {@code unreserved} set.
     * RFC 3986, appendix A
     */
    private static boolean isUnreserved(int c) {
        return (isAlpha(c) || isDigit(c) || '-' == c || '.' == c || '_' == c || '~' == c);
    }

    /**
     * Indicates whether the given character is in the {@code pchar} set.
     * RFC 3986, appendix A
     */
    private static boolean isPchar(int c) {
        return (isUnreserved(c) || isSubDelimiter(c) || ':' == c || '@' == c);
    }

}
