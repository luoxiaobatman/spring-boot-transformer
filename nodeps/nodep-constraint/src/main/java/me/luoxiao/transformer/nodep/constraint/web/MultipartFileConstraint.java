package me.luoxiao.transformer.nodep.constraint.web;


import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import me.luoxiao.transformer.nodep.constraint.support.NoopConstraint;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * provider should use it to validate {@link org.springframework.web.multipart.MultipartFile}
 */
@Target({PARAMETER,ElementType.FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {})
@NoopConstraint
public @interface MultipartFileConstraint {
    boolean requireFilename() default true;

    /**
     * if no unit provide, Bytes
     * supports: xxKB, xxMB
     */
    String maxFileSize() default "";

    String filenamePattern() default "";

    String message() default "{me.luoxiao.transformer.nodep.constraint.web.MultipartFileConstraint}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
