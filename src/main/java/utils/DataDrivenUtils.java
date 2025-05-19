package utils;

import org.apache.poi.ss.usermodel.Sheet;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


public class DataDrivenUtils {

    public static Object[][] readDataFromExcel(String filePath, String sheetName) {
        try (InputStream file = DataDrivenUtils.class.getClassLoader().getResourceAsStream("data/" + filePath);
             Workbook workbook = new XSSFWorkbook(file)) {

            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet " + sheetName + " not found");
            }
            int rowCount = sheet.getPhysicalNumberOfRows();
            int colCount = sheet.getRow(0).getPhysicalNumberOfCells();

            Object[][] data = new Object[rowCount - 1][colCount-1];

            for (int i = 1; i < rowCount; i++) {
                Row row = sheet.getRow(i);
                for (int j = 1; j < colCount; j++) {
                    data[i - 1][j] = getCellValue(row.getCell(j));
                }
            }

            return data;
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return new Object[0][0]; // Trường hợp đọc lỗi
    }

    private static Object getCellValue(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> cell.getNumericCellValue();
            case BOOLEAN -> cell.getBooleanCellValue();
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
}
