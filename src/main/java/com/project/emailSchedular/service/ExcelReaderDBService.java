package com.project.emailSchedular.service;

import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.project.emailSchedular.util.CommonUtil.getCellValueAsString;

@Service
public class ExcelReaderDBService {
    private final JdbcTemplate jdbcTemplate;
    private final String excelFilePath;

    public ExcelReaderDBService(JdbcTemplate jdbcTemplate, @Value("${excel.file-path}") String excelFilePath) {
        this.jdbcTemplate = jdbcTemplate;
        this.excelFilePath = excelFilePath;
    }

    public void readDataFromExcelandsavetoDB() {
        try (FileInputStream fileInputStream = new FileInputStream(excelFilePath)) {
            Workbook workbook = WorkbookFactory.create(fileInputStream);

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);

                // Create or update a table based on sheet name
                String tableName = sheet.getSheetName();
                createOrUpdateTable(tableName, sheet);

                // Read header row for column names
                Row headerRow = sheet.getRow(0);
                List<String> columnNames = readColumnNames(headerRow);

                // Read data rows and update database
                for (int j = 1; j <= sheet.getLastRowNum(); j++) {
                    Row dataRow = sheet.getRow(j);
                    updateData(tableName, columnNames, dataRow);
                }
            }

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
        }
    }

    private List<String> readColumnNames(Row headerRow) {
        List<String> columnNames = new ArrayList<>();
        Iterator<Cell> cellIterator = headerRow.cellIterator();

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            columnNames.add(cell.getStringCellValue());
        }

        return columnNames;
    }

    private void createOrUpdateTable(String tableName, Sheet sheet) {
        StringBuilder createTableQuery = new StringBuilder("CREATE TABLE IF NOT EXISTS " + tableName + " (");

        Row headerRow = sheet.getRow(0);
        Iterator<Cell> cellIterator = headerRow.cellIterator();

        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            String columnName = cell.getStringCellValue();
            createTableQuery.append(columnName).append(" VARCHAR(255), "); // Adjust the column type accordingly
        }

        createTableQuery.setLength(createTableQuery.length() - 2); // Remove the last comma and space
        createTableQuery.append(")");

        jdbcTemplate.execute(createTableQuery.toString());
    }

    private void updateData(String tableName, List<String> columnNames, Row dataRow) {
        StringBuilder updateQuery = new StringBuilder("INSERT INTO " + tableName + " (");

        for (String columnName : columnNames) {
            updateQuery.append(columnName).append(", ");
        }

        updateQuery.setLength(updateQuery.length() - 2); // Remove the last comma and space
        updateQuery.append(") VALUES (");

        for (int i = 0; i < columnNames.size(); i++) {
            Cell cell = dataRow.getCell(i);
            updateQuery.append("'").append(getCellValueAsString(cell)).append("'").append(", "); // Adjust the value handling accordingly
        }

        updateQuery.setLength(updateQuery.length() - 2); // Remove the last comma and space
        updateQuery.append(")");

        jdbcTemplate.update(updateQuery.toString());
    }


}
