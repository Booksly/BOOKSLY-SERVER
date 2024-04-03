package kyonggi.bookslyserver.domain.user.dto.response;

import kyonggi.bookslyserver.domain.user.entity.ShopOwner;
import lombok.Builder;

@Builder
public record JoinOwnerResponseDto(
        Long shopOwnerId
) {
    public static JoinOwnerResponseDto of(ShopOwner shopOwner) {
        return JoinOwnerResponseDto.builder()
                .shopOwnerId(shopOwner.getId())
                .build();
    }
}
