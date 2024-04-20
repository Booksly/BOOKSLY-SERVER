package kyonggi.bookslyserver.domain.shop.dto.response;

import kyonggi.bookslyserver.domain.shop.dto.request.MenuCreateRequestDto;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Builder;

@Builder
public record MenuCreateResponseDto(
        Long id
) {
}
