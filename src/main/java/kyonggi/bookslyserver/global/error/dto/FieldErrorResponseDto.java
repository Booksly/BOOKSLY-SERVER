package kyonggi.bookslyserver.global.error.dto;

import kyonggi.bookslyserver.global.error.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Builder
@Getter
public class FieldErrorResponseDto {

    private HttpStatus status;
    private int code;
    private String message;
    private Object result;
}
