package kyonggi.bookslyserver.domain.shop.dto.request.shop;

import kyonggi.bookslyserver.domain.shop.constant.CategoryName;
import kyonggi.bookslyserver.domain.shop.entity.BusinessSchedule.BusinessSchedule;
import kyonggi.bookslyserver.domain.shop.entity.Shop.ShopImage;

import java.util.List;

public record ShopUpdateRequestDto(
        String name,
        CategoryName category,
        String phoneNumber,
        String businessNumber,
        String firstAddress,
        String secondAddress,
        String thirdAddress,
        String detailAddress,
        String kakaoUrl,
        String instagramUrl,
        String blogUrl,
        String introduction,
        List<BusinessSchedule> businessScheduleList,
        List<ShopImage> shopImageList

) {
}
