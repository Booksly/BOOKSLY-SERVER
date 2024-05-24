package kyonggi.bookslyserver.domain.shop.dto.request;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.NotNull;
import kyonggi.bookslyserver.domain.shop.entity.Menu.MenuCategory;
import org.aspectj.weaver.ast.Not;

import java.util.List;

public record MenuCreateRequestDto(
        @NotNull String menuName,
        @NotNull int price,
        @NotNull @Lob String description,
        @NotNull String menuCategory,

        @NotNull List<String> menuImgUri
        ) {

}
