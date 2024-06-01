package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record ReadEmployeeNamesWithImageWrapperResponseDto(
        List<ReadEmployeeNamesWithImageResponseDto> employees
) {
    public static ReadEmployeeNamesWithImageWrapperResponseDto of(List<Employee> employees, boolean withImage) {
        return ReadEmployeeNamesWithImageWrapperResponseDto.builder()
                .employees(employees.stream().map(employee -> ReadEmployeeNamesWithImageResponseDto.of(employee, withImage)).collect(Collectors.toList())).build();
    }
}
