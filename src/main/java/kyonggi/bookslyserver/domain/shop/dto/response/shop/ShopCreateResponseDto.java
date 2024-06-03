package kyonggi.bookslyserver.domain.shop.dto.response.shop;

import com.fasterxml.jackson.annotation.JsonInclude;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.shop.entity.Shop.ShopImage;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class ShopCreateResponseDto {
    private String name;

    private String introduction;

    private String address;

    private String phoneNumber;

    private String kakaoUrl;

    private String instagramUrl;

    private String blogUrl;

    private List<BusinessScheduleDto> businessScheduleList;

    private String mainImage;

    public ShopCreateResponseDto(Shop shop){
        name = shop.getName();
        introduction = shop.getIntroduction();
        address = shop.getAddress().getFirstAddress() + " "
                    + shop.getAddress().getSecondAddress() + " "
                    + shop.getAddress().getThirdAddress() + shop.getDetailAddress();
        phoneNumber = shop.getPhoneNumber();
        kakaoUrl = shop.getKakaoUrl();
        instagramUrl = shop.getInstagramUrl();
        blogUrl=shop.getBlogUrl();

        for(ShopImage shopImage : shop.getShopImages()){
            if(shopImage.getIsRepresentative()){
                mainImage = shopImage.getImgUri();
            }
        }
        businessScheduleList = shop.getBusinessSchedules().stream().map(b -> new BusinessScheduleDto(b)).collect(Collectors.toList());

    }


}
