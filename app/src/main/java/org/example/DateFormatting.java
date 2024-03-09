/* (C)2024 */
package org.example;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public final class DateFormatting {

    public static final DateTimeFormatter DUTCH_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT);
}
