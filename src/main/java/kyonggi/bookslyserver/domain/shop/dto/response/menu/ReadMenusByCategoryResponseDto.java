package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import kyonggi.bookslyserver.domain.shop.entity.Menu.MenuCategory;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record ReadMenusByCategoryResponseDto(
        String categoryName,
        List<ReadMenuResponseDto> menus
) {
    public static ReadMenusByCategoryResponseDto of(MenuCategory menuCategory) {
        return ReadMenusByCategoryResponseDto.builder()
                .categoryName(menuCategory.getName())
                .menus(menuCategory.getMenus().stream().map(menu -> ReadMenuResponseDto.of(menu)).collect(Collectors.toList()))
                .build();
    }
}
