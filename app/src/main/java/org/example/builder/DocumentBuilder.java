package org.example.builder;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Set;

public class DocumentBuilder implements IDocumentBuilder {

    private static DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.nnnnnn'Z'");

    private final File file;

    public DocumentBuilder() throws IOException {
        this.file = createFile();
    }

    @Override
    public File build() {
        return file;
    }

    @Override
    public IDocumentBuilder withAbsentPeople(Set<String> names) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withAbsentPeople'");
    }

    @Override
    public IDocumentBuilder withPresentPeopleAfterAbsence(Set<String> names) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withPresentPeopleAfterAbsence'");
    }

    @Override
    public IDocumentBuilder withTotalPeoplePerPackageSize(Map<Integer, Integer> totalPerPackageSize) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withTotalPeoplePerPackageSize'");
    }

    @Override
    public IDocumentBuilder withTotalPackagesInTwos(int total) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withTotalPackagesInTwos'");
    }

    private File createFile() throws IOException {
        String dateTimeString = DATE_TIME_FORMATTER.format(LocalDate.now());
        String fileName = String.format("Aantallen-%s", dateTimeString);
        return File.createTempFile(fileName, ".xslx");
    }
    
}
