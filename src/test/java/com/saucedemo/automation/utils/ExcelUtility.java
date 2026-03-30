package com.saucedemo.automation.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;

public class ExcelUtility {

    private static final Logger log = LogManager.getLogger(ExcelUtility.class);
    private static final String EXCEL_PATH = "src/test/resources/testdata/TestData.xlsx";

    // Returns 2D Object array — plugs directly into @DataProvider
    public static Object[][] getTestData(String sheetName) {

        Object[][] data = null;

        try (FileInputStream fis = new FileInputStream(EXCEL_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new RuntimeException("Sheet not found: " + sheetName);
            }

            int rowCount   = sheet.getPhysicalNumberOfRows();
            int colCount   = sheet.getRow(0).getPhysicalNumberOfCells();

            // Row 0 = headers, so data starts from row 1
            data = new Object[rowCount - 1][colCount];

            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                for (int j = 0; j < colCount; j++) {
                    Cell cell = row.getCell(j);
                    data[i - 1][j] = getCellValue(cell);
                }
            }
            log.info("Excel data loaded from sheet: " + sheetName +
                     " | Rows: " + (rowCount-1));

        } catch (IOException e) {
            log.error("Failed to read Excel: " + e.getMessage());
            throw new RuntimeException("Excel read failed", e);
        }

        return data;
    }

    // Handles different cell types — common source of bugs if not handled
    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING:  return cell.getStringCellValue().trim();
            case NUMERIC: return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default:      return "";
        }
    }
}