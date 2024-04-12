package kyonggi.bookslyserver.domain.user.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kyonggi.bookslyserver.domain.user.validation.validator.CheckLoginIdFormatValidator;
import kyonggi.bookslyserver.domain.user.validation.validator.NotFalseValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CheckLoginIdFormatValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckLoginIdFormat {
    String message() default "아이디는 필수 입력값입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
