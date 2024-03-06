package org.example.reader;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class DocumentReader implements IDocumentReader {

    @Override
    public Set<String> readAbsentPeopleNames(LocalDate date) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readAbsentPeopleNames'");
    }

    @Override
    public Set<String> readPresentPeopleAfterAbsenceNames(LocalDate date) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readPresentPeopleAfterAbsenceNames'");
    }

    @Override
    public Map<Integer, Integer> readTotalPeoplePerPackageSize(LocalDate date) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readTotalPeoplePerPackageSize'");
    }

    @Override
    public int readTotalPackagesInTwos(LocalDate date) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readTotalPackagesInTwos'");
    }
    
}
