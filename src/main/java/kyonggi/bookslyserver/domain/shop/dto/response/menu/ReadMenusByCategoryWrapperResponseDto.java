package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import kyonggi.bookslyserver.domain.shop.entity.Menu.MenuCategory;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record ReadMenusByCategoryWrapperResponseDto(
        List<ReadMenusByCategoryResponseDto> categories
) {
    public static ReadMenusByCategoryWrapperResponseDto of(List<MenuCategory> menuCategories) {
        return ReadMenusByCategoryWrapperResponseDto.builder().categories(menuCategories.stream()
                .map(menuCategory -> ReadMenusByCategoryResponseDto.of(menuCategory)).collect(Collectors.toList()))
                .build();
    }
}
