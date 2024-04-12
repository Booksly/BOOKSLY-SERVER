package kyonggi.bookslyserver.global.error.handler;

import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.dto.ErrorResponseDto;
import kyonggi.bookslyserver.global.error.dto.FieldErrorResponseDto;
import kyonggi.bookslyserver.global.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Valid & Validated annotation의 binding error를 handling합니다.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<FieldErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(">>> handle: MethodArgumentNotValidException ", e);

        Map<String, String> errors = new LinkedHashMap<>();

        e.getBindingResult().getFieldErrors().stream()
                        .forEach(fieldError -> {
                            String fieldName = fieldError.getField();
                            log.info("뭐가 나올려나 ~ "+fieldError.getCode());
                            String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
                            errors.merge(fieldName, errorMessage, (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", " + newErrorMessage);
                        });

        HttpStatusCode statusCode = e.getStatusCode();
        log.info("상태코드:"+statusCode);

        final FieldErrorResponseDto fieldErrorReason = ErrorCode.BAD_REQUEST.getFieldErrorReason(errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(fieldErrorReason);
    }

    /**
     * ModelAttribute annotation의 binding error를 handling합니다.
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponseDto> handleBindException(BindException e) {
        log.error(">>> handle: BindException ", e);
        final ErrorResponseDto errorBaseResponse = ErrorCode.BAD_REQUEST.getErrorReason();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBaseResponse);
    }

    /**
     * RequestParam annotation의 binding error를 handling합니다.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error(">>> handle: MethodArgumentTypeMismatchException ", e);
        final ErrorResponseDto errorBaseResponse = ErrorCode.BAD_REQUEST.getErrorReason();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBaseResponse);
    }

    /**
     * 지원하지 않는 HTTP method로 요청 시 발생하는 error를 handling합니다.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponseDto> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error(">>> handle: HttpRequestMethodNotSupportedException ", e);
        final ErrorResponseDto errorBaseResponse = ErrorCode.METHOD_NOT_ALLOWED.getErrorReason();
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorBaseResponse);
    }

    /**
     * BusinessException을 handling합니다.
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponseDto> handleBusinessException(final BusinessException e) {
        log.error(">>> handle: BusinessException ", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponseDto errorBaseResponse = errorCode.getErrorReason();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorBaseResponse);
    }

    /**
     * NoResourceFoundException을 handling합니다.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ErrorResponseDto> handleNoResoureException(final NoResourceFoundException e) {
        log.error(">>> handle: NoResoureException ", e);
        final ErrorResponseDto errorBaseResponse = ErrorCode.BAD_REQUEST.getErrorReason();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBaseResponse);
    }


    /**
     * 위에서 정의한 Exception을 제외한 모든 예외를 handling합니다.
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponseDto> handleException(Exception e) {
        log.error(">>> handle: Exception ", e);
        final ErrorResponseDto errorBaseResponse = ErrorCode.INTERNAL_SERVER_ERROR.getErrorReason();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBaseResponse);
    }
}
