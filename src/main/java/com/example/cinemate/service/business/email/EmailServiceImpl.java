package com.example.cinemate.service.business.email;

import com.example.cinemate.exception.common.EmailSendException;
import com.example.cinemate.model.email.EmailContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Objects;

@Service
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Value("${spring.application.name}")
    private String appName;

    private final JavaMailSender emailSender;

    public EmailServiceImpl(JavaMailSender emailSender) {
        this.emailSender = emailSender;
    }

    @Override
    public void sendEmail(EmailContext emailContext) {
        try {
            // валидация входных данных
            Objects.requireNonNull(emailContext.getTo(), "Email recipient is required");
            Objects.requireNonNull(emailContext.getSubject(), "Email subject is required");
            Objects.requireNonNull(emailContext.getMessage(), "Email message is required");

            // создание письма
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            var helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(new InternetAddress(fromEmail, appName));
            helper.setTo(emailContext.getTo());
            helper.setSubject(emailContext.getSubject());
            helper.setText(emailContext.getMessage(), true);

            emailSender.send(mimeMessage);
        } catch (Exception e) {
            Logger.error("Send email error ({}): {}", emailContext, e.getMessage());
            throw new EmailSendException("Send email error: " + e.getMessage());
        }
    }
}
