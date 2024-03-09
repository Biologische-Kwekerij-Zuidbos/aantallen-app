/* (C)2024 */
package org.example.factory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

public interface IDocumentFactory {

    public File createFromDate(LocalDate date) throws IOException;
}
