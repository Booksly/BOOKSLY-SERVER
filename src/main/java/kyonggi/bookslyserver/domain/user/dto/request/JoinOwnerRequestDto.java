package kyonggi.bookslyserver.domain.user.dto.request;

import jakarta.validation.constraints.NotNull;
import kyonggi.bookslyserver.domain.user.validation.annotation.CheckLoginIdFormat;
import kyonggi.bookslyserver.domain.user.validation.annotation.CheckPasswordStrength;
import kyonggi.bookslyserver.domain.user.validation.annotation.NotFalse;
import kyonggi.bookslyserver.domain.user.validation.annotation.VerifyPhoneNum;

public record JoinOwnerRequestDto(
        @NotNull @CheckLoginIdFormat String loginId,
        @NotNull @CheckPasswordStrength String password,
        @NotNull String businessNumber,
        @NotNull @VerifyPhoneNum String phoneNum,
        @NotNull @NotFalse Boolean isVerify,
        @NotNull String email
) { }
