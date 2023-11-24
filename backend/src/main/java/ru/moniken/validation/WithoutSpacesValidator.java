package ru.moniken.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class WithoutSpacesValidator implements ConstraintValidator<WithoutSpaces, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (s == null) return true;

        return !s.contains(" ");
    }
}
