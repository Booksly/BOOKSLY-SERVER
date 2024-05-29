package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MenuReadDto {
    private String menuCategoryName;

    private List<MenuDto> menu;

    public MenuReadDto(Menu menu){
        this.menuCategoryName = menu.getMenuCategory().getName();
        this.menu = new ArrayList<>();
    }


    @Data
    public static class MenuDto{
        private Long id;
        private String menuName;
        private String imgUri;
        private int price;
        private String description;

        public MenuDto(Menu menu){
            this.id = menu.getId();
            this.menuName = menu.getMenuName();
            this.imgUri = menu.getMenuImages().get(0).getMenuImgUri();
            this.price = menu.getPrice();
            this.description = menu.getDescription();
        }
    }
}
