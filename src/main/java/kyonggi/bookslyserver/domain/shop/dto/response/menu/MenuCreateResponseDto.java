package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Builder;

@Builder
public record MenuCreateResponseDto(
        Long menuId
) {
    public static MenuCreateResponseDto of(Menu menu) {
        return MenuCreateResponseDto.builder()
                .menuId(menu.getId()).build();
    }
}
