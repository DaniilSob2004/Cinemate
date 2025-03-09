package com.example.cinemate.service.business.user;

import com.example.cinemate.dto.auth.ResetPasswordRequestDto;
import com.example.cinemate.exception.auth.UserNotFoundException;
import com.example.cinemate.exception.common.BadRequestException;
import com.example.cinemate.model.email.EmailContent;
import com.example.cinemate.model.email.EmailContext;
import com.example.cinemate.model.db.AppUser;
import com.example.cinemate.service.business.email.EmailContentBuilder;
import com.example.cinemate.service.business.email.EmailService;
import com.example.cinemate.service.business_db.appuserservice.AppUserService;
import com.example.cinemate.service.redis.ResetPasswordTokenStorage;
import com.example.cinemate.utils.GenerateUtil;
import com.example.cinemate.validate.user.UserDataValidate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.tinylog.Logger;

import java.time.LocalDateTime;

@Service
public class UpdatePasswordService {

    @Value("${email_data.reset_password_front_url}")
    private String resetPasswordFrontUrl;

    private final AppUserService appUserService;
    private final EmailService emailService;
    private final EmailContentBuilder emailContentBuilder;
    private final ResetPasswordTokenStorage resetPasswordTokenStorage;
    private final UserDataValidate userDataValidate;
    private final BCryptPasswordEncoder passwordEncoder;

    public UpdatePasswordService(AppUserService appUserService, EmailService emailService, EmailContentBuilder emailContentBuilder, ResetPasswordTokenStorage resetPasswordTokenStorage, UserDataValidate userDataValidate, BCryptPasswordEncoder passwordEncoder) {
        this.appUserService = appUserService;
        this.emailService = emailService;
        this.emailContentBuilder = emailContentBuilder;
        this.resetPasswordTokenStorage = resetPasswordTokenStorage;
        this.userDataValidate = userDataValidate;
        this.passwordEncoder = passwordEncoder;
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

        Logger.info("Reset password token created for user: {} - {}", email, token);

        // отправка письма на эту почту с ссылкой
        String resetLink = resetPasswordFrontUrl + token;
        EmailContent emailContent = emailContentBuilder.resetPasswordEmail(resetLink);
        var emailContext = new EmailContext(email, emailContent.getSubject(), emailContent.getMessage());
        emailService.sendEmail(emailContext);
    }

    public void resetPassword(final ResetPasswordRequestDto resetPasswordRequestDto) {
        // получаем id пользователя по токену из Redis
        String userId = resetPasswordTokenStorage.getUserId(resetPasswordRequestDto.getToken())
                .orElseThrow(() -> new BadRequestException("Token is not valid"));

        // находим пользователя
        AppUser user = appUserService.findByIdWithoutIsActive(Integer.parseInt(userId))
                .orElseThrow(() -> new UserNotFoundException("User with id '" + userId + "' was not found..."));

        // активирован ли пользователь (иначе UserInactiveException)
        userDataValidate.validateIsActiveUser(user);

        // нет ли провайдера (иначе BadRequestException)
        userDataValidate.validateIsNotHaveProvider(user.getEncPassword() == null);

        Logger.info("Reset password for: {}", user.getEmail());

        // сохранение пароля
        user.setEncPassword(passwordEncoder.encode(resetPasswordRequestDto.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        appUserService.save(user);

        // удаление токена
        resetPasswordTokenStorage.remove(resetPasswordRequestDto.getToken());
    }
}
