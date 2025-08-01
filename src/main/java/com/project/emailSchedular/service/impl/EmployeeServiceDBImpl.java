package com.project.emailSchedular.service.impl;

import com.project.emailSchedular.dao.EmployeeRepository;
import com.project.emailSchedular.response.SheetData;
import com.project.emailSchedular.service.EmailService;
import com.project.emailSchedular.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeServiceDBImpl implements EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    EmailService emailService;
    @Override
    public List<SheetData> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }

    @Override
    public List<SheetData> sendBirthdayEmail() {
        SheetData  todaysSheet = employeeRepository.getTodaysBirthdayEmployees();
        if(todaysSheet != null && !todaysSheet.getRows().isEmpty()){
            emailService.sendBirthdayEmail(todaysSheet.getRows());
            return List.of(todaysSheet);
        }
        return employeeRepository.getAllEmployees();
    }
}
