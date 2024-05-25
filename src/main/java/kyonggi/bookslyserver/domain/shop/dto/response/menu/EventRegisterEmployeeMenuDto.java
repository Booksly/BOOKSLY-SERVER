package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class EventRegisterEmployeeMenuDto {
    private String menuCategoryName;

    private List<MenuDto> menu;

    public EventRegisterEmployeeMenuDto(Menu menu){
        this.menuCategoryName = menu.getMenuCategory().getName();
        this.menu = new ArrayList<>();
    }


    @Data
    public static class MenuDto{
        private Long id;
        private String menuName;

        public MenuDto(Menu menu){
            this.id = menu.getId();
            this.menuName = menu.getMenuName();
        }
    }

}
