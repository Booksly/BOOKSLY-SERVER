package kyonggi.bookslyserver.domain.user.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kyonggi.bookslyserver.domain.user.validation.validator.CheckPasswordValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CheckPasswordValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckPasswordStrength {
    String message() default "비밀번호는 필수 입력값입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
