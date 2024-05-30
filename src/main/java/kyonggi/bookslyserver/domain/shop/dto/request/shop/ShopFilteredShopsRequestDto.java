package kyonggi.bookslyserver.domain.shop.dto.request.shop;

import kyonggi.bookslyserver.domain.shop.constant.CategoryName;
import lombok.Data;

import java.util.List;

@Data
public class ShopFilteredShopsRequestDto {
    private String region;

    private List<CategoryName> categories;

    private List<String> times;
}
