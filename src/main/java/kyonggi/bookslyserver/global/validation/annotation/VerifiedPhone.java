package kyonggi.bookslyserver.global.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kyonggi.bookslyserver.global.validation.validator.VerifiedPhoneValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = VerifiedPhoneValidator.class)
@Target(value = {ElementType.FIELD,ElementType.METHOD,ElementType.PARAMETER})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface VerifiedPhone {
    String message() default "전화번호 본인인증이 필요합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
