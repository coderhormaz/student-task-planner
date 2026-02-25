package com.hormaz.studentplanner.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class DateUtils {

    private static final DateTimeFormatter ISO_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DISPLAY_DATE =
            DateTimeFormatter.ofPattern("EEEE, MMMM d");
    private static final DateTimeFormatter DISPLAY_DATE_SHORT =
            DateTimeFormatter.ofPattern("MMM d");
    private static final DateTimeFormatter DISPLAY_TIME =
            DateTimeFormatter.ofPattern("h:mm a");
    private static final DateTimeFormatter DISPLAY_DATE_TIME =
            DateTimeFormatter.ofPattern("MMM d, h:mm a");

    public static String getGreeting() {
        int hour = LocalTime.now().getHour();
        if (hour < 12) return "Good morning";
        if (hour < 17) return "Good afternoon";
        return "Good evening";
    }

    public static String getTodayDisplay() {
        return LocalDate.now().format(DISPLAY_DATE);
    }

    public static String getTodayISO() {
        return LocalDate.now().format(DATE_FORMAT);
    }

    public static String formatDate(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) return "";
        try {
            LocalDate date = LocalDate.parse(isoDate.substring(0, 10), DATE_FORMAT);
            return date.format(DISPLAY_DATE_SHORT);
        } catch (Exception e) {
            return isoDate;
        }
    }

    public static String formatDateTime(String isoDateTime) {
        if (isoDateTime == null || isoDateTime.isEmpty()) return "";
        try {
            LocalDateTime dateTime = LocalDateTime.parse(isoDateTime, ISO_FORMAT);
            return dateTime.format(DISPLAY_DATE_TIME);
        } catch (Exception e) {
            return isoDateTime;
        }
    }

    public static String formatTime(String isoDateTime) {
        if (isoDateTime == null || isoDateTime.isEmpty()) return "";
        try {
            LocalDateTime dateTime = LocalDateTime.parse(isoDateTime, ISO_FORMAT);
            return dateTime.format(DISPLAY_TIME);
        } catch (Exception e) {
            return "";
        }
    }

    public static String toISO(LocalDate date) {
        return date.format(DATE_FORMAT);
    }

    public static String toISO(LocalDateTime dateTime) {
        return dateTime.format(ISO_FORMAT);
    }

    public static String nowISO() {
        return LocalDateTime.now().format(ISO_FORMAT);
    }

    public static LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        try {
            return LocalDate.parse(dateStr.substring(0, 10), DATE_FORMAT);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isToday(String dateStr) {
        LocalDate date = parseDate(dateStr);
        return date != null && date.equals(LocalDate.now());
    }

    public static boolean isTomorrow(String dateStr) {
        LocalDate date = parseDate(dateStr);
        return date != null && date.equals(LocalDate.now().plusDays(1));
    }

    public static boolean isOverdue(String dateStr) {
        LocalDate date = parseDate(dateStr);
        return date != null && date.isBefore(LocalDate.now());
    }

    public static String getRelativeDate(String dateStr) {
        if (dateStr == null) return "";
        LocalDate date = parseDate(dateStr);
        if (date == null) return "";

        LocalDate today = LocalDate.now();
        long days = ChronoUnit.DAYS.between(today, date);

        if (days == 0) return "Today";
        if (days == 1) return "Tomorrow";
        if (days == -1) return "Yesterday";
        if (days > 1 && days <= 7) return "In " + days + " days";
        if (days < -1 && days >= -7) return Math.abs(days) + " days ago";
        return formatDate(dateStr);
    }

    public static long toMillis(int year, int month, int day, int hour, int minute) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month, day, hour, minute, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }

    private DateUtils() {}
}
