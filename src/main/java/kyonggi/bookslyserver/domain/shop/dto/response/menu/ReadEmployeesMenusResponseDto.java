package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record ReadEmployeesMenusResponseDto(
        String categoryName,
        List<ReadEmployeeMenuResponseDto> menus
) {
    public static ReadEmployeesMenusResponseDto of(String categoryName, List<Menu> menus) {
        return ReadEmployeesMenusResponseDto.builder()
                .categoryName(categoryName)
                .menus(menus.stream().map(menu -> ReadEmployeeMenuResponseDto.of(menu)).collect(Collectors.toList())).build();
    }
}
