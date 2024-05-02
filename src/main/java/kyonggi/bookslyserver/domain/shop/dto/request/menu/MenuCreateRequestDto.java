package kyonggi.bookslyserver.domain.shop.dto.request.menu;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MenuCreateRequestDto(
        @NotNull String menuName,
        @NotNull int price,
        @NotNull @Lob String description,
        @NotNull String menuCategory,

        @NotNull List<String> menuImgUri
        ) {

}
