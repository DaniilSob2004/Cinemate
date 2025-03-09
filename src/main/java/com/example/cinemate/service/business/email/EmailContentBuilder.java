package com.example.cinemate.service.business.email;

import com.example.cinemate.model.email.EmailContent;
import org.springframework.stereotype.Service;

@Service
public class EmailContentBuilder {

    public EmailContent resetPasswordEmail(final String resetLink) {
        String emailSubject = "Reset Password";
        String emailMessage = String.format("""
                <div>
                    <p>Hello,</p>
                    <p>Click the link below to reset your password:</p>
                    <p><a href='%s'>Reset Password</a></p>
                    <p>If you did not request a password reset, please ignore this email.</p>
                </div>
        """, resetLink);
        return new EmailContent(emailSubject, emailMessage);
    }
}
