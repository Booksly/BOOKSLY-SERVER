package kyonggi.bookslyserver.domain.user.dto.request;

import kyonggi.bookslyserver.domain.user.validation.annotation.VerifyPhoneNum;

public record SendSMSRequestDto(
        @VerifyPhoneNum String receivingNumber
) {
}
