package kyonggi.bookslyserver.domain.user.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kyonggi.bookslyserver.domain.user.validation.annotation.NotFalse;
import kyonggi.bookslyserver.global.error.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class NotFalseValidator implements ConstraintValidator<NotFalse, Boolean> {
    @Override
    public void initialize(NotFalse constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(Boolean verify, ConstraintValidatorContext context) {
        if (!verify) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(ErrorCode.VERIFY_IDENTITY.getMessage()).addConstraintViolation();

            return false;
        }

        return true;
    }
}
