package me.luoxiao.transformer.nodep.constraint.validator;

import me.luoxiao.transformer.nodep.constraint.web.MultipartFileConstraint;
import me.luoxiao.transformer.support.testing.BaseIntegrationTest;
import org.hibernate.validator.HibernateValidatorConfiguration;
import org.hibernate.validator.cfg.ConstraintMapping;
import org.hibernate.validator.internal.engine.ConfigurationImpl;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationConfigurationCustomizer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.test.context.ContextConfiguration;

@ImportAutoConfiguration(value = {
        ValidationAutoConfiguration.class
})
@EnableAutoConfiguration
@ContextConfiguration(classes = {
        BaseValidatorTest.ValidationTestConfig.class
})
public class BaseValidatorTest extends BaseIntegrationTest {
    @Override
    protected String pathPrefix() {
        return "";
    }

    @TestConfiguration
    public static class ValidationTestConfig {
        @Bean
        public MessageSource messageSource() {
            ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
            messageSource.setDefaultEncoding("UTF-8");
            return messageSource;
        }

        @Bean
        ValidationConfigurationCustomizer idpSystemCertValidationConfigurationCustomizer() {
            return configuration -> {
                HibernateValidatorConfiguration hvc = ((ConfigurationImpl) configuration)
                        .allowOverridingMethodAlterParameterConstraint(true);
                ConstraintMapping mapping = hvc.createConstraintMapping();
                mapping.constraintDefinition(MultipartFileConstraint.class)
                        .validatedBy(MultipartFileValidator.class);
                hvc.addMapping(mapping);
            };
        }

    }
}
