package kyonggi.bookslyserver.domain.shop.dto.response.employee;


import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Data;

@Data
public class EmployeeReadOneMenuDto {
    private String categoryName;

    private String menuName;

    public EmployeeReadOneMenuDto(Menu menu){
        this.categoryName = menu.getMenuCategory().getName();
        this.menuName = menu.getMenuName();
    }
}
