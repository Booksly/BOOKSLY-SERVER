package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Data;

@Data
public class MenuReadDto {
    String category;
    String menuName;
    int price;
    String description;

    public MenuReadDto(Menu menu){
        this.category = menu.getMenuCategory().getName();
        this.menuName = menu.getMenuName();
        this.price = menu.getPrice();
        this.description = menu.getDescription();
    }

}
