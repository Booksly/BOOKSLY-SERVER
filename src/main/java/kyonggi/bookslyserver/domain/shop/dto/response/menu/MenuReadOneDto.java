package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Data;

@Data
public class MenuReadOneDto {
    private String imgUri;

    private int price;

    private String menuName;

    private String description;

    private String menuCategory;

    public MenuReadOneDto(Menu menu){
        if(menu.getMenuImage() != null) this.imgUri = menu.getMenuImage().getMenuImgUri();
        this.price = menu.getPrice();
        this.menuName = menu.getMenuName();
        this.description = menu.getDescription();
        this.menuCategory = menu.getMenuCategory().getName();
    }
}
