package me.luoxiao.transformer.nodep.constraint.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import me.luoxiao.transformer.nodep.constraint.web.MultipartFileConstraint;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.regex.Pattern;

@Slf4j
public class MultipartFileValidator implements ConstraintValidator<MultipartFileConstraint, MultipartFile> {
    private boolean requireFilename;
    private long maxFileSize;
    private Pattern filenamePattern;

    @Override
    public void initialize(MultipartFileConstraint constraint) {
        requireFilename = constraint.requireFilename();
        maxFileSize = parseDataSize(constraint.maxFileSize());
        if (!ObjectUtils.isEmpty(constraint.filenamePattern())) {
            filenamePattern = Pattern.compile(constraint.filenamePattern());
        }
    }

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        HibernateConstraintValidatorContext unwrap = null;
        if (context instanceof HibernateConstraintValidatorContext) {
            unwrap = context.unwrap(HibernateConstraintValidatorContext.class);
            eagerlySetVariable(unwrap);
        }
        String originalFilename = value.getOriginalFilename();
        if (maxFileSize > 0 && value.getSize() > maxFileSize) {
            if (unwrap != null) {
                unwrap.addExpressionVariable("maxFileSize_actual", readable(value.getSize()));
            }
            return false;
        }
        if (requireFilename) {
            if (ObjectUtils.isEmpty(originalFilename)) {
                if (unwrap != null) {
                    unwrap.addExpressionVariable("requireFilename_actual", true);
                }
                return false;
            }
        }
        if (originalFilename != null && filenamePattern != null
            && !filenamePattern.matcher(originalFilename).matches()) {
            if (unwrap != null) {
                unwrap.addExpressionVariable("regex_actual", originalFilename);
            }
            return false;
        }
        return true;
    }

    private long parseDataSize(String dataSize) {
        if (dataSize == null || dataSize.isEmpty()) {
            return 0;
        }
        try {
            return Long.parseLong(dataSize);
        } catch (Exception ignored) {
        }

        dataSize = dataSize.toUpperCase().trim();
        if (!dataSize.matches("^\\d+(KB|MB|GB|TB)$")) {
            log.warn("invalid dataSize={}, expect KB or MB or GB or TB", dataSize);
            return 0;
        }
        int multiplierIndex = dataSize.length() - 2;
        long size = Long.parseLong(dataSize.substring(0, multiplierIndex));
        String unit = dataSize.substring(multiplierIndex);

        return switch (unit) {
            case "KB" -> size * 1024;
            case "MB" -> size * 1024 * 1024;
            case "GB" -> size * 1024 * 1024 * 1024;
            case "TB" -> size * 1024L * 1024L * 1024L * 1024L;
            default -> 0;
        };
    }

    private String readable(long dataSize) {
        if (dataSize < 0) {
            return "Invalid Size";
        }
        if (dataSize == 0) {
            return "0B";
        }

        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int unitIndex = (int) (Math.log10(dataSize) / Math.log10(1024));
        if (unitIndex >= units.length) {
            unitIndex = units.length - 1;
        }

        double readableSize = dataSize / Math.pow(1024, unitIndex);
        return String.format("%.2f%s", readableSize, units[unitIndex]);
    }

    private void eagerlySetVariable(HibernateConstraintValidatorContext context) {
        context.addExpressionVariable("maxFileSize_actual", "");
        context.addExpressionVariable("requireFilename_actual", false);
        context.addExpressionVariable("regex_actual", "");
    }
}
