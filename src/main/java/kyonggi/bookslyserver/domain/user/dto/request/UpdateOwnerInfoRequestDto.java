package kyonggi.bookslyserver.domain.user.dto.request;

import kyonggi.bookslyserver.domain.user.validation.annotation.CheckPasswordStrength;
import kyonggi.bookslyserver.domain.user.validation.annotation.VerifyPhoneNum;

public record UpdateOwnerInfoRequestDto(
        String currentPassword,
        @CheckPasswordStrength String newPassword,
        @VerifyPhoneNum String phoneNumber,
        String email
) {
}
