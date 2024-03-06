package org.example.factory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

import org.example.builder.DocumentBuilder;
import org.example.reader.DocumentReader;

public class DocumentFactory implements IDocumentFactory {

    private final DocumentReader reader;

    public DocumentFactory(File file) {
        this.reader = new DocumentReader(file);
    }

    @Override
    public File createFromDate(LocalDate date) throws IOException {
        DocumentBuilder builder = new DocumentBuilder();
        Set<String> absentPeopleNames = reader.readAbsentPeopleNames(date);
        Set<String> presentPeopleAfterAbsenceNames = reader.readPresentPeopleAfterAbsenceNames(date);
        int totalPackagesInTwos = reader.readTotalPackagesInTwos(date);
        Map<Integer, Integer> totalPeoplePerPackageSize = reader.readTotalPeoplePerPackageSize(date);

        return builder
                .withAbsentPeople(absentPeopleNames)
                .withPresentPeopleAfterAbsence(presentPeopleAfterAbsenceNames)
                .withTotalPackagesInTwos(totalPackagesInTwos)
                .withTotalPeoplePerPackageSize(totalPeoplePerPackageSize)
                .build();
    }

}
