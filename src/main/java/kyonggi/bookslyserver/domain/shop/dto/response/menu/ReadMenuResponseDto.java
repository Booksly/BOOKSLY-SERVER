package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Builder;

@Builder
public record ReadMenuResponseDto(
        Long menuId,
        String imgUri,
        int price,
        String menuName,
        String description
) {
    public static ReadMenuResponseDto of(Menu menu) {
        return ReadMenuResponseDto.builder()
                .menuId(menu.getId())
                .imgUri(menu.getMenuImage() != null ? menu.getMenuImage().getMenuImgUri() : null)
                .price(menu.getPrice())
                .menuName(menu.getMenuName())
                .description(menu.getDescription()).build();
    }
}
