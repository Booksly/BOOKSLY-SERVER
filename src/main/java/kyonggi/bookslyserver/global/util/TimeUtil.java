package kyonggi.bookslyserver.global.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;

@Component
public class TimeUtil {
    public static final String TIME_RANGE_PATTERN = "^([01][0-9]|2[0-3]):[0-5][0-9]-([01][0-9]|2[0-3]):[0-5][0-9]$";
    public static final String TIME_PATTERN = "^([01][0-9]|2[0-3]):[0-5][0-9]$";
    public static final String TIME_SEPARATOR = "-";

    public static LocalTime[] parseTimeSlot(String timeSlot) {
        String[] times = timeSlot.split(TIME_SEPARATOR);
        LocalTime startTime = LocalTime.parse(times[0]);
        LocalTime endTime = LocalTime.parse(times[1]);
        return new LocalTime[]{startTime, endTime};
    }

    public static boolean isTimeAfterNow(LocalTime time) {
        if (time.isBefore(LocalTime.now())) return false;
        return true;
    }

    public static boolean checkTimeOrder(LocalTime startTime, LocalTime endTime) {
        if (startTime.isAfter(endTime)) return false;
        return true;
    }

    public static boolean checkDateOrder(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) return false;
        return true;
    }

    public static boolean isDateAfterNow(LocalDate date) {
        if (date.isBefore(LocalDate.now())) return false;
        return true;
    }
}
