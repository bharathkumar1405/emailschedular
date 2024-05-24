package com.project.emailSchedular.service;

import com.project.emailSchedular.config.EmailFields;
import com.project.emailSchedular.helper.EmployeeHelper;
import com.project.emailSchedular.response.SheetData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EmployeeService {
    @Value("${excel.file-path}") String excelFilePath;

    @Autowired
    EmailFields fields;

    @Autowired
    ExcelService excelService;

    @Autowired
    EmailService emailService;

    @Autowired
    EmployeeHelper employeeHelper;

    @Value("${date.pattern}")
    String datePattern;
    Logger log= LoggerFactory.getLogger(EmployeeService.class);
    @Cacheable
    public List<SheetData> getAllEmployees()
    {
        List<SheetData> sheetDataList = excelService.getDataFromExcel();
        log.info("getAllEmployees  Size {}", sheetDataList.get(0).getRows().size());
        LocalDate today= LocalDate.now();
        SheetData todaysData = employeeHelper.filterOndateAndAddBirthdaydata(sheetDataList,today);
        log.info("today Email  Size {}", todaysData.getRows().size());
        SheetData nextTwentyDays = employeeHelper.filterDateBetweenAndAddBirthdaydata(sheetDataList,today,today.plusDays(21));
        SheetData thisMonth = employeeHelper.filterandAddThisMonthBirthdaydata(sheetDataList, today.getMonth());

        if (todaysData != null) sheetDataList.add(0, todaysData);
        if(nextTwentyDays!=null) sheetDataList.add(1,nextTwentyDays);
        if(thisMonth!=null) sheetDataList.add(2,thisMonth);
        return sheetDataList;
    }

    public List<SheetData> sendBirthdayEmail() {
        List<SheetData> sheetDataList = excelService.getDataFromExcel();
        SheetData todaysSheet = employeeHelper.filterOndateAndAddBirthdaydata(sheetDataList,LocalDate.now());
        if(todaysSheet != null && !todaysSheet.getRows().isEmpty()){
            emailService.sendBirthdayEmail(todaysSheet.getRows());
            excelService.writeStatusTosheet(todaysSheet);
            todaysSheet.setSheetName("TodaysentEmail");
            return List.of(todaysSheet);
        }
        return null;
    }
}
