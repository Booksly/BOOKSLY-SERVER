package kyonggi.bookslyserver.domain.user.dto.response;

import lombok.Builder;

@Builder
public record GetOwnerLoginIdResponseDto(
        String loginId
) {
}
