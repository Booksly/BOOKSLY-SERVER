package kyonggi.bookslyserver.global.error;

import kyonggi.bookslyserver.global.error.dto.ErrorResponseDto;
import kyonggi.bookslyserver.global.error.dto.FieldErrorResponseDto;

public interface BaseErrorCode {
    ErrorResponseDto getErrorReason();
    FieldErrorResponseDto getFieldErrorReason(Object result);
}
