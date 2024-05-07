package kyonggi.bookslyserver.domain.event.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CreateClosingEventRequestDto(
        @NotNull Long shopId,
        @NotNull Long employeeId,
        @NotNull String message,
        @NotNull int discountRate,
        @NotNull List<Long> menuIds
) {
}
