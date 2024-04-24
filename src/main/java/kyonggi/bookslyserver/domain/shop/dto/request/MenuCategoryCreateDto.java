package kyonggi.bookslyserver.domain.shop.dto.request;

import jakarta.validation.constraints.NotNull;

public record MenuCategoryCreateDto(
        @NotNull String categoryName
) {
}
