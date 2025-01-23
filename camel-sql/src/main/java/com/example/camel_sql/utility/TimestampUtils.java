package com.example.camel_sql.utility;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimestampUtils {

    public static String getTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.TIMESTAMP_FORMAT);
        return LocalDateTime.now().format(formatter);
    }
}
