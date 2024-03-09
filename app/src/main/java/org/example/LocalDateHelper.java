/* (C)2024 */
package org.example;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public final class LocalDateHelper {

    public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
