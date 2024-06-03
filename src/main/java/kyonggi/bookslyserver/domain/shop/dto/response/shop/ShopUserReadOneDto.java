package kyonggi.bookslyserver.domain.shop.dto.response.shop;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.util.List;
@Builder
public record ShopUserReadOneDto(
    String Name,
    String category,
    String profileImg,
    String address,
    String description,
    String detailAddress,
    String phoneNumber,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String kakaoUrl,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String instagramUrl,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    String blogUrl,
    Float rating,
    List<BusinessScheduleDto> businessSchedules
) {
}
