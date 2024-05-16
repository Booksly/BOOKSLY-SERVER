package kyonggi.bookslyserver.domain.shop.dto.response.shop;

import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import lombok.Data;

@Data
public class ReadShopNamesDto {
    private Long id;

    private String name;

    public ReadShopNamesDto(Shop shop){
        this.id = shop.getId();
        this.name = shop.getName();
    }
}
