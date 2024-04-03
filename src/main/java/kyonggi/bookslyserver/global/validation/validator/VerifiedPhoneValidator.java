package kyonggi.bookslyserver.global.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kyonggi.bookslyserver.global.validation.annotation.VerifiedPhone;

public class VerifiedPhoneValidator implements ConstraintValidator<VerifiedPhone, String> {
    @Override
    public void initialize(VerifiedPhone constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        //발송한 인증

        return false;
    }
}
