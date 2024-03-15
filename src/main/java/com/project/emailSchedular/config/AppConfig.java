package com.project.emailSchedular.config;

import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.IOException;

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
}
