package kyonggi.bookslyserver.domain.shop.dto.request.menu;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record UpdateMenuRequestDto(
        String menuName,
        @Min(value = 0) int price,
        String description,
        String menuCategory,
        MultipartFile menuImg
) {
}
