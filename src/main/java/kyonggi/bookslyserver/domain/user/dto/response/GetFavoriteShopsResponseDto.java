package kyonggi.bookslyserver.domain.user.dto.response;

import kyonggi.bookslyserver.domain.user.entity.FavoriteShop;
import lombok.Builder;

import java.util.List;
import java.util.stream.Collectors;

@Builder
public record GetFavoriteShopsResponseDto(
        List<GetFavoriteShopResponseDto> favoriteShops
) {
    public static GetFavoriteShopsResponseDto of(List<FavoriteShop> favoriteShops) {
        return GetFavoriteShopsResponseDto.builder()
                .favoriteShops(favoriteShops.stream().map(favoriteShop -> GetFavoriteShopResponseDto.of(favoriteShop)).collect(Collectors.toList()))
                .build();
    }
}
