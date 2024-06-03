package kyonggi.bookslyserver.domain.shop.dto.response.shop;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;

@Builder
public record ShopOwnerDetailReadOneDto(
        String name,
        float reviewAvg,
        int reviewNum,
        String ownerId,
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

}
