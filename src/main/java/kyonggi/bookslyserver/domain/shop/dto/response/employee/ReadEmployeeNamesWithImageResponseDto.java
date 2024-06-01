package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import lombok.Builder;

@Builder
public record ReadEmployeeNamesWithImageResponseDto(
        Long id,
        String profileImgUri,
        String name
) {
    public static ReadEmployeeNamesWithImageResponseDto of(Employee employee, boolean withImage) {
        return ReadEmployeeNamesWithImageResponseDto.builder()
                .id(employee.getId())
                .name(employee.getName())
                .profileImgUri(withImage ? employee.getProfileImgUri() : null).build();
    }
}
