package com.project.emailSchedular.service;

import com.project.emailSchedular.config.EmailFields;
import com.project.emailSchedular.response.MailResponse;
import com.project.emailSchedular.response.SheetData;
import com.project.emailSchedular.vo.EmailDetailsVO;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.lang3.ObjectUtils;
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
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailService {

    @Autowired
    private FreeMarkerConfigurer freemarkerConfigurer;
    @Autowired
    JavaMailSender sender;

    @Value("${spring.mail.images.path}")
    String imagePath;
    @Value("${spring.mail.img.default}")
    String defaultImg;

    @Autowired
    EmailDetailsVO emailDetailsVO;

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
            MimeMessageHelper helper = new MimeMessageHelper(message, true,
                    StandardCharsets.UTF_8.name());
            // add attachment

            String to=(String) model.getCell().get(fields.getEmail().toUpperCase());
            if(StringUtils.isNotBlank(to)) {
                String[] toArr = to.split(",");
                helper.setTo(toArr);
            }
            String cc=(String) model.getCell().get(fields.getCc().toUpperCase());
            if(StringUtils.isNotBlank(cc)) {
                String[] ccArr = cc.split(",");
                helper.setCc(ccArr);
            }
            String bcc=(String) model.getCell().get(fields.getBcc().toUpperCase());
            if(StringUtils.isNotBlank(bcc)) {
                String[] bccArr = bcc.split(",");
                helper.setBcc(bccArr);
            }
            String emailBody = getEmailBodyContent(model);
            helper.setText(emailBody, true);
            helper.setSubject((String) model.getCell().get(fields.getSubject().toUpperCase()));
            if(StringUtils.contains(emailBody,"imageContentCID")) {
                File imageContent = (File) model.getCell().get("IMAGE");
                helper.addInline("imageContentCID", imageContent);  // Add CID reference
            }
            if(StringUtils.isBlank(fields.getFromName())) {
                helper.setFrom(emailDetailsVO.getFromEmail());
            }else{
                helper.setFrom(emailDetailsVO.getFromEmail(),fields.getFromName() );
            }

            sender.send(message);
            response.setMessage("Mail sent on "+ LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            response.setStatus(Boolean.TRUE);
        } catch (MessagingException | IOException | TemplateException e) {
            response.setMessage("Mail Sending failure : "+e.getMessage());
            response.setStatus(Boolean.FALSE);
            model.getCell().put("IMAGE","");
            log.error("Error occurred sending email for {}",model.getCell(), e);
        }catch (Exception e) {
            response.setMessage("Mail Sending failure : "+e.getMessage());
            response.setStatus(Boolean.FALSE);
            model.getCell().put("IMAGE","");
            log.error("Error occurred Exception full sending email for {}",model.getCell(), e);
        }
        return response;
    }

    private String getEmailBodyContent(SheetData.Rows model) throws TemplateException, IOException, MessagingException {
        String template = (String) model.getCell().get(fields.getTemplate().toUpperCase());
        String htmlContent = "";
        checkImagePresent(model);
        if(StringUtils.isNotBlank(template)){
            htmlContent = buildHtmlFromTemplate(model);
        }else {
            File imageContent = (File) model.getCell().get("IMAGE");
            if (ObjectUtils.isNotEmpty(imageContent)) {
                htmlContent = "<html><body><img src='cid:imageContentCID' alt='logo'></body></html>";
            } else {
                String textContent = (String) model.getCell().get("CONTENT");
                if (StringUtils.isNotBlank(textContent)) {
                    htmlContent = "<html><body>"+textContent+"</body></html>";
                } else {
                    htmlContent = "<html><body>No content available.</body></html>";
                }
            }
        }
        return htmlContent;
    }


    public String buildHtmlFromTemplate(SheetData.Rows model) throws TemplateException, IOException {
        model.getCell().put("IMAGE", "");
        Template freemarkerTemplate = freemarkerConfigurer.getConfiguration()
                .getTemplate((String) model.getCell().get(fields.getTemplate().toUpperCase()));
        String htmlBody = FreeMarkerTemplateUtils.processTemplateIntoString(freemarkerTemplate, model.getCell());
        return htmlBody;
    }

    private boolean checkImagePresent(SheetData.Rows model) throws IOException {
        String baseFilePath = imagePath + model.getCell().get("ID");
        String defaultFilePath = imagePath+ defaultImg;
        String[] extensions = fields.getImageExtension().split(",");

        // Check for specific image
        if (processImageFiles(baseFilePath, extensions, model)) {
            return true;
        }

        // Check for default image
        return processImageFiles(defaultFilePath, extensions, model);
    }

    private boolean processImageFiles(String filePath, String[] extensions, SheetData.Rows model) throws IOException {
        for (String extension : extensions) {
            File file = new File(filePath +"."+ extension.trim());
            if (file.exists()) {
                // byte[] fileContent = FileUtils.readFileToByteArray(file);
                // String encodedString = Base64.getEncoder().encodeToString(fileContent);
                //  FileSystemResource image = new FileSystemResource(file);
                model.getCell().put("IMAGE", file);

                return true;  // Image found and processed
            }
        }
        return false;  // No image found
    }
}
