package kyonggi.bookslyserver.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import kyonggi.bookslyserver.domain.user.validation.annotation.VerifyPhoneNum;

public record VerifyCodeRequestDto(
        @NotNull String code,
        @VerifyPhoneNum String receivingNumber
) {
}
