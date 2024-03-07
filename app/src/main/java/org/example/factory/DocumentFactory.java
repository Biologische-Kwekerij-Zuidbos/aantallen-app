package org.example.factory;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.builder.DocumentBuilder;
import org.example.reader.DocumentReader;

public class DocumentFactory implements IDocumentFactory {

    private static final Logger LOGGER = LogManager.getLogger(DocumentFactory.class);

    private final DocumentReader reader;

    public DocumentFactory(File file) throws IOException {
        this.reader = new DocumentReader(file);
    }

    @Override
    public File createFromDate(LocalDate date) throws IOException {
        Set<String> absentPeopleNames = reader.readAbsentPeopleNames(date);
        Set<String> presentPeopleAfterAbsenceNames = reader.readPresentPeopleAfterAbsenceNames(date);
        BigDecimal totalPackagesInTwos = reader.readTotalPackagesInTwos(date);
        Map<Integer, Integer> totalPeoplePerPackageSize = reader.readTotalPeoplePerPackageSize(date);

        LOGGER.error("Absent people names: " + absentPeopleNames);
        LOGGER.error("Present people after absence names: " + presentPeopleAfterAbsenceNames);
        LOGGER.error("Total packages in 2s: " + totalPackagesInTwos);
        LOGGER.error("Total people per package size: " + totalPeoplePerPackageSize);

        return new DocumentBuilder()
                .withAbsentPeople(absentPeopleNames)
                .withPresentPeopleAfterAbsence(presentPeopleAfterAbsenceNames)
                .withTotalPackagesInTwos(totalPackagesInTwos)
                .withTotalPeoplePerPackageSize(totalPeoplePerPackageSize)
                .build();
    }

}
