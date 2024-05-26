package kyonggi.bookslyserver.domain.user.dto.response;

import kyonggi.bookslyserver.domain.user.entity.FavoriteShop;
import lombok.Builder;

@Builder
public record CreateFavoriteShopResponseDto(
        Long favoriteId
) {
    public static CreateFavoriteShopResponseDto of(FavoriteShop favoriteShop) {
        return CreateFavoriteShopResponseDto.builder().favoriteId(favoriteShop.getId()).build();
    }
}
