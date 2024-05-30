package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Builder;

@Builder
public record ReadEmployeeMenuResponseDto(
        Long id,
        String menuName
) {
    public static ReadEmployeeMenuResponseDto of(Menu menu) {
        return ReadEmployeeMenuResponseDto.builder()
                .id(menu.getId())
                .menuName(menu.getMenuName()).build();
    }
}
