package kyonggi.bookslyserver.domain.shop.dto.response;

import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import lombok.Data;

@Data
public class ShopRegisterDto {
    private Long id;

    public ShopRegisterDto(Shop shop){
        this.id = shop.getId();
    }
}
