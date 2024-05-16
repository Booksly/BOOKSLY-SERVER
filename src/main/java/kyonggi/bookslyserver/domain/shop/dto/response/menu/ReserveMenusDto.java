package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import kyonggi.bookslyserver.domain.shop.entity.Employee.EmployeeMenu;
import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Data;

@Data
public class ReserveMenusDto {
    private Long menuId;

    private Long employeeMenuId;

    private String menuCategoryName;

    private String menuName;

    private int price;

    public ReserveMenusDto(Menu menu, EmployeeMenu employeeMenu){
        this.menuId = menu.getId();
        this.employeeMenuId = employeeMenu.getId();
        this.menuCategoryName = menu.getMenuCategory().getName();
        this.menuName = menu.getMenuName();
        this.price = menu.getPrice();
    }
}
