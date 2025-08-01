package com.project.emailSchedular.service;

import com.project.emailSchedular.response.SheetData;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;


public interface EmployeeService {
    List<SheetData> getAllEmployees();

    List<SheetData> sendBirthdayEmail();
}
