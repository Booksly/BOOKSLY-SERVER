package kyonggi.bookslyserver.domain.shop.dto.request.employee;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
public record EmployeeCreateRequestDto(
        @NotNull String name,
        @NotNull @Length(max = 50) String description,
        MultipartFile image,
        List<Long> menus,
        @NotNull List<EmployeeWorkScheduleDto> workSchedules
) {
}
