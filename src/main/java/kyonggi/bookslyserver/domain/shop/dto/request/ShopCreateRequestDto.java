package kyonggi.bookslyserver.domain.shop.dto.request;

import jakarta.validation.constraints.NotNull;
import kyonggi.bookslyserver.domain.shop.constant.CategoryName;
import kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule.BusinessSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Shop.ShopImage;
import kyonggi.bookslyserver.domain.shop.entity.Shop.TimeUnit;
import lombok.Data;

import java.util.List;

@Data
public class ShopCreateRequestDto {

    @NotNull private String Name;

    @NotNull private CategoryName category;

    @NotNull private String phoneNumber;

    @NotNull private String businessNumber;

    @NotNull private String firstAddress;

    @NotNull private String secondAddress;

    @NotNull private String thirdAddress;

    @NotNull private String detailAddress;

    private String kakaoUrl;

    private String instagramUrl;

    private String introduction;


    @NotNull private List<BusinessSchedule> businessScheduleList;

    @NotNull private List<ShopImage> shopImageList;

    private TimeUnit timeUnit;


}
