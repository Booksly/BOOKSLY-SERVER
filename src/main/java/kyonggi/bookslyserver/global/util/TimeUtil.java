package kyonggi.bookslyserver.global.util;

import org.springframework.stereotype.Component;

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

}
