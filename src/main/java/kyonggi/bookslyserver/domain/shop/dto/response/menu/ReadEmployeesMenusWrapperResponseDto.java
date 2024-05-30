package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import lombok.Builder;

import java.util.List;

@Builder
public record ReadEmployeesMenusWrapperResponseDto(
        List<ReadEmployeesMenusResponseDto> categories
) {
    public static ReadEmployeesMenusWrapperResponseDto of(List<ReadEmployeesMenusResponseDto> categories) {
        return ReadEmployeesMenusWrapperResponseDto.builder().categories(categories).build();
    }
}
