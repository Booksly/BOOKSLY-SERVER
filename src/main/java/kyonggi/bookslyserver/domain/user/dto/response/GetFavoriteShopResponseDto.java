package kyonggi.bookslyserver.domain.user.dto.response;

import kyonggi.bookslyserver.domain.shop.entity.Shop.Address;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.user.entity.FavoriteShop;
import lombok.Builder;

@Builder
public record GetFavoriteShopResponseDto(
        Long favoriteShopId,
        Long shopId,
        String shopName,
        String shopImgUri,
        String address,
        String category
) {
    public static GetFavoriteShopResponseDto of(FavoriteShop favoriteShop) {
        Shop shop = favoriteShop.getShop();
        Address address = shop.getAddress();
        return GetFavoriteShopResponseDto.builder()
                .favoriteShopId(favoriteShop.getId())
                .shopId(shop.getId())
                .shopName(shop.getName())
                .shopImgUri(shop.getShopImages().get(0).getImgUri())
                .address(address.getFirstAddress()+" "+address.getSecondAddress() +" "+ address.getThirdAddress())
                .category(shop.getCategory().getCategoryName().toString()).build();
    }
}
