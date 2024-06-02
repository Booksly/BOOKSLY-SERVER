package kyonggi.bookslyserver.domain.shop.dto.response.shop;

import com.fasterxml.jackson.annotation.JsonInclude;
import kyonggi.bookslyserver.domain.shop.dto.response.menu.MenuReadDto;
import lombok.Builder;

import java.util.List;
@Builder
public record ShopUserReadOneDto(
    String Name,
    String category,
    String imageUrl,
    AddressDto address,
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
