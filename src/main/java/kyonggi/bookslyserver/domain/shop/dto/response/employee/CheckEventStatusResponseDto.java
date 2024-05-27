package kyonggi.bookslyserver.domain.shop.dto.response.employee;

import lombok.Builder;

@Builder
public record CheckEventStatusResponseDto(
        boolean status
) {
    public static CheckEventStatusResponseDto of(boolean status) {
        return CheckEventStatusResponseDto.builder().status(status).build();
    }
}
