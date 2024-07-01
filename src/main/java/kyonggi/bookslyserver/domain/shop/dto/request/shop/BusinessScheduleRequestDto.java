package kyonggi.bookslyserver.domain.shop.dto.request.shop;

import jakarta.validation.constraints.NotNull;
import kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule.DayName;

public record BusinessScheduleRequestDto(
        @NotNull DayName day,
        @NotNull String openAt,
        @NotNull String closeAt,
        @NotNull Boolean isHoliday
) {
}
