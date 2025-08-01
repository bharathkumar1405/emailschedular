package com.project.emailSchedular.schedular;

import com.project.emailSchedular.service.EmployeeService;
import com.project.emailSchedular.service.impl.EmployeeServiceDBImpl;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class Schedular {

    @Scheduled(cron = "${cron.expression}")
    public void runJob(){
        System.out.println("Batch Job Run ON "+ LocalDateTime.now());
        EmployeeService employeeService= new EmployeeServiceDBImpl();
        employeeService.sendBirthdayEmail();
        System.out.println("batch job END ON "+ LocalDateTime.now());
    }
}
