package me.luoxiao.transformer.nodep.constraint.support;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NoopValidator implements ConstraintValidator<NoopConstraint, Object> {
    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        return true;
    }

}
