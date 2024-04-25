package kyonggi.bookslyserver.domain.shop.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record MenuCategoryCreateDto(
        @NotNull String categoryName
) {
}
