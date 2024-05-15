package kyonggi.bookslyserver.domain.event.dto.response;

import lombok.Builder;

@Builder
public record DeleteClosingEventResponseDto(
        Long deletedId
) {
    public static DeleteClosingEventResponseDto of(Long id) {
        return DeleteClosingEventResponseDto.builder()
                .deletedId(id).build();
    }
}
