package kyonggi.bookslyserver.domain.event.dto.response;

import lombok.Builder;

@Builder
public record DeleteTimeEventResponseDto(
        Long deletedId
) {
    public static DeleteTimeEventResponseDto of(Long id) {
        return DeleteTimeEventResponseDto.builder()
                .deletedId(id).build();
    }
}