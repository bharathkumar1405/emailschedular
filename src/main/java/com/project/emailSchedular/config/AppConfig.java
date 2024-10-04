package com.project.emailSchedular.config;

import com.project.emailSchedular.response.SheetData;
import com.project.emailSchedular.service.ExcelService;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import org.apache.poi.ss.usermodel.Cell;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableScheduling
public class AppConfig {
    @Value("${spring.mail.templates.path}")
    private String mailTemplatesPath;
    @Primary
    @Bean
    public FreeMarkerConfigurer freemarkerClassLoaderConfig() throws IOException {
        freemarker.template.Configuration configuration = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_27);
        TemplateLoader templateLoader = new FileTemplateLoader(new File(mailTemplatesPath));
        //templateResolver.setPrefix(mailTemplatesPath);
        configuration.setTemplateLoader(templateLoader);
        FreeMarkerConfigurer freeMarkerConfigurer = new FreeMarkerConfigurer();
        freeMarkerConfigurer.setConfiguration(configuration);
        return freeMarkerConfigurer;
    }

    @Bean
    JavaMailSender javaMailSender(ExcelService excelService){
        SheetData data= excelService.getSecureDataFromExcel().get(0);
        Map<String, Object> mailDetails=data.getRows().get(0).getCell();
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost((String) mailDetails.get("SMTP"));
        mailSender.setPort((int)Double.parseDouble((String) mailDetails.get("PORT")));

        mailSender.setUsername((String) mailDetails.get("EMAIL"));
        mailSender.setPassword((String) mailDetails.get("PASSWORD"));

//        Properties props = mailSender.getJavaMailProperties();
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.auth", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//        props.put("mail.debug", "true");

        return mailSender;
    }
}
