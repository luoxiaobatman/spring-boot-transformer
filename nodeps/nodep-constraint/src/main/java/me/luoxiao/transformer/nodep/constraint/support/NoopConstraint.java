package me.luoxiao.transformer.nodep.constraint.support;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * support for writing annotation like this @Constraint(validatedBy = {})
 * <p>
 * for avoiding the following error
 * <p>
 * Caused by: java.lang.IllegalArgumentException: HV000116: The annotation type must be annotated with @jakarta.validation.Constraint when creating a constraint definition
 */
@Target({ANNOTATION_TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {NoopValidator.class})
public @interface NoopConstraint {
    String message() default "";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
