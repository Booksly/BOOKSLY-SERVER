package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record ReadEmployeeWithReviewsWrapperResponseDto(
        List<ReadEmployeeWithReviewsResponseDto> employees
) {
    public static ReadEmployeeWithReviewsWrapperResponseDto of(List<ReadEmployeeWithReviewsResponseDto> employees) {
        return ReadEmployeeWithReviewsWrapperResponseDto.builder()
                .employees(employees).build();
    }
}
