package kyonggi.bookslyserver.domain.event.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kyonggi.bookslyserver.domain.event.validation.annotation.TimeRange;
import kyonggi.bookslyserver.global.error.ErrorCode;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;

@Slf4j
public class TimeRangeValidator implements ConstraintValidator<TimeRange, String> {

    @Override
    public void initialize(TimeRange constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String timeInput, ConstraintValidatorContext context) {
        if (timeInput == null) {
            return true; // @NotNull 어노테이션을 사용하여 별도로 null 체크
        }
        if (!timeInput.matches("^([01][0-9]|2[0-3]):[0-5][0-9]$")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("올바른 시간 입력 형식이 아닙니다. 09:00와 같은 형식으로 입력해주세요.").addConstraintViolation();

            return false;
        }
        LocalTime localTime = LocalTime.parse(timeInput);
        // LocalTime이 00:00부터 23:59 사이인지 확인
        return !localTime.isBefore(LocalTime.MIN) && !localTime.isAfter(LocalTime.MAX);
    }
}
