package ru.moniken.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EndpointValidator.class)
public @interface Endpoint {
    String message() default "Endpoint syntax error";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
