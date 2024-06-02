package kyonggi.bookslyserver.domain.shop.converter;

import kyonggi.bookslyserver.domain.review.entity.Review;
import kyonggi.bookslyserver.domain.shop.dto.response.shop.BusinessScheduleDto;
import kyonggi.bookslyserver.domain.shop.dto.response.shop.ShopOwnerDetailReadOneDto;
import kyonggi.bookslyserver.domain.shop.dto.response.shop.ShopUserReadOneDto;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.entity.Shop.ShopImage;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ShopConverter {

    public ShopUserReadOneDto toShopUserReadOneDto(Shop shop) {
        shop.setTotalVisitors(shop.getTotalVisitors() + 1);
        return ShopUserReadOneDto.builder()
                .Name(shop.getName())
                .category(shop.getCategory().getCategoryName().toString())
                .profileImg(findRepresentativeImageUrl(shop))
                .rating(shop.getRatingByReview())
                .description(shop.getIntroduction())
                .detailAddress(shop.getDetailAddress())
                .phoneNumber(shop.getPhoneNumber())
                .businessSchedules(
                        shop.getBusinessSchedules().stream()
                                .map(BusinessScheduleDto::new)
                                .toList()
                )
                .address(shop.getAddress().getFirstAddress()+" "+ shop.getAddress().getSecondAddress() + " " + shop.getAddress().getThirdAddress())
                .blogUrl(shop.getBlogUrl())
                .instagramUrl(shop.getInstagramUrl())
                .kakaoUrl(shop.getKakaoUrl())
                .build();
    }

    public ShopOwnerDetailReadOneDto toShopOwnerDetailReadOneDto(Shop shop) {
        return ShopOwnerDetailReadOneDto.builder()
                .name(shop.getName())
                .reviewAvg(calculateReviewAvg(shop))
                .reviewNum(shop.getReviews() != null ? shop.getReviews().size() : 0)
                .ownerId(shop.getShopOwner().getUser().getLoginId())
                .description(shop.getIntroduction())
                .address(shop.getAddress().getFirstAddress() + " " + shop.getAddress().getSecondAddress() + " " + shop.getAddress().getThirdAddress())
                .detailAddress(shop.getDetailAddress())
                .phoneNum(shop.getPhoneNumber())
                .businessSchedules(
                        shop.getBusinessSchedules().stream()
                                .map(BusinessScheduleDto::new)
                                .toList()
                )
                .instagramUrl(shop.getInstagramUrl())
                .kakaoUrl(shop.getKakaoUrl())
                .blogUrl(shop.getBlogUrl())
                .profileImg(findRepresentativeImageUrl(shop))
                .build();
    }

    private String findRepresentativeImageUrl(Shop shop) {
        Optional<ShopImage> img = shop.getShopImages().stream()
                .filter(ShopImage::getIsRepresentative)
                .findFirst();
        return img.map(ShopImage::getImgUri).orElse(null);
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
}
