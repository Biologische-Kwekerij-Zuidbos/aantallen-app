/* (C)2024 */
package org.example.builder;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

/*
 * Document contains:
 *  - Names of absent people
 *  - Total of people per package size
 *  - Total of packages converted to 2s
 *  - Names of people who are present after being absent last week
 */
public interface IDocumentBuilder {

    public File build() throws IOException;

    public IDocumentBuilder withAbsentPeople(Set<String> names);

    public IDocumentBuilder withPresentPeopleAfterAbsence(Set<String> names);

    public IDocumentBuilder withTotalPeoplePerPackageSize(
            Map<Integer, Integer> totalPerPackageSize);

    public IDocumentBuilder withTotalPackagesInTwos(BigDecimal totalPackagesInTwos);

    public IDocumentBuilder withConversionDate();

    public IDocumentBuilder withDeliveryDate(LocalDate date);

    public IDocumentBuilder withNarrowGaps();
}
