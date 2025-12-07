package com.guidex.framework.web.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


/**
 * @author kled2
 * @date 2025/3/20
 */
@Service
public class EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    // @Value("${spring.mail.username}")
    // private String fromEmail;
    //
    // @Value("${spring.mail.password}")
    // private String password;

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    /**
     * 发送重置密码邮件
     *
     * @param toEmail
     * @param subject
     * @param text
     */
    public Boolean sendEmail(String toEmail, String subject, String text) {
        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom("noreply@guidex.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(text, true);  // true: 设置为 HTML 格式

            javaMailSender.send(message);
            log.info("Email sent successfully to: " + toEmail);
            return true;
        } catch (MessagingException | MailException e) {
            log.error("Failed to send email: " + e.getMessage());
            return false;
        }
    }
}
