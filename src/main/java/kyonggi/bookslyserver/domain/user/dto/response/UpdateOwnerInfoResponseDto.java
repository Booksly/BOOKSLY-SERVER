package kyonggi.bookslyserver.domain.user.dto.response;

import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import lombok.Builder;

@Builder
public record UpdateOwnerInfoResponseDto(
        String phoneNumber,
        String email
) {
    public static UpdateOwnerInfoResponseDto of(ShopOwner shopOwner) {
        return UpdateOwnerInfoResponseDto.builder()
                .phoneNumber(shopOwner.getUser().getPhoneNum())
                .email(shopOwner.getUser().getEmail()).build();
    }
}
