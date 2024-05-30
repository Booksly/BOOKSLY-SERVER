package kyonggi.bookslyserver.domain.shop.dto.response.shop;


import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import lombok.Data;

@Data
public class ShopFilteredShopsResponseDto {
    private Long id;

    private String name;

    private String categoryName;

    private AddressDto address;

    public ShopFilteredShopsResponseDto(Shop shop){
        this.id = shop.getId();
        this.name = shop.getName();
        this.categoryName = shop.getCategory().getCategoryName().name();
        this.address = new AddressDto(shop.getAddress());
    }
}
