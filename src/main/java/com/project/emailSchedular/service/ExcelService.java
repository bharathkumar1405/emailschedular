package com.project.emailSchedular.service;

import com.project.emailSchedular.config.EmailFields;
import com.project.emailSchedular.response.SheetData;
import com.project.emailSchedular.util.CommonUtil;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.project.emailSchedular.util.CommonUtil.getCellValueAsString;

@Service
public class ExcelService {

    @Value("${excel.file-path}") String excelFilePath;

    @Autowired
    EmailFields fields;

    Logger log= LoggerFactory.getLogger(ExcelService.class);
    @Cacheable
    public List<SheetData> getDataFromExcel()
    {
        List<SheetData> sheetDataList = new ArrayList<>();

        try (FileInputStream fileInputStream = new FileInputStream(excelFilePath)) {
            Workbook workbook = WorkbookFactory.create(fileInputStream);

            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                List<String> headers = readHeaders(sheet);
                List<SheetData.Rows> data = readData(sheet,headers);

                sheetDataList.add(new SheetData(sheet.getSheetName(), headers, data));
            }

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception appropriately
            log.error("Failed reading Excel",e);
        }

        return sheetDataList;
    }

    private List<String> readHeaders(Sheet sheet) {
        Row headerRow = sheet.getRow(0);
        List<String> headers = new ArrayList<>();

        for (Cell cell : headerRow) {
            headers.add(cell.getStringCellValue());
        }

        return headers;
    }

    private List<SheetData.Rows> readData(Sheet sheet, List<String> headers) {
        List<SheetData.Rows> data = new ArrayList<>();

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row dataRow = sheet.getRow(i);
            Map<String,Object> rowData = new LinkedHashMap<>();
            SheetData.Rows row=new SheetData.Rows();
            for (int j = 0; j < dataRow.getLastCellNum(); j++) {
                Cell cell = dataRow.getCell(j);
                rowData.put(headers.get(j).toUpperCase(),getCellValueAsString(cell));
            }
            row.setCell(rowData);
            data.add(row);
        }

        return data;
    }
    public void writeStatusTosheet(SheetData sheetData) {
        try (FileInputStream fileInputStream = new FileInputStream(excelFilePath)) {
            Workbook workbook = WorkbookFactory.create(fileInputStream);
            Sheet sheet = CommonUtil.getSheet(workbook,fields.getEmpSheetName());
            List<String> headers = readHeaders(sheet).stream().map(String::toUpperCase).toList();
            if(sheet !=null) {
                DataFormatter dataFormatter = new DataFormatter();
                int columnIndex = headers.indexOf(fields.getStatus().toUpperCase());
                for (Row row : sheet) {
                    String key = dataFormatter.formatCellValue(row.getCell(headers.indexOf(fields.getEmpId().toUpperCase())));
                    for (SheetData.Rows updatedrows : sheetData.getRows()) {
                        if (updatedrows.getCell().get("ID").equals(key) && updatedrows.getMailResponse() != null) {
                            String newValue = String.valueOf(updatedrows.getMailResponse().getMessage());
                            Cell cell = row.getCell(columnIndex);
                            if (cell == null) {
                                cell = row.createCell(columnIndex);
                            }
                            cell.setCellValue(newValue);
                        }
                    }
                }

            FileOutputStream outFile = new FileOutputStream(new File(excelFilePath));
            workbook.write(outFile);
            workbook.close();
            outFile.close();
            }
        } catch (IOException e) {
            log.error("Error occurred while updating excel",e);
        }
    }

}
