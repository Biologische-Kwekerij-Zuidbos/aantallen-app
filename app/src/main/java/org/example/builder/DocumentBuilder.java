package org.example.builder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.example.DateFormatting;

public class DocumentBuilder implements IDocumentBuilder {

    private static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmssn");

    private static final int START_ROW = 3;

    private final Map<Integer, Row> ROW_CACHE = new HashMap<>();

    private final File file;
    private final HSSFWorkbook workbook;
    private final Sheet sheet;

    public DocumentBuilder() throws IOException {
        this.file = createFile();
        this.workbook = new HSSFWorkbook();
        this.sheet = workbook.createSheet("Sheet");
    }

    @Override
    public File build() throws IOException {
        saveToFile();
        return file;
    }

    @Override
    public IDocumentBuilder withAbsentPeople(Set<String> names) {
        Row headerRow = getOrElseCreateRow(DocumentBuilder.START_ROW);

        Cell headerCell = getOrElseCreateCell(headerRow, 0);
        sheet.setColumnWidth(0, 25 * 256);
        headerCell.setCellValue("Afmelders");

        int index = DocumentBuilder.START_ROW + 1;
        for (var name : names) {
            Row row = getOrElseCreateRow(index++);
            Cell cell = getOrElseCreateCell(row, 0);
            cell.setCellValue(name);
        }
        return this;
    }

    @Override
    public IDocumentBuilder withPresentPeopleAfterAbsence(Set<String> names) {
        Row headerRow = getOrElseCreateRow(DocumentBuilder.START_ROW);

        Cell headerCell = getOrElseCreateCell(headerRow, 2);
        sheet.setColumnWidth(2, 30 * 256);
        headerCell.setCellValue("Terugkomers");

        int index = DocumentBuilder.START_ROW + 1;
        for (var name : names) {
            Row row = getOrElseCreateRow(index++);
            Cell cell = getOrElseCreateCell(row, 2);
            cell.setCellValue(name);
        }

        return this;
    }

    @Override
    public IDocumentBuilder withTotalPeoplePerPackageSize(Map<Integer, Integer> totalPerPackageSize) {
        Row headerRow = getOrElseCreateRow(DocumentBuilder.START_ROW);

        Cell headerCell = getOrElseCreateCell(headerRow, 4);
        sheet.setColumnWidth(4, 25 * 256);
        headerCell.setCellValue("Pakketaantallen");

        int index = DocumentBuilder.START_ROW + 1;
        for (var entry : totalPerPackageSize.entrySet()) {
            var packageSize = entry.getKey();
            var total = entry.getValue();

            String value = String.format("%d x %d", packageSize, total);
            Row row = getOrElseCreateRow(index++);
            Cell cell = getOrElseCreateCell(row, 4);
            cell.setCellValue(value);
        }

        return this;
    }

    @Override
    public IDocumentBuilder withTotalPackagesInTwos(BigDecimal total) {
        int startRow = DocumentBuilder.START_ROW + 8;
        Row headerRow = getOrElseCreateRow(startRow);

        Cell header = getOrElseCreateCell(headerRow, 4);
        header.setCellValue("Aantal pakketten in 2'en");

        Row cellRow = getOrElseCreateRow(startRow + 1);
        Cell cell = getOrElseCreateCell(cellRow, 4);
        cell.setCellValue(total.toString());

        return this;
    }

    @Override
    public IDocumentBuilder withConversionDate() {
        Row headerRow = getOrElseCreateRow(0);
        Cell header = getOrElseCreateCell(headerRow, 0);
        header.setCellValue("Conversiedatum");

        String value = LocalDate.now().format(DateFormatting.DUTCH_DATE_TIME_FORMATTER);
        Row cellRow = getOrElseCreateRow(1);
        Cell cell = getOrElseCreateCell(cellRow, 0);
        cell.setCellValue(value);

        return this;
    }

    @Override
    public IDocumentBuilder withDeliveryDate(LocalDate date) {
        Row headerRow = getOrElseCreateRow(0);
        Cell header = getOrElseCreateCell(headerRow, 2);
        header.setCellValue("Leverdatum");

        String value = date.format(DateFormatting.DUTCH_DATE_TIME_FORMATTER);

        Row cellRow = getOrElseCreateRow(1);
        Cell cell = getOrElseCreateCell(cellRow, 2);
        cell.setCellValue(value);

        return this;
    }

    private void saveToFile() throws IOException {
        try (OutputStream fileOut = new FileOutputStream(file)) {
            workbook.write(fileOut);
        }
    }

    private File createFile() throws IOException {
        String dateTimeString = DATE_TIME_FORMATTER.format(LocalDateTime.now());
        String fileName = String.format("Aantallen-%s", dateTimeString);
        return File.createTempFile(fileName, ".xls");
    }

    private Row getOrElseCreateRow(int rownum) {
        Optional<Row> row = Optional.ofNullable(ROW_CACHE.get(rownum));
        if (row.isPresent()) {
            return row.get();
        }

        Row createdRow = sheet.createRow(rownum);
        ROW_CACHE.put(rownum, createdRow);
        return createdRow;
    }

    private Cell getOrElseCreateCell(Row row, int cellnum) {
        Optional<Cell> cell = Optional.ofNullable(row.getCell(cellnum));
        return cell.orElse(row.createCell(cellnum));
    }

}
