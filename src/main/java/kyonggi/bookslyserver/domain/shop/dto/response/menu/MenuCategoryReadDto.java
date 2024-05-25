package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import kyonggi.bookslyserver.domain.shop.entity.Menu.MenuCategory;
import lombok.Data;

@Data
public class MenuCategoryReadDto {
    private Long id;
    private String categoryName;

    public MenuCategoryReadDto(MenuCategory menuCategory){
        this.id = menuCategory.getId();
        this.categoryName = menuCategory.getName();
    }
}
