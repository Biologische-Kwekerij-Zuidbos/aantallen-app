package org.example;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class WorkbookFactory {
    
    public Workbook createFromFile(File file) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(file);

        try {
            return new HSSFWorkbook(fileInputStream);
        } catch (OfficeXmlFileException e) {
            return new XSSFWorkbook(fileInputStream);
        }
    }

}
