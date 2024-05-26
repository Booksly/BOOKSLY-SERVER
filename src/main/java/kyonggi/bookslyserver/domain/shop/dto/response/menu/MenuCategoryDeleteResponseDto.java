package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import kyonggi.bookslyserver.domain.shop.entity.Menu.MenuCategory;
import lombok.Data;

@Data
public class MenuCategoryDeleteResponseDto {
    private Long id;

    public MenuCategoryDeleteResponseDto(MenuCategory menuCategory){
        this.id = menuCategory.getId();
    }
}
