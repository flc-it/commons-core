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

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class DateUtils {

    /**
     * 
     */
    public static final TimeZone UTC = TimeZone.getTimeZone("UTC");

    private static final DatatypeFactory datatypeFactory;

    /**
     * 
     */
    public static final String DATE_ISO8601_PATTERN = "yyyy-MM-dd";
    private static final String DATE_TIME_ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    @SuppressWarnings("java:S2885")
    private static final DateFormat DATE_TIME_FILE = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    @SuppressWarnings("java:S2885")
    private static final DateFormat DATE_FR = new SimpleDateFormat("dd/MM/yyyy");
    @SuppressWarnings("java:S2885")
    private static final DateFormat DATE_TIME_FR = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    @SuppressWarnings("java:S2885")
    private static final DateFormat DATE_ISO8601 = new SimpleDateFormat(DATE_ISO8601_PATTERN);
    @SuppressWarnings("java:S2885")
    private static final DateFormat DATE_TIME_ISO8601 = getDateTimeFormatISO8601();

    static {
        try {
            datatypeFactory = DatatypeFactory.newInstance();
        } catch (DatatypeConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    private DateUtils() { }

    /*
     *  DateFormat is not thread-safe so it needs
     *  to execute in extern synchronized block
     */
    /**
     * @param format
     * @param date
     * @return
     */
    @SuppressWarnings("java:S2445")
    public static String format(final DateFormat format, final Date date) {
        synchronized (format) {
            return format.format(date);
        }
    }

    /**
     * @param format
     * @param date
     * @return
     * @throws ParseException
     */
    @SuppressWarnings("java:S2445")
    public static Date parse(final DateFormat format, final String date) throws ParseException {
        synchronized (format) {
            return format.parse(date);
        }
    }

    /**
     * @param date
     * @return
     */
    public static String formatFile(Date date) {
        return format(DATE_TIME_FILE, date);
    }

    /**
     * @param date
     * @return
     */
    public static String formatDateISO8601(Date date) {
        return format(DATE_ISO8601, date);
    }

    /**
     * @return
     */
    public static DateFormat getDateTimeFormatISO8601() {
        final DateFormat df = new SimpleDateFormat(DATE_TIME_ISO8601_FORMAT);
        df.setTimeZone(UTC);
        return df;
    }

    /**
     * @param date
     * @return
     */
    public static String formatDateTimeISO8601(Date date) {
        return format(DATE_TIME_ISO8601, date);
    }

    /**
     * @param date
     * @return
     */
    public static String formatDateFR(Date date) {
        return format(DATE_FR, date);
    }

    /**
     * @param date
     * @return
     * @throws ParseException
     */
    public static Date parseDateFR(String date) throws ParseException {
        return parse(DATE_FR, date);
    }

    /**
     * @param date
     * @return
     */
    public static String formatDateTimeFR(Date date) {
        return format(DATE_TIME_FR, date);
    }

    /**
     * @return
     */
    public static XMLGregorianCalendar currentXMLGregorianCalendar() {
        return datatypeFactory.newXMLGregorianCalendar(new GregorianCalendar());
    }

    /**
     * @return
     */
    public static XMLGregorianCalendar currentXMLGregorianCalendarNoTimezone() {
        final XMLGregorianCalendar date = currentXMLGregorianCalendar();
        date.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
        return date;
    }

    /**
     * @param date
     * @return
     */
    @SuppressWarnings("deprecation")
    public static LocalTime convertToLocalTime(Date date) {
        return date == null ? null : LocalTime.of(date.getHours(), date.getMinutes(), getSeconds(date.getSeconds()));
    }

    private static int getSeconds(int seconds) {
        return seconds > 59 ? 59 : seconds;
    }

    /**
     * @param date
     * @return
     */
    @SuppressWarnings("deprecation")
    public static LocalDate convertToLocalDate(Date date) {
        return date == null ? null : LocalDate.of(date.getYear() + 1900, date.getMonth() + 1, date.getDate());
    }

    /**
     * @param date
     * @return
     */
    public static LocalDateTime convertToLocalDateTime(Date date) {
        return date == null ? null : LocalDateTime.of(convertToLocalDate(date), convertToLocalTime(date));
    }

    /**
     * @return
     */
    public static Timestamp currentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }

    /**
     * @param date
     * @return
     */
    public static Timestamp convertToTimestamp(Date date) {
        return date == null ? null : new Timestamp(date.getTime());
    }

    /**
     * @param date
     * @return
     */
    public static Timestamp convertToTimestamp(XMLGregorianCalendar date) {
        return date == null ? null : new Timestamp(date.toGregorianCalendar().getTimeInMillis());
    }

}
