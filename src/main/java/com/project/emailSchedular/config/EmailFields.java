package com.project.emailSchedular.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cell")
@Data
public class EmailFields {

    private String date;
    private String email;
    private String cc;
    private String bcc;
    private String template;
    private String subject;
    private String content;
    private String fromName;
    private String status;
    private String empId;
    private String empSheetName;
    private String imageExtension;
}
