package kyonggi.bookslyserver.domain.shop.dto.request;

import lombok.Builder;

import java.util.List;

@Builder
public record EmployeeCreateRequestDto(
        String employeeName,
        String description,
        String imgUri,
        List<String> menus,

        List<EmployeeWorkScheduleDto> workSchedules
) {
}
