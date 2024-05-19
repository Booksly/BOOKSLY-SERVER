package kyonggi.bookslyserver.domain.event.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import kyonggi.bookslyserver.domain.event.validation.validator.TimeRangeValidator;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = TimeRangeValidator.class)
@Target({ ElementType.FIELD, ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface TimeRange {
    String message() default "시간은 00:00부터 23:59 사이여야 합니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
