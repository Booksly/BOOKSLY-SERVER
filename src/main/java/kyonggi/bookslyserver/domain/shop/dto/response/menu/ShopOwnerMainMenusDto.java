package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Data;

@Data
public class ShopOwnerMainMenusDto {
    private String imgUri;

    private String menuName;

    private int price;

    public ShopOwnerMainMenusDto(Menu menu){
        this.imgUri = menu.getMenuImages().get(0).getMenuImgUri();
        this.menuName = menu.getMenuName();
        this.price = menu.getPrice();

    }
}
