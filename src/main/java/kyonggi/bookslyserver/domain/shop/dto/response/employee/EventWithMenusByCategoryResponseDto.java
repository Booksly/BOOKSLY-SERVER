package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import kyonggi.bookslyserver.domain.shop.entity.Menu.MenuCategory;
import lombok.Builder;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record EventWithMenusByCategoryResponseDto(
        String eventTitle,
        int discountRate,
        List<GetEventCategoryAndMenus> categories
) {
    public static EventWithMenusByCategoryResponseDto of(String eventTitle, int discountRate, List<Menu> menus) {
        Map<MenuCategory, List<Menu>> menusByCategory = menus.stream().collect(Collectors.groupingBy(Menu::getMenuCategory));
        Set<MenuCategory> categories = menusByCategory.keySet();

        return EventWithMenusByCategoryResponseDto.builder()
                .eventTitle(eventTitle)
                .discountRate(discountRate)
                .categories(categories.stream().map(menuCategory ->
                        GetEventCategoryAndMenus.of(menuCategory, menusByCategory.get(menuCategory))).collect(Collectors.toList()))
                .build();
    }
}
