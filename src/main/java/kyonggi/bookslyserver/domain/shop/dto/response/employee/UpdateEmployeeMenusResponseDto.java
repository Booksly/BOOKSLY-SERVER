package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import kyonggi.bookslyserver.domain.shop.dto.response.menu.ReadMenuResponseDto;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record UpdateEmployeeMenusResponseDto(
        String categoryName,
        List<ReadMenuResponseDto> menus
) {
    public static UpdateEmployeeMenusResponseDto of(String categoryName, List<Menu> menus) {
        return UpdateEmployeeMenusResponseDto.builder()
                .categoryName(categoryName)
                .menus(menus.stream().map(menu -> ReadMenuResponseDto.of(menu)).collect(Collectors.toList()))
                .build();
    }
}
