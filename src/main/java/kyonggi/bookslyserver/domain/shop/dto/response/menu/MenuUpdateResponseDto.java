package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record MenuUpdateResponseDto(
    @NotNull String menuName,
    @NotNull int price,
    @NotNull String description,
    @NotNull String menuCategory,
    @NotNull String imgUrl
) {
}
