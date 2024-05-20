package kyonggi.bookslyserver.domain.shop.dto.response.menu;

import lombok.Data;

@Data
public class ReserveEventMenuDto {
    private String eventName;

    private String menuName;

    private String menuCategory;

    private int price;
    
}
