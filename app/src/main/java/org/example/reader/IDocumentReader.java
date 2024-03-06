package org.example.reader;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public interface IDocumentReader {
    
    public Set<String> readAbsentPeopleNames(LocalDate date);

    public Set<String> readPresentPeopleAfterAbsenceNames(LocalDate date);

    public Map<Integer, Integer> readTotalPeoplePerPackageSize(LocalDate date);

    public int readTotalPackagesInTwos(LocalDate date);

}
