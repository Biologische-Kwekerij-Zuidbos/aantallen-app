package org.example.reader;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public interface IDocumentReader {
    
    public Set<String> readAbsentPeopleNames(LocalDate date) throws IOException;

    public Set<String> readPresentPeopleAfterAbsenceNames(LocalDate date) throws IOException;

    public Map<Integer, Integer> readTotalPeoplePerPackageSize(LocalDate date) throws IOException;

    public BigDecimal readTotalPackagesInTwos(LocalDate date) throws IOException;

}
