package kyonggi.bookslyserver.domain.shop.dto.request.shop;

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

    @NotNull private String phoneNumber;//@VerifyPhoneNum 어노테이션 걸어주세요.(전화번호 형식 검증)

    @NotNull private String businessNumber;

    @NotNull private String firstAddress;

    @NotNull private String secondAddress;

    @NotNull private String thirdAddress;

    @NotNull private String detailAddress;

    private String kakaoUrl;

    private String instagramUrl;

    private String introduction;

    //DTO에서 엔티티를 직접 사용하는 것은 유연성, 책임, 보안 측면에서 좋지 않습니다. BusinessScheduleRequestDto와 같이 dto를 만들어주세요.
    @NotNull private List<BusinessSchedule> businessScheduleList;

    //이미지는 가게 대표이미지 하나만 등록합니다.
    //DTO에서 엔티티를 직접 사용하는 것은 유연성, 책임, 보안 측면에서 좋지 않습니다. dto를 사용해주세요.
    @NotNull private List<ShopImage> shopImageList;

    private TimeUnit timeUnit;

    //자동 예약 확정 여부를 받는 필드가 누락된 것 같습니다.
}
