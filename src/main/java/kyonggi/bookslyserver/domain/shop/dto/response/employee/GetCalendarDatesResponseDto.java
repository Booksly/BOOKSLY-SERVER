package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.List;


@Builder
@Slf4j
public record GetCalendarDatesResponseDto(
        Long employeeId,
        List<LocalDate> workdays,
        List<LocalDate> holidays
) {
    public static GetCalendarDatesResponseDto of(Long id, List<LocalDate> workdays, List<LocalDate> holidays) {

        return GetCalendarDatesResponseDto.builder()
                .employeeId(id)
                .workdays(workdays)
                .holidays(holidays).build();
    }
}
