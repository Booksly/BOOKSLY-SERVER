package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Data;

@Data
public class MenuReadDto {
    private Long id;

    private String imgUrl;

    private String menuCategory;

    private String menuName;

    private int price;

    private String description;

    public MenuReadDto(Menu menu){
        this.id = menu.getId();
        if(menu.getMenuImages() != null){
            this.imgUrl = menu.getMenuImages().get(0).getMenuImgUri();
        }
        this.menuCategory = menu.getMenuCategory().getName();
        this.menuName = menu.getMenuName();
        this.price = menu.getPrice();
        this.description = menu.getDescription();
    }
}
