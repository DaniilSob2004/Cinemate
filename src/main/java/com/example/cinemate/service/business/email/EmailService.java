package com.example.cinemate.service.business.email;

import com.example.cinemate.model.EmailContext;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public interface EmailService {

    @NotBlank(message = "Email should not be blank")
    @Email
    void sendEmail(EmailContext emailContext);
}
