package com.project.emailSchedular.dao;

import com.project.emailSchedular.response.SheetData;

import java.util.List;

public interface EmployeeRepository {
    List<SheetData> getAllEmployees();

    SheetData getTodaysBirthdayEmployees();
}
