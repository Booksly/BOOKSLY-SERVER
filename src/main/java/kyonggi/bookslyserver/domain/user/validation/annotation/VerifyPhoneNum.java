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
    String message() default "전화번호 입력란에는 01012345678과 같은 형태만 입력가능합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
