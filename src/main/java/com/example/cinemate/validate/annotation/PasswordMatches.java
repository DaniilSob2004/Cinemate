package com.example.cinemate.validate.annotation;

import com.example.cinemate.validate.validator.PasswordMatchesValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordMatchesValidator.class)
@Documented
public @interface PasswordMatches {
    String message();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
