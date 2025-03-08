package com.example.cinemate.service.business.user;

import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.model.EmailContext;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.business.email.EmailService;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import com.example.cinemate.service.redis.ResetPasswordTokenStorage;
import com.example.cinemate.utils.GenerateUtil;
import com.example.cinemate.validate.user.UserDataValidate;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

@Service
public class UpdatePasswordService {

    private final AppUserService appUserService;
    private final EmailService emailService;
    private final ResetPasswordTokenStorage resetPasswordTokenStorage;
    private final UserDataValidate userDataValidate;

    public UpdatePasswordService(AppUserService appUserService, EmailService emailService, ResetPasswordTokenStorage resetPasswordTokenStorage, UserDataValidate userDataValidate) {
        this.appUserService = appUserService;
        this.emailService = emailService;
        this.resetPasswordTokenStorage = resetPasswordTokenStorage;
        this.userDataValidate = userDataValidate;
    }

    public void createPasswordResetToken(final String email) {
        // проверить есть ли такой пользователь
        AppUser user = appUserService.findByEmailWithoutIsActive(email)
                .orElseThrow(() -> new UserNotFoundException("User '" + email + "' was not found..."));

        // активирован ли пользователь (иначе UserInactiveException)
        userDataValidate.validateIsActiveUser(user);

        // нет ли провайдера (иначе BadRequestException)
        userDataValidate.validateIsNotHaveProvider(user.getEncPassword() == null);

        // генерация и сохранение токена в Redis с id пользователя
        String token = GenerateUtil.getRandomUuid();
        resetPasswordTokenStorage.addToStorage(token, user.getId().toString());

        Logger.info("Reset password token created for user: {}", email);

        // отправка письма на эту почту с ссылкой
        String resetLink = "https://myfrontend.com/reset-password?token=" + token;
        String emailBody = "<p>Hello,</p>"
                + "<p>Click the link below to reset your password:</p>"
                + "<p><a href='" + resetLink + "'>Reset Password</a></p>"
                + "<p>If you did not request a password reset, please ignore this email.</p>";
        var emailContext = new EmailContext(email, "Reset Password", emailBody);
        emailService.sendEmail(emailContext);
    }
}
