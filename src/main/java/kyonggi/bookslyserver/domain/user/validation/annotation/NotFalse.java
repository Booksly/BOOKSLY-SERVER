package kyonggi.bookslyserver.domain.user.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kyonggi.bookslyserver.domain.user.validation.validator.NotFalseValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NotFalseValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface NotFalse {
    String message() default "본인 인증이 필요합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
