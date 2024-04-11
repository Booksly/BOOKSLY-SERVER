package kyonggi.bookslyserver.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import kyonggi.bookslyserver.domain.user.validation.annotation.NotFalse;
import kyonggi.bookslyserver.domain.user.validation.annotation.VerifyPhoneNum;

public record JoinOwnerRequestDto(
        @NotNull String loginId,
        @NotNull String password,
        @NotNull String businessNumber,
        @VerifyPhoneNum String phoneNum,
        @NotFalse Boolean isVerify,
        @NotNull String email
) { }
