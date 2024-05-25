package kyonggi.bookslyserver.domain.user.dto.response;

import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import lombok.Builder;

@Builder
public record GetOwnerProfileResponseDto(
        String loginId,
        String shopImgUri,
        int totalVisitors,
        int todayVisitors,
        int regularCustomerNum
) {
    public static GetOwnerProfileResponseDto of(Shop shop, ShopOwner shopOwner) {
        return GetOwnerProfileResponseDto.builder()
                .loginId(shopOwner.getUser().getLoginId())
                .shopImgUri(shop.getShopImages().get(0).getImgUri())
                .totalVisitors(shop.getTotalVisitors())
                .todayVisitors(shop.getTodayVisitors())
                .regularCustomerNum(shop.getFavoriteShops().size()).build();
    }
}
