package kyonggi.bookslyserver.domain.user.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kyonggi.bookslyserver.domain.user.validation.annotation.CheckLoginIdFormat;
import kyonggi.bookslyserver.global.error.ErrorCode;

public class CheckLoginIdFormatValidator implements ConstraintValidator<CheckLoginIdFormat, String> {
    @Override
    public void initialize(CheckLoginIdFormat constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String loginId, ConstraintValidatorContext context) {
        if (loginId == null) {
            return false;
        }

        // 조건 1: 아이디는 최소한 6글자 이상
        if (loginId.length() < 6) {
            addConstraintViolation(context, ErrorCode.ID_LENGTH_BAD_REQUEST.getMessage());
            return false;
        }

        // 조건 2: 숫자를 하나 이상 포함
        if (!loginId.matches(".*\\d+.*")) {
            addConstraintViolation(context, ErrorCode.MUST_INCLUDE_NUMBER.getMessage());
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}