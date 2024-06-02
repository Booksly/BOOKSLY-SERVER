package kyonggi.bookslyserver.domain.shop.dto.response.shop;

import com.fasterxml.jackson.annotation.JsonInclude;
import kyonggi.bookslyserver.domain.shop.dto.response.menu.MenuReadDto;
import lombok.Builder;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public record ShopUserReadOneDto(
    String Name,
    String category,
    AddressDto address,
    String description,
    String detailAddress,
    String phoneNumber,
    String kakaoUrl,
    String instagramUrl,
    String blogUrl,
    Float rating,
    List<BusinessScheduleDto> businessSchedules
) {
}
