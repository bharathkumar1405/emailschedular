package com.project.emailSchedular.controller;

import com.project.emailSchedular.response.SheetData;
import com.project.emailSchedular.service.EmailService;
import com.project.emailSchedular.service.EmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.List;
@Controller
@RequestMapping("/")
public class EmployeeExcelController
{
    @Autowired
    EmployeeService employeeServiceExcelImpl;
    @Autowired
    EmailService emailService;
    Logger log= LoggerFactory.getLogger(EmployeeExcelController.class);
    @RequestMapping
    public String getAllEmployees(Model model)    {
        log.info("getAllEmployees  on {}", LocalDateTime.now());
        List<SheetData> sheetDataList = employeeServiceExcelImpl.getAllEmployees();
        model.addAttribute("sheetDataList", sheetDataList);
        return "index";
    }

    @RequestMapping("/sendEmail")
    public String sendBirthdayEmail(Model model)    {
        log.info("sendEmail Triggered on {}", LocalDateTime.now());
        List<SheetData> emailSentList =  employeeServiceExcelImpl.sendBirthdayEmail();
        model.addAttribute("sheetDataList", emailSentList);
        return "list-employees";
    }

}