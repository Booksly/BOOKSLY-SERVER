package kyonggi.bookslyserver.domain.shop.dto.response.shop;

import com.fasterxml.jackson.annotation.JsonInclude;
import kyonggi.bookslyserver.domain.review.entity.Review;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.entity.Shop.ShopImage;

import java.util.List;
import java.util.Optional;

public record ShopOwnerDetailReadOneDto(
        String name,
        float reviewAvg,
        int reviewNum,
        String userId,
        String description,
        String address,
        String detailAddress,
        String phoneNum,
        List<BusinessScheduleDto> businessSchedules,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String instagramUrl,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String kakaoUrl,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        String blogUrl,
        String profileImg
) {
    public ShopOwnerDetailReadOneDto(Shop shop, List<BusinessScheduleDto> businessSchedules) {
        this(
                shop.getName(),
                calculateReviewAvg(shop),
                shop.getReviews() != null ? shop.getReviews().size() : 0,
                shop.getShopOwner().getUser().getLoginId(),
                shop.getIntroduction(),
                shop.getAddress().getFirstAddress() + " " + shop.getAddress().getSecondAddress() + " " + shop.getAddress().getThirdAddress(),
                shop.getDetailAddress(),
                shop.getPhoneNumber(),
                businessSchedules,
                shop.getInstagramUrl(),
                shop.getKakaoUrl(),
                shop.getBlogUrl(),
                findRepresentativeImageUrl(shop)
        );
    }

    private static float calculateReviewAvg(Shop shop) {
        if (shop.getReviews() == null || shop.getReviews().isEmpty()) {
            return 0f;
        }
        float avg = 0f;
        for (Review review : shop.getReviews()) {
            avg += review.getRating();
        }
        return Float.parseFloat(String.format("%.1f", avg / shop.getReviews().size()));
    }
    private static String findRepresentativeImageUrl(Shop shop){
        Optional<ShopImage> img=shop.getShopImages().stream()
                .filter(ShopImage::getIsRepresentative)
                .findFirst();
        return img.map(ShopImage::getImgUri).orElse(null);
    }
}
