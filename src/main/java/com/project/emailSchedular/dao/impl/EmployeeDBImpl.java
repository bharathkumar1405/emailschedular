package com.project.emailSchedular.dao.impl;

import com.project.emailSchedular.dao.EmployeeRepository;
import com.project.emailSchedular.response.SheetData;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EmployeeDBImpl implements EmployeeRepository {
    @Override
    public List<SheetData> getAllEmployees() {
        return null;
    }

    @Override
    public SheetData getTodaysBirthdayEmployees() {
        return null;
    }
}
