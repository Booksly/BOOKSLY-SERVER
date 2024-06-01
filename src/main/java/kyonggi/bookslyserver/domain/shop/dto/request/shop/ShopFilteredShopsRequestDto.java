package kyonggi.bookslyserver.domain.shop.dto.request.shop;

import kyonggi.bookslyserver.domain.shop.constant.CategoryName;
import lombok.Data;

import java.util.List;

@Data
public class ShopFilteredShopsRequestDto {
    private List<String> region;

    private List<Long> categories;

    private List<String> date;

    private List<String> times;
}
