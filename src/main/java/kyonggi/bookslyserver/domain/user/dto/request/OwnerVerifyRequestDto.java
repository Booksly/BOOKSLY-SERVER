package kyonggi.bookslyserver.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;

public record OwnerVerifyRequestDto(
        @NotNull String code
) {
}
