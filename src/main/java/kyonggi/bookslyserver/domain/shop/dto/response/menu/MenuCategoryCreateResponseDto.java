package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import kyonggi.bookslyserver.domain.shop.entity.Menu.MenuCategory;
import lombok.Data;

@Data
public class MenuCategoryCreateResponseDto {
    private Long id;

    public MenuCategoryCreateResponseDto(MenuCategory menuCategory){
        this.id = menuCategory.getId();
    }
    
}
