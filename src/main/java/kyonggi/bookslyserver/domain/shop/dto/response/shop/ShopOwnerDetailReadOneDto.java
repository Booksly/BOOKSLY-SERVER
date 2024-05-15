package kyonggi.bookslyserver.domain.shop.dto.response.shop;

import kyonggi.bookslyserver.domain.review.entity.Review;
import kyonggi.bookslyserver.domain.shop.dto.BusinessScheduleDto;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import lombok.Data;

import java.util.List;

@Data
public class ShopOwnerDetailReadOneDto {
    private String name;

    private float reviewAvg;

    private int reviewNum;

    private int totalVisitors;

    private int todayVisitors;

    private int favoriteUserNum;

    private String userId;

    private String description;

    private String address;

    private String detailAddress;

    private String phoneNum;

    private List<BusinessScheduleDto> businessSchedules;

    private String instagramUrl;

    private String kakaoUrl;

    private String profileImg;

    public ShopOwnerDetailReadOneDto(Shop shop, List<BusinessScheduleDto> businessSchedules){
        float avg = 0f;
        int reviewNum = 0;
        this.name = shop.getName();

        if(shop.getReviews() != null) {

            for (Review review : shop.getReviews()) {
                avg += review.getRating();
                reviewNum++;
            }
            this.reviewAvg = Float.parseFloat(String.format("%.1f", avg / reviewNum));
        }
        this.totalVisitors = shop.getTotalVisitors();
        this.todayVisitors = shop.getTodayVisitors();
        this.favoriteUserNum = shop.getFavoriteShops().size();
        this.userId = shop.getShopOwner().getUser().getLoginId();
        this.description = shop.getIntroduction();
        this.address = shop.getAddress().getFirstAddress() + " " + shop.getAddress().getSecondAddress() + " " + shop.getAddress().getThirdAddress();
        this.detailAddress = shop.getDetailAddress();
        this.phoneNum = shop.getPhoneNumber();
        this.businessSchedules = businessSchedules;
        this.instagramUrl = shop.getInstagramUrl();
        this.kakaoUrl = shop.getKakaoUrl();
        if(shop.getShopImages() != null) {
            this.profileImg = shop.getShopImages().get(0).getImgUri();
        }

    }


}
