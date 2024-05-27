package kyonggi.bookslyserver.domain.user.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kyonggi.bookslyserver.domain.user.validation.annotation.CheckPasswordStrength;
import kyonggi.bookslyserver.global.error.ErrorCode;

public class CheckPasswordValidator implements ConstraintValidator<CheckPasswordStrength, String> {
    @Override
    public void initialize(CheckPasswordStrength constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String passwd, ConstraintValidatorContext context) {
        if (passwd == null) {
            return true;
        }

        // 조건 1: 비밀번호는 최소한 8글자 이상
        if (passwd.length() < 8) {
            addConstraintViolation(context, ErrorCode.PASSWORD_LENGTH_BAD_REQUEST.getMessage());
            return false;
        }

        // 조건 2: 숫자를 하나 이상 포함
        if (!passwd.matches(".*\\d+.*")) {
            addConstraintViolation(context, ErrorCode.MUST_INCLUDE_NUMBER.getMessage());
            return false;
        }

        // 조건 3: 특수 문자를 하나 이상 포함
        if (!passwd.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\",.<>/?]+.*")) {
            addConstraintViolation(context, ErrorCode.MUST_INCLUDE_SPECIAL_CHAR.getMessage());
            return false;
        }

        return true;
    }

    private void addConstraintViolation(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }
}
