package kyonggi.bookslyserver.domain.user.dto.response;

import kyonggi.bookslyserver.domain.user.entity.FavoriteShop;
import lombok.Builder;

@Builder
public record DeleteFavoriteShopResponseDto(
        Long favoriteShopId,
        String info
) {
    public static DeleteFavoriteShopResponseDto of(FavoriteShop favoriteShop) {
        return DeleteFavoriteShopResponseDto.builder()
                .favoriteShopId(favoriteShop.getId())
                .info("단골 가게 등록이 해제되었습니다.").build();
    }
}
