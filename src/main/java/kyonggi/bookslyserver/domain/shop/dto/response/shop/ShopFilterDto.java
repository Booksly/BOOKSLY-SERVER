package kyonggi.bookslyserver.domain.shop.dto.response.shop;

import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import lombok.Data;

@Data
public class ShopFilterDto {
    private Long id;
    private String name;


    public ShopFilterDto(Shop shop){
        this.id = shop.getId();
        this.name = shop.getName();
    }
}
