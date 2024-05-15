package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import kyonggi.bookslyserver.domain.shop.entity.Menu.MenuCategory;
import lombok.Data;

@Data
public class MenuCategoryReadDto {
    private String categoryName;

    public MenuCategoryReadDto(MenuCategory menuCategory){
        this.categoryName = menuCategory.getName();
    }
}
