package kyonggi.bookslyserver.domain.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record JoinOwnerRequestDto(
        @NotNull String loginId,
        @NotNull String password,
        @NotNull String businessNumber,
        @NotNull String phoneNum,// 본인인증 검증 어노테이션 추가
        @NotNull String email
) { }
