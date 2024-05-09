package kyonggi.bookslyserver.domain.shop.dto.response.shop;

import lombok.Data;

@Data
public class ShopDeleteResponseDto {

    private Long id;
    public ShopDeleteResponseDto(Long id){
        this.id = id;
    }
}
