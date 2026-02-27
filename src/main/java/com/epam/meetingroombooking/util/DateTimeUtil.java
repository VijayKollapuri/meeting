package com.epam.meetingroombooking.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeUtil {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static String format(LocalDateTime dateTime) {
        return dateTime != null ? dateTime.format(FORMATTER) : "";
    }

    public static String calculateDuration(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) return "";
        Duration duration = Duration.between(start, end);
        long minutes = duration.toMinutes();
        if (minutes < 60) {
            return minutes + " mins";
        } else {
            return (minutes / 60) + "h " + (minutes % 60) + "m";
        }
    }
}
