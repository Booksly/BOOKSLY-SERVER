package kyonggi.bookslyserver.domain.user.dto.response;

import lombok.Builder;

@Builder
public record VerifyCodeResponseDto(
        boolean isVerify
) {
}
