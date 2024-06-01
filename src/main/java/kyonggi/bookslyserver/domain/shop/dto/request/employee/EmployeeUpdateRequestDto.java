package kyonggi.bookslyserver.domain.shop.dto.request.employee;

import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record EmployeeUpdateRequestDto(
        String name,
        @Length(max = 50) String description,
        List<Long> menuIds,
        List<EmployeeWorkScheduleRequestDto> workSchedules
) {
}
