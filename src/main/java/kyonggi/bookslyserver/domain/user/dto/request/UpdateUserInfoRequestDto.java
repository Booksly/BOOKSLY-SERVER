package kyonggi.bookslyserver.domain.user.dto.request;

import kyonggi.bookslyserver.domain.user.validation.annotation.VerifyPhoneNum;

public record UpdateUserInfoRequestDto(
        @VerifyPhoneNum String phoneNumber,
        String nickname
) {
}
