package org.example;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class DateReader {

    private final DateParser dateParser = new DateParser();
    private final WorkbookFactory workbookFactory = new WorkbookFactory();
    
    public Set<LocalDate> readDates(File file) throws IOException {
        Workbook workbook = workbookFactory.createFromFile(file);
        Sheet firstSheet = workbook.getSheetAt(0);
        Row firstRow = firstSheet.getRow(0);
        return getDates(firstRow);
    }

    private Set<LocalDate> getDates(Row row) {
        HashSet<LocalDate> dates = new HashSet<>();
        row.cellIterator().forEachRemaining(cell -> {
            String content = cell.getStringCellValue();
            if (dateParser.isValid(content)) {
                LocalDate date = dateParser.parse(content);
                dates.add(date);
            }
        });
        System.out.println(dates);
        return dates;
    }

}
