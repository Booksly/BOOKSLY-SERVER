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

/*
public static JoinOwnerResponseDto of(ShopOwner shopOwner) {
    return JoinOwnerResponseDto.builder()
            .shopOwnerId(shopOwner.getId())
            .build();
}
*/


/*
public record JoinOwnerRequestDto(
        @NotNull @CheckLoginIdFormat String loginId,
        @NotNull @CheckPasswordStrength String password,
        @NotNull String businessNumber,
        @NotNull @VerifyPhoneNum String phoneNum,
        @NotNull @NotFalse Boolean isVerify,
        @NotNull String email
) { }
*/
