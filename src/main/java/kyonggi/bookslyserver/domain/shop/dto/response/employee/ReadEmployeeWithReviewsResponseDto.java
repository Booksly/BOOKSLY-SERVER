package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import kyonggi.bookslyserver.domain.shop.entity.Employee.Employee;
import lombok.Builder;

@Builder
public record ReadEmployeeWithReviewsResponseDto(
        Long id,
        String name,
        String profileImage,
        String description,
        Integer numberOfReviews
) {
    public static ReadEmployeeWithReviewsResponseDto of(Employee employee, Boolean withReviews, Integer reviewCount) {
        return ReadEmployeeWithReviewsResponseDto.builder()
                .id(employee.getId())
                .name(employee.getName())
                .profileImage(employee.getProfileImgUri())
                .description(employee.getSelfIntro())
                .numberOfReviews(withReviews ? reviewCount : null).build();
    }
}
