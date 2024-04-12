package kyonggi.bookslyserver.domain.user.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kyonggi.bookslyserver.domain.user.validation.annotation.VerifyPhoneNum;
import kyonggi.bookslyserver.global.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class VerifyPhoneNumValidator implements ConstraintValidator<VerifyPhoneNum, String> {
    @Override
    public void initialize(VerifyPhoneNum constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {

        if (phoneNumber == null) {
            return false;
        }

        // 전화번호 길이 검사
        if(!(phoneNumber.length() == 10 || phoneNumber.length() == 11)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorCode.PHONE_NUM_LENGTH_BAD_REQUEST.getMessage()).addConstraintViolation();
            return false;
        }

        if (phoneNumber.contains("-")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorCode.HYPHEN_BAD_REQUEST.getMessage()).addConstraintViolation();
            return false;
        }

        return true;
    }
}
