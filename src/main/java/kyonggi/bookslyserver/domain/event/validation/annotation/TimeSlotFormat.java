package kyonggi.bookslyserver.domain.event.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kyonggi.bookslyserver.domain.event.validation.validator.TimeSlotFormatValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TimeSlotFormatValidator.class)
@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.METHOD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeSlotFormat {
    String message() default "00:00-23:59 형식으로 입력해주세요.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
