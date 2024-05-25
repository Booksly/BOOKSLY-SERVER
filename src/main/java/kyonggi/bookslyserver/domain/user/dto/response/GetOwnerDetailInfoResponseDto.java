package kyonggi.bookslyserver.domain.user.dto.response;

import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import lombok.Builder;

@Builder
public record GetOwnerDetailInfoResponseDto(
        String phoneNumber,
        String email
) {
    public static GetOwnerDetailInfoResponseDto of(ShopOwner shopOwner) {
        return GetOwnerDetailInfoResponseDto.builder()
                .phoneNumber(shopOwner.getUser().getPhoneNum())
                .email(shopOwner.getUser().getEmail()).build();
    }
}
