package kyonggi.bookslyserver.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import kyonggi.bookslyserver.domain.user.validation.annotation.VerifyPhoneNum;

public record SendSMSRequestDto(
        @NotNull @VerifyPhoneNum String receivingNumber
) {
}
