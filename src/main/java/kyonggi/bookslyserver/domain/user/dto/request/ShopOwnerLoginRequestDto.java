package kyonggi.bookslyserver.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;

public record ShopOwnerLoginRequestDto(
        @NotNull String loginId,
        @NotNull String password
) {
}
