package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import jakarta.validation.constraints.NotNull;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Builder;

import java.util.List;

@Builder
public record MenuUpdateResponseDto(
    @NotNull String menuName,
    @NotNull int price,
    @NotNull String description,
    @NotNull String menuCategory,
    @NotNull String imgUrl
) {
    public static MenuUpdateResponseDto of(Menu menu) {
        return MenuUpdateResponseDto.builder()
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .description(menu.getDescription())
                .menuCategory(menu.getMenuCategory().getName())
                .imgUrl(menu.getMenuImage() != null ? menu.getMenuImage().getMenuImgUri() : null)
                .build();
    }
}
