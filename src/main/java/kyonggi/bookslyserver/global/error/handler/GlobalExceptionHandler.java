package kyonggi.bookslyserver.global.error.handler;

import kyonggi.bookslyserver.global.error.ErrorCode;
import kyonggi.bookslyserver.global.error.dto.ErrorResponse;
import kyonggi.bookslyserver.global.error.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(">>> handle: MethodArgumentNotValidException ", e);

        Map<String, String> errors = new LinkedHashMap<>();

        e.getBindingResult().getFieldErrors().stream()
                        .forEach(fieldError -> {
                            String fieldName = fieldError.getField();
                            String errorMessage = Optional.ofNullable(fieldError.getDefaultMessage()).orElse("");
                            errors.merge(fieldName, errorMessage, (existingErrorMessage, newErrorMessage) -> existingErrorMessage + ", " + newErrorMessage);
                        });

        final ErrorResponse errorBaseResponse = ErrorResponse.of(ErrorCode.BAD_REQUEST, errors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBaseResponse);
    }

    /**
     * ModelAttribute annotation의 binding error를 handling합니다.
     */
    @ExceptionHandler(BindException.class)
    protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
        log.error(">>> handle: BindException ", e);
        final ErrorResponse errorBaseResponse = ErrorResponse.of(ErrorCode.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBaseResponse);
    }

    /**
     * RequestParam annotation의 binding error를 handling합니다.
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error(">>> handle: MethodArgumentTypeMismatchException ", e);
        final ErrorResponse errorBaseResponse = ErrorResponse.of(ErrorCode.BAD_REQUEST);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBaseResponse);
    }

    /**
     * 지원하지 않는 HTTP method로 요청 시 발생하는 error를 handling합니다.
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.error(">>> handle: HttpRequestMethodNotSupportedException ", e);
        final ErrorResponse errorBaseResponse = ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(errorBaseResponse);
    }

    /**
     * BusinessException을 handling합니다.
     */
    @ExceptionHandler(BusinessException.class)
    protected ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException e) {
        log.error(">>> handle: BusinessException ", e);
        final ErrorCode errorCode = e.getErrorCode();
        final ErrorResponse errorBaseResponse = ErrorResponse.of(errorCode);
        return ResponseEntity.status(errorCode.getHttpStatus()).body(errorBaseResponse);
    }

    /**
     * NoResourceFoundException을 handling합니다.
     */
    @ExceptionHandler(NoResourceFoundException.class)
    protected ResponseEntity<ErrorResponse> handleNoResoureException(final NoResourceFoundException e) {
        log.error(">>> handle: NoResoureException ", e);
        final ErrorResponse errorBaseResponse = ErrorResponse.of(ErrorCode.RESOURCE_NOT_FOUND);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorBaseResponse);
    }


    /**
     * 위에서 정의한 Exception을 제외한 모든 예외를 handling합니다.
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error(">>> handle: Exception ", e);
        final ErrorResponse errorBaseResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorBaseResponse);
    }
}
