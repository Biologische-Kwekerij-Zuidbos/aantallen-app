package org.example.factory;

import java.io.File;
import java.time.LocalDate;

public interface IDocumentFactory {
    
    public File createFromDate(LocalDate date);

}
