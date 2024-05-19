package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Data;

@Data
public class EventRegisterMenuNamesDto {
    private Long id;

    private String menuCategoryName;

    private String menuName;

    public EventRegisterMenuNamesDto(Menu menu){
        this.id = menu.getId();
        this.menuCategoryName = menu.getMenuCategory().getName();
        this.menuName = menu.getMenuName();
    }
}
