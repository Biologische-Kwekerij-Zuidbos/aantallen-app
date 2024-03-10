/* (C)2024 */
package org.example.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.example.LocalDateHelper;

public class DocumentReader implements IDocumentReader {

    private static final Logger LOGGER = LogManager.getLogger(DocumentReader.class);

    private static final int PACKAGE_SIZE_COLUMN_INDEX = 5;
    private static final int NAME_COLUMN_INDEX = 1;

    private static final Set<Short> RED_COLORS =
            Set.of(
                    IndexedColors.RED.getIndex(),
                    IndexedColors.RED1.getIndex(),
                    IndexedColors.DARK_RED.getIndex());
    private static final Set<Short> WHITE_COLORS =
            Set.of(
                    IndexedColors.WHITE.getIndex(),
                    IndexedColors.WHITE1.getIndex(),
                    IndexedColors.AUTOMATIC.getIndex());

    private final File file;
    private final List<Row> rows;

    public DocumentReader(File file) throws IOException {
        this.file = file;
        this.rows = readRows();
    }

    @Override
    public Set<String> readAbsentPeopleNames(LocalDate date) throws IOException {
        int dateColumnIndex = readDateColumnIndex(date);
        List<Row> absentPeopleRows =
                rows.stream().filter(filterAbsentPeople(dateColumnIndex)).toList();
        return getNamesFromRows(absentPeopleRows);
    }

    @Override
    public Set<String> readPresentPeopleAfterAbsenceNames(LocalDate date) throws IOException {
        int dateColumnIndex = readDateColumnIndex(date);
        List<Row> presentAfterAbsentPeopleRows =
                rows.stream()
                        .filter(filterAbsentPeople(dateColumnIndex - 1))
                        .filter(filterPresentPeople(dateColumnIndex))
                        .toList();
        return getNamesFromRows(presentAfterAbsentPeopleRows);
    }

    @Override
    public Map<Integer, Integer> readTotalPeoplePerPackageSize(LocalDate date) throws IOException {
        int dateColumnIndex = readDateColumnIndex(date);
        List<Row> presentPeopleRows =
                rows.stream().filter(filterPresentPeople(dateColumnIndex)).toList();
        return presentPeopleRows.stream()
                .collect(
                        Collectors.groupingBy(
                                getPackageSizeFromRow(), Collectors.summingInt(x -> 1)));
    }

    private Function<Row, Integer> getPackageSizeFromRow() {
        return (Row row) -> {
            Cell cell = row.getCell(PACKAGE_SIZE_COLUMN_INDEX);
            return (int) cell.getNumericCellValue();
        };
    }

    @Override
    public BigDecimal readTotalPackagesInTwos(LocalDate date) throws IOException {
        Map<Integer, Integer> totalPeoplePerPackageSize = readTotalPeoplePerPackageSize(date);
        double total =
                totalPeoplePerPackageSize.entrySet().stream()
                        .mapToDouble(calculatePackagesInTwosFromEntry())
                        .sum();
        return BigDecimal.valueOf(total).setScale(1, RoundingMode.HALF_UP);
    }

    private Set<String> getNamesFromRows(List<Row> peopleRows) {
        return peopleRows.stream()
                .map(row -> row.getCell(NAME_COLUMN_INDEX).getStringCellValue())
                .collect(Collectors.toSet());
    }

    private Predicate<? super Row> filterAbsentPeople(int dateColumnIndex) {
        return (Row row) -> {
            Cell cell = row.getCell(dateColumnIndex);
            CellStyle style = cell.getCellStyle();
            short color = style.getFillForegroundColor();
            return RED_COLORS.stream().anyMatch(c -> color == c);
        };
    }

    private Predicate<? super Row> filterPresentPeople(int dateColumnIndex) {
        return (Row row) -> {
            Cell cell = row.getCell(dateColumnIndex);
            CellStyle style = cell.getCellStyle();
            short color = style.getFillForegroundColor();
            return WHITE_COLORS.stream().anyMatch(c -> color == c);
        };
    }

    private List<Row> readRows() throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            try (Workbook workbook = new HSSFWorkbook(inputStream)) {
                Sheet firstSheet = workbook.getSheetAt(0);
                return getRowsFromSheet(firstSheet);
            }
        }
    }

    private int readDateColumnIndex(LocalDate date) throws IOException {
        try (InputStream inputStream = new FileInputStream(file)) {
            try (Workbook workbook = new HSSFWorkbook(inputStream)) {
                Sheet firstSheet = workbook.getSheetAt(0);
                return getDateColumnIndex(firstSheet, date);
            }
        }
    }

    private int getDateColumnIndex(Sheet sheet, LocalDate date) {
        int index = 0;
        Row row = sheet.getRow(0);
        while (index < row.getLastCellNum()) {
            int columnIndex = index++;
            Cell cell = row.getCell(columnIndex);
            try {
                Date value = cell.getDateCellValue();
                LocalDate localDateValue = LocalDateHelper.convertToLocalDateViaInstant(value);
                if (localDateValue.equals(date)) {
                    return columnIndex;
                }
            } catch (Exception e) {
                continue;
            }
        }

        throw new IllegalStateException("Date column index not found");
    }

    private List<Row> getRowsFromSheet(Sheet sheet) {
        List<Row> rows = new ArrayList<>();
        int index = 1;
        while (rowAtIndexIsNotBlank(sheet, index)) {
            Row row = sheet.getRow(index++);
            if (row.getZeroHeight()) continue;
            rows.add(row);
        }

        return rows;
    }

    private boolean rowAtIndexIsNotBlank(Sheet sheet, int index) {
        Row row = sheet.getRow(index);
        if (row == null) return false;
        Cell cell = row.getCell(NAME_COLUMN_INDEX);
        return !cell.getCellType().equals(CellType.BLANK);
    }

    private ToDoubleFunction<? super Map.Entry<Integer, Integer>> calculatePackagesInTwosFromEntry() {
        return entry -> {
            var packageSize = (double) entry.getKey();
            var totalAmountOfPeople = (double) entry.getValue();
            return packageSize / 2d * totalAmountOfPeople;
        };
    }
}
