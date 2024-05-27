package kyonggi.bookslyserver.domain.shop.dto.response.menu;


import kyonggi.bookslyserver.domain.shop.entity.Menu.Menu;
import lombok.Data;

@Data
public class MenuDeleteResponseDto {
    private Long id;

    public MenuDeleteResponseDto(Menu menu){
        this.id = menu.getId();
    }
}
