package kyonggi.bookslyserver.domain.shop.dto.response.shop;

import kyonggi.bookslyserver.domain.shop.constant.CategoryName;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import lombok.Data;

@Data
public class NewShopFilterDto {
    private Long id;
    private String imgUri;
    private String name;
    private CategoryName categoryName;
    private AddressDto address;

    public NewShopFilterDto(Shop shop){
        this.id = shop.getId();
        this.imgUri = shop.getShopImages().get(0).getImgUri();
        this.name = shop.getName();
        this.categoryName = shop.getCategory().getCategoryName();
        this.address = new AddressDto(shop.getAddress());
    }
}
