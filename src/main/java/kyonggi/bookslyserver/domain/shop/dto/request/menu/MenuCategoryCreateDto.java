package kyonggi.bookslyserver.domain.shop.dto.request.menu;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MenuCategoryCreateDto(
        @NotNull String categoryName
) {
}
