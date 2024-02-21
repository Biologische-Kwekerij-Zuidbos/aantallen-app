package org.example;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import org.junit.Test;

public class DateParserTest {
    
    private final DateParser dateParser = new DateParser();

    @Test
    public void testParse_WithValidDate() {
        String dateString = "22-12-2023";
        LocalDate date = dateParser.parse(dateString);
        LocalDate expectedDate = LocalDate.of(2023, 12, 22);
        assertEquals(expectedDate, date);
    }

    @Test
    public void testParse_WithInvalidDate() {
        String dateString = "22-12-202323456";
        DateTimeParseException exception = assertThrows(DateTimeParseException.class, () -> dateParser.parse(dateString));
        String expectedExceptionMessage = "Text '22-12-202323456' could not be parsed at index 6";
        String actualExceptionMessage = exception.getMessage();
        assertEquals(expectedExceptionMessage, actualExceptionMessage);
    }

}
