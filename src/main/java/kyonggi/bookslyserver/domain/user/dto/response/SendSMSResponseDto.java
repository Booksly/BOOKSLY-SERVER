package kyonggi.bookslyserver.domain.user.dto.response;

import lombok.Builder;

@Builder
public record SendSMSResponseDto(
        String sendFrom,
        String sendTo,
        String statusCode,
        String statusMessage
) {
}
