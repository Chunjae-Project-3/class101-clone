package net.fullstack.class101clone.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Function;

public class CommonDateUtil {
    public CommonDateUtil() {
    }

    public LocalDateTime parseLocalDateTime(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(date, formatter);
    }

    public static LocalDate parseLocalDate(String pattern, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(date, formatter);
    }

    public static <T> T parse(String date, String pattern, Function<String, T> parser) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        String formattedDate = date;
        return parser.apply(formattedDate);
    }
}
