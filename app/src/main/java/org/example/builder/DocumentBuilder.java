/* (C)2024 */
package org.example.builder;

import java.awt.Color;
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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.DateFormatting;

public class DocumentBuilder implements IDocumentBuilder {

    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmssn");

    private static final int START_ROW = 4;

    private final Map<Integer, Row> ROW_CACHE = new HashMap<>();

    private final File file;
    private final XSSFWorkbook workbook;
    private final Sheet sheet;

    public DocumentBuilder() throws IOException {
        this.file = createFile();
        this.workbook = new XSSFWorkbook();
        this.sheet = workbook.createSheet("Sheet");
    }

    @Override
    public File build() throws IOException {
        saveToFile();
        return file;
    }

    @Override
    public IDocumentBuilder withAbsentPeople(Set<String> names) {
        sheet.setColumnWidth(0, 25 * 256);
        createHeaderCell(DocumentBuilder.START_ROW, 0, "Afmelders");

        int index = DocumentBuilder.START_ROW + 1;
        for (var name : names) {
            createStripedCell(index++, 0, name);
        }

        return this;
    }

    @Override
    public IDocumentBuilder withPresentPeopleAfterAbsence(Set<String> names) {
        sheet.setColumnWidth(2, 18 * 256);
        createHeaderCell(DocumentBuilder.START_ROW, 2, "Terugkomers");

        int index = DocumentBuilder.START_ROW + 1;
        for (var name : names) {
            createStripedCell(index++, 2, name);
        }

        return this;
    }

    @Override
    public IDocumentBuilder withTotalPeoplePerPackageSize(
            Map<Integer, Integer> totalPerPackageSize) {
        sheet.setColumnWidth(4, 25 * 256);
        createHeaderCell(DocumentBuilder.START_ROW, 4, "Pakketaantallen");

        int index = DocumentBuilder.START_ROW + 1;
        for (var entry : totalPerPackageSize.entrySet()) {
            var packageSize = entry.getKey();
            var total = entry.getValue();

            String value = String.format("%d x %dp", total, packageSize);
            createStripedCell(index++, 4, value);
        }

        return this;
    }

    @Override
    public IDocumentBuilder withTotalPackagesInTwos(BigDecimal total) {
        int startRow = DocumentBuilder.START_ROW + 8;
        createHeaderCell(startRow, 4, "Aantal pakketten in 2'en");
        var value = String.format("%s x 2p", total.toString());
        createCell(startRow + 1, 4, value);
        return this;
    }

    @Override
    public IDocumentBuilder withConversionDate() {
        createHeaderCell(0, 0, "Conversiedatum");
        String value = LocalDate.now().format(DateFormatting.DUTCH_DATE_TIME_FORMATTER);
        createCell(1, 0, value);
        return this;
    }

    @Override
    public IDocumentBuilder withDeliveryDate(LocalDate date) {
        createHeaderCell(0, 2, "Leverdatum");
        String value = date.format(DateFormatting.DUTCH_DATE_TIME_FORMATTER);
        createCell(1, 2, value);
        return this;
    }

    @Override
    public IDocumentBuilder withNarrowGaps() {
        sheet.setColumnWidth(1, 5 * 256);
        sheet.setColumnWidth(3, 5 * 256);
        sheet.setColumnWidth(5, 5 * 256);
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
        return File.createTempFile(fileName, ".xlsx");
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

    private Cell createCell(int rownum, int cellnum, String value) {
        Row row = getOrElseCreateRow(rownum);
        Cell cell = getOrElseCreateCell(row, cellnum);
        cell.setCellValue(value);
        return cell;
    }

    private Cell createHeaderCell(int rownum, int cellnum, String value) {
        Cell cell = createCell(rownum, cellnum, value);
        var style = workbook.createCellStyle();
        var font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        cell.setCellStyle(style);
        return cell;
    }

    private Cell createStripedCell(int rownum, int cellnum, String value) {
        Cell cell = createCell(rownum, cellnum, value);
        if (rownum % 2 == 0) {
            var style = workbook.createCellStyle();
            var color = new XSSFColor(new Color(0xEAEAEA), null);
            style.setFillForegroundColor(color);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            cell.setCellStyle(style);
        }

        return cell;
    }
}
