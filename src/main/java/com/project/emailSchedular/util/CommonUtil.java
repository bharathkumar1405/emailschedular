package com.project.emailSchedular.util;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

public class CommonUtil {

    public static String getCellValueAsString(Cell cell) {
        if (cell == null) {
            return "";
        }
        String cellValue;
        switch (cell.getCellType()) {
            case STRING:
                cellValue = cell.getStringCellValue();
                break;
            case NUMERIC:
                cellValue = String.valueOf(cell.getNumericCellValue());
                break;
            case BOOLEAN:
                cellValue = String.valueOf(cell.getBooleanCellValue());
                break;
            case FORMULA:
                // Handle formulas if needed
                cellValue = "FORMULA";
                break;
            case BLANK:
                cellValue = "";
                break;
            default:
                cellValue = "UNKNOWN_TYPE";
        }
        return cellValue;
    }

    public static Sheet getSheet(Workbook workbook, String empSheetName){
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            if(workbook.getSheetAt(i).getSheetName().equalsIgnoreCase(empSheetName)){
                return workbook.getSheetAt(i);
            }
        }
        return null;
    }

}
