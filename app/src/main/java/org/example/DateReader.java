package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class DateReader {

    public Set<LocalDate> readDates(File file) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            try (Workbook workbook = new HSSFWorkbook(inputStream)) {
                Sheet firstSheet = workbook.getSheetAt(0);
                Row firstRow = firstSheet.getRow(0);
                return getDates(firstRow);
            }
        }
    }

    private SortedSet<LocalDate> getDates(Row row) {
        TreeSet<LocalDate> dates = createSortedDateSet();
        row.cellIterator().forEachRemaining(cell -> {
            Optional<LocalDate> optDate = getDate(cell);
            optDate
                .filter(date -> date.isAfter(LocalDate.now().minusMonths(3)))
                .ifPresent(dates::add);
        });
        return dates;
    }

    private TreeSet<LocalDate> createSortedDateSet() {
        Comparator<LocalDate> comparator = new Comparator<LocalDate>() {
            public int compare(LocalDate a, LocalDate b) {
                return a.compareTo(b);
            }
        };

        return new TreeSet<>(comparator);
    }

    private Optional<LocalDate> getDate(Cell cell) {
        try {
            Date date = cell.getDateCellValue();
            LocalDate localDate = LocalDateHelper.convertToLocalDateViaInstant(date);
            return Optional.of(localDate);
        } catch(Exception e) {
            return Optional.empty();
        }
    }

}
