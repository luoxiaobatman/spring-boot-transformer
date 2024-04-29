package me.luoxiao.transformer.nodep.constraint.validator;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import me.luoxiao.transformer.nodep.constraint.web.MultipartFileConstraint;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SuppressWarnings("CodeBlock2Expr")
@ContextConfiguration(classes = {
        MultipartFileValidatorTest.SimService.class
})
class MultipartFileValidatorTest extends BaseValidatorTest {
    @Autowired
    SimService simService;

    @Test
    public void multipartFileValidatorOK() {
        assertDoesNotThrow(() -> {
            simService.simMaxFileSize10Bytes(multipartFile());
        });

        assertThrows(ConstraintViolationException.class, () -> {
            simService.simMaxFileSize10Bytes(multipartFile(11));
        });

        assertThatThrownBy(() -> simService.simMaxFileSize10Bytes(multipartFile(11)))
                .isInstanceOf(ConstraintViolationException.class);

        assertThrows(ConstraintViolationException.class, () -> {
            simService.simMaxFileSize1MB(multipartFile(1024 + 1));
        });

        assertDoesNotThrow(() -> {
            simService.simMaxFileSize10Bytes(multipartFile("sim"));
        });

        assertThatThrownBy(() -> simService.simMaxFileSizeRegex(multipartFile("notSim")))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    public void i18nTest() throws Exception {
        assertThat(true).isFalse();
    }

    @Service
    @Validated
    public static class SimService {
        public void simMaxFileSize10Bytes(@Valid
                                          @MultipartFileConstraint(
                                                  maxFileSize = "10"
                                          )
                                          MultipartFile file) {
        }

        public void simMaxFileSize1MB(@Valid
                                      @MultipartFileConstraint(
                                              maxFileSize = "1KB"
                                      )
                                      MultipartFile file) {
        }

        public void simMaxFileSizeRegex(@Valid
                                        @MultipartFileConstraint(
                                                filenamePattern = "^sim"
                                        )
                                        MultipartFile file) {
        }
    }
}
