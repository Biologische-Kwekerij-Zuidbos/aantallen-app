package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateParser {

    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public LocalDate parse(String dateString) {
        return LocalDate.parse(dateString, DATE_TIME_FORMATTER);
    }

}
