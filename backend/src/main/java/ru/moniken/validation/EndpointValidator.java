package ru.moniken.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.net.URI;
import java.net.URISyntaxException;

public class EndpointValidator implements ConstraintValidator<Endpoint, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) return false;
        try {
            URI u = new URI(s);
            if (u.isAbsolute()) return false;
            if (!u.getPath().equals(s)) return false;
        } catch (URISyntaxException e) {
            return false;
        }

        return true;
    }
}
