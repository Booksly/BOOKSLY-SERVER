package kyonggi.bookslyserver.domain.user.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kyonggi.bookslyserver.domain.user.validation.validator.VerifyPhoneNumValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = VerifyPhoneNumValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface VerifyPhoneNum {
    String message() default "전화번호를 올바르게 입력해주세요";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
