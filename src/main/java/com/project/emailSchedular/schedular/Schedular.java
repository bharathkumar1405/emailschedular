package com.project.emailSchedular.schedular;

import com.project.emailSchedular.controller.EmployeeController;
import com.project.emailSchedular.service.EmployeeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.time.LocalDateTime;

@Service
public class Schedular {

    @Scheduled(cron = "${cron.expression}")
    public void runJob(){
        System.out.println("Batch Job Run ON "+ LocalDateTime.now());
        EmployeeService employeeService= new EmployeeService();
        employeeService.sendBirthdayEmail();
        System.out.println("batch job END ON "+ LocalDateTime.now());
    }
}
