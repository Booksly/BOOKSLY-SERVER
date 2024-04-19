package kyonggi.bookslyserver.domain.shop.dto.response;

import jakarta.validation.constraints.NotNull;
import kyonggi.bookslyserver.domain.shop.entity.Shop.Shop;
import kyonggi.bookslyserver.domain.user.dto.response.JoinOwnerResponseDto;
import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import kyonggi.bookslyserver.domain.user.validation.annotation.CheckLoginIdFormat;
import kyonggi.bookslyserver.domain.user.validation.annotation.CheckPasswordStrength;
import kyonggi.bookslyserver.domain.user.validation.annotation.NotFalse;
import kyonggi.bookslyserver.domain.user.validation.annotation.VerifyPhoneNum;
import lombok.Data;

@Data
public class ShopRegisterDto{
    private Long id;

    public ShopRegisterDto(Shop shop){
        this.id = shop.getId();
    }
}

