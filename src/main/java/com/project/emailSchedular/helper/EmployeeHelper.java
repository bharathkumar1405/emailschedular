package com.project.emailSchedular.helper;

import com.project.emailSchedular.config.EmailFields;
import com.project.emailSchedular.response.SheetData;
import com.project.emailSchedular.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class EmployeeHelper {

    @Autowired
    EmailFields fields;

    @Value("${date.pattern}")
    String datePattern;

    @Value("${spring.mail.images.path}")
    String imagePath;
    Logger log= LoggerFactory.getLogger(EmployeeHelper.class);
    public SheetData filterandAddThisMonthBirthdaydata(List<SheetData> sheetDataList, Month month) {
        Optional<SheetData> empdataOptional = sheetDataList.stream()
                .filter(d -> d.getSheetName().equalsIgnoreCase(fields.getEmpSheetName()))
                .findFirst();

        if(empdataOptional.isPresent()){
            SheetData sheetData = new SheetData();
            log.info("Filtering this months Data {}", month);
            List<SheetData.Rows> thisMonthsList = empdataOptional.get().getRows().stream().filter(d -> {
                String date = (String) d.getCell().get(fields.getDate().toUpperCase());
                LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ofPattern(datePattern));
                return date1.getMonth().equals(month);
            }).toList();
            sheetData.setSheetName(month.toString());
            sheetData.setRows(thisMonthsList);
            sheetData.setHeaders(empdataOptional.get().getHeaders());
            return sheetData;
        }
        return null;
    }

    public SheetData filterOndateAndAddBirthdaydata(List<SheetData> sheetDataList,LocalDate filterOndate) {
        Optional<SheetData> empdataOptional = sheetDataList.stream()
                .filter(d -> d.getSheetName().equalsIgnoreCase(fields.getEmpSheetName()))
                .findFirst();

        if(empdataOptional.isPresent()){
            SheetData sheetData = new SheetData();
            log.info("Filtering on Date Data {}", filterOndate);
            List<SheetData.Rows> thisMonthsList = empdataOptional.get().getRows().stream().filter(d -> {
                String date = (String) d.getCell().get(fields.getDate().toUpperCase());
                LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ofPattern(datePattern));
                return date1.isEqual(filterOndate);
            }).toList();
            sheetData.setSheetName(LocalDate.now().toString());
            sheetData.setRows(thisMonthsList);
            sheetData.setHeaders(empdataOptional.get().getHeaders());
            return sheetData;
        }
        return null;
    }

    public SheetData filterDateBetweenAndAddBirthdaydata(List<SheetData> sheetDataList,LocalDate startDate,LocalDate endDate) {
        Optional<SheetData> empdataOptional = sheetDataList.stream()
                .filter(d -> d.getSheetName().equalsIgnoreCase(fields.getEmpSheetName()))
                .findFirst();

        if(empdataOptional.isPresent()){
            SheetData sheetData = new SheetData();
            log.info("Filtering Date Data  between {} and {}", startDate,endDate);
            List<SheetData.Rows> thisMonthsList = empdataOptional.get().getRows().stream().filter(d -> {
                String date = (String) d.getCell().get(fields.getDate().toUpperCase());
                LocalDate date1 = LocalDate.parse(date, DateTimeFormatter.ofPattern(datePattern));
                return date1.isAfter(startDate) && date1.isBefore(endDate);
            }).map(d -> {
                SheetData.Rows obj = d;
                boolean fileExist = checkImagePresent(d);
                obj.setImagePreset((fileExist)?"":"color" );
                return obj;
            }).toList();
            sheetData.setSheetName("Next 20 Days");
            sheetData.setRows(thisMonthsList);
            sheetData.setHeaders(empdataOptional.get().getHeaders());
            return sheetData;
        }
        return null;
    }

    private boolean checkImagePresent(SheetData.Rows d) {
        String baseFilePath = imagePath + d.getCell().get("ID");
        String[] extensions = fields.getImageExtension().split(",");

        for (String extension : extensions) {
            File file = new File(baseFilePath + extension.trim());
            if (file.exists()) {
                return true;
            }
        }
        return false;
    }

}
