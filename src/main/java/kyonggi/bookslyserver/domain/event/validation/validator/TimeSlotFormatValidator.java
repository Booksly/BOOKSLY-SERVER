package kyonggi.bookslyserver.domain.event.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import kyonggi.bookslyserver.domain.event.validation.annotation.TimeSlotFormat;

import java.util.List;
import java.util.regex.Pattern;

public class TimeSlotFormatValidator implements ConstraintValidator<TimeSlotFormat, List<String>> {

    private static final String TIME_RANGE_PATTERN = "^([01][0-9]|2[0-3]):[0-5][0-9]-([01][0-9]|2[0-3]):[0-5][0-9]$";
    private static final Pattern TIME_PATTERN = Pattern.compile(TIME_RANGE_PATTERN);

    @Override
    public void initialize(TimeSlotFormat constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(List<String> timeSlots, ConstraintValidatorContext context) {

        if (timeSlots == null) {
            return true;
        }
        for (String timeSlot : timeSlots) {
            if (!TIME_PATTERN.matcher(timeSlot).matches()) {
                return false;
            }}

        return true;
    }

}
