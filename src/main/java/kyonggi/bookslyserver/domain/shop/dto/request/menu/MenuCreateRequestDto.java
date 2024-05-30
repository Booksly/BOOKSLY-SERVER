package kyonggi.bookslyserver.domain.shop.dto.request.menu;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;


public record MenuCreateRequestDto(
        @NotNull String menuName,
        @NotNull int price,
        @NotNull @Lob String description,
        @NotNull Long menuCategoryId,
        MultipartFile menuImg
) {

}
