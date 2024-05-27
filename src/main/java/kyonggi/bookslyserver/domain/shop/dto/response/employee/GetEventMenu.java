package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Builder;

@Builder
public record GetEventMenu(
        Long menuId,
        String menuName
) {
    public static GetEventMenu of(Menu menu) {
        return GetEventMenu.builder()
                .menuId(menu.getId())
                .menuName(menu.getMenuName()).build();
    }
}
