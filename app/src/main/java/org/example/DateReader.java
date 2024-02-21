package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class DateReader {

    private final DateParser dateParser = new DateParser();
    
    public Set<LocalDate> readDates(File file) throws IOException {
        FileInputStream inputStream = new FileInputStream(file);
        Workbook workbook = new HSSFWorkbook(inputStream);
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

        return dates;
    }

}
