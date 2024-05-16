package kyonggi.bookslyserver.domain.shop.dto.response.shop;

import kyonggi.bookslyserver.domain.shop.dto.response.menu.MenuReadDto;
import lombok.Builder;

import java.util.List;

@Builder
public record ShopUserReadOneDto(
    String Name,
    AddressDto address,
    String description,
    String detailAddress,
    String phoneNumber,
    List<BusinessScheduleDto> businessSchedules,

    List<MenuReadDto> menus,

    List<EmployeeUserResponseDto> employees




) {
}
