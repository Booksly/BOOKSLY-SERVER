package kyonggi.bookslyserver.domain.user.dto.response;

import lombok.Builder;

@Builder
public record GetOwnerLoginIdResponseDto(
        String loginId
) {
    public static GetOwnerLoginIdResponseDto of(String loginId) {
        return GetOwnerLoginIdResponseDto.builder()
                .loginId(loginId)
                .build();
    }
}
