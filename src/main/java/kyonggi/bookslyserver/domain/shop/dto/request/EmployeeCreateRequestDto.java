package kyonggi.bookslyserver.domain.shop.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record EmployeeCreateRequestDto(
        @NotNull String employeeName,
        @NotNull String description,
        @NotNull String imgUri,
        List<String> menus,

        @NotNull List<EmployeeWorkScheduleDto> workSchedules
) {
}
