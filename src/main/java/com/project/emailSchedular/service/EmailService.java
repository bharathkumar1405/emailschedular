package com.project.emailSchedular.service;

import com.project.emailSchedular.config.EmailFields;
import com.project.emailSchedular.response.MailResponse;
import com.project.emailSchedular.response.SheetData;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;
    @Autowired
    JavaMailSender sender;
    @Value("${spring.mail.username}")
    String fromEmail;
    @Value("${spring.mail.images.path}")
    String imagePath;

    @Value("${spring.mail.img.extension}")
    String emailImgExt;

    @Value("${spring.mail.img.default}")
    String defaultImg;


    @Autowired
    EmailFields fields;

    Logger log= LoggerFactory.getLogger(EmailService.class);
    public List<SheetData.Rows> sendBirthdayEmail(List<SheetData.Rows> emailSending) {
            for (SheetData.Rows row : emailSending) {
                if(StringUtils.isBlank((String) row.getCell().get("STATUS"))) {
                    log.info("sending Email to {}",row.getCell());
                    MailResponse res = sendEmail(row);
                    row.setMailResponse(res);
                }
            }
        return emailSending;
    }


    public MailResponse sendEmail(SheetData.Rows model) {
        MailResponse response = new MailResponse();
        MimeMessage message = sender.createMimeMessage();
        try {
            // set mediaType
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            // add attachment

            String html = buildHtmlFromTemplate(model);
            helper.setTo((String) model.getCell().get(fields.getEmail().toUpperCase()));
            helper.setText(html, true);
            helper.setSubject((String) model.getCell().get(fields.getSubject().toUpperCase()));
            if(StringUtils.isBlank(fields.getFromName())) {
                helper.setFrom(fromEmail);
            }else{
                helper.setFrom(fromEmail,fields.getFromName() );
            }

            sender.send(message);
            response.setMessage("Mail sent on "+ LocalDateTime.now());
            response.setStatus(Boolean.TRUE);
        } catch (MessagingException |TemplateException | IOException e) {
            response.setMessage("Mail Sending failure : "+e.getMessage());
            response.setStatus(Boolean.FALSE);
            log.error("Error occurred sending email for {}",model.getCell(), e);
        }
        return response;
    }



    public String buildHtmlFromTemplate(SheetData.Rows model) throws TemplateException, IOException {
        model.getCell().put("IMAGE", "");
        //deafult image
        File defaultFile = new File(imagePath+ defaultImg+fields.getImageExtension());
        if(defaultFile.exists()){
            byte[] fileContent = FileUtils.readFileToByteArray(defaultFile);
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
            model.getCell().put("IMAGE", emailImgExt+encodedString);
        }

        //Id based image
        File file = new File(imagePath+ model.getCell().get("ID")+fields.getImageExtension());

        if(file.exists()) {
            byte[] fileContent = FileUtils.readFileToByteArray(file);
            String encodedString = Base64.getEncoder().encodeToString(fileContent);
            model.getCell().put("IMAGE", emailImgExt+encodedString);
        }

        Template freemarkerTemplate = freemarkerConfigurer.getConfiguration()
                .getTemplate((String) model.getCell().get(fields.getTemplate().toUpperCase()));
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, model.getCell());
        return htmlBody;
    }
}
