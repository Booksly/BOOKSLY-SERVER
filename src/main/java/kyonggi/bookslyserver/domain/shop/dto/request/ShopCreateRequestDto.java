package kyonggi.bookslyserver.domain.shop.dto.request;

import kyonggi.bookslyserver.domain.shop.constant.CategoryName;
import kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule.BusinessSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Shop.ShopImage;
import kyonggi.bookslyserver.domain.shop.entity.Shop.TimeUnit;
import lombok.Data;

import java.util.List;

@Data
public class ShopCreateRequestDto {

    private String Name;

    private CategoryName category;

    private String phoneNumber;

    private String businessNumber;

    private String firstAddress;

    private String secondAddress;

    private String thirdAddress;

    private String detailAddress;

    private String kakaoUrl;

    private String instagramUrl;

    private String introduction;


    private List<BusinessSchedule> businessScheduleList;

    private List<ShopImage> shopImageList;

    private TimeUnit timeUnit;


}
