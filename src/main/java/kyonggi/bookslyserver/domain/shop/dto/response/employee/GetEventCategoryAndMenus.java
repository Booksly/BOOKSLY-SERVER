package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.domain.shop.entity.Menu.MenuCategory;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record GetEventCategoryAndMenus(
        String categoryName,
        Long categoryId,
        List<GetEventMenu> menus
) {
    public static GetEventCategoryAndMenus of(MenuCategory menuCategory, List<Menu> menus) {

        return GetEventCategoryAndMenus.builder()
                .categoryName(menuCategory.getName())
                .categoryId(menuCategory.getId())
                .menus(menus.stream().map(menu -> GetEventMenu.of(menu)).collect(Collectors.toList())).build();
    }
}
