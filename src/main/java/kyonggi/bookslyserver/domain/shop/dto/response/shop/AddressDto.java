package kyonggi.bookslyserver.domain.shop.dto.response.shop;

import kyonggi.bookslyserver.domain.shop.entity.Shop.Address;
import lombok.Data;

@Data
public class AddressDto {
    private String firstAddress;
    private String secondAddress;
    private String thirdAddress;

    public AddressDto(Address address){
        this.firstAddress = address.getFirstAddress();
        this.secondAddress = address.getSecondAddress();
        this.thirdAddress = address.getThirdAddress();
    }

}
