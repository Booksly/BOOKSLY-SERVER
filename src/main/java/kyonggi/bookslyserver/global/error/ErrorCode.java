package kyonggi.bookslyserver.global.error;

import kyonggi.bookslyserver.global.error.dto.ErrorResponseDto;
import kyonggi.bookslyserver.global.error.dto.FieldErrorResponseDto;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorCode implements BaseErrorCode{

    /**
     * 400 Bad Request
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NURIGO_BAD_REQUEST(HttpStatus.BAD_REQUEST, "본인 인증 요청 정보를 다시 확인해주세요."),
    HYPHEN_BAD_REQUEST(HttpStatus.BAD_REQUEST, "하이픈을 제외한 숫자만 입력해주세요"),
    PHONE_NUM_LENGTH_BAD_REQUEST(HttpStatus.BAD_REQUEST,"핸드폰 번호는 10자리 또는 11자리만 입력 가능합니다."),
    MUST_INCLUDE_NUMBER(HttpStatus.BAD_REQUEST,"반드시 하나 이상의 숫자를 포함해야 합니다."),
    MUST_INCLUDE_SPECIAL_CHAR(HttpStatus.BAD_REQUEST,"반드시 하나 이상의 특수문자를 포함해야 합니다"),
    ID_LENGTH_BAD_REQUEST(HttpStatus.BAD_REQUEST,"아이디는 최소 6글자여야 합니다."),
    PASSWORD_LENGTH_BAD_REQUEST(HttpStatus.BAD_REQUEST,"비밀번호는 최소 8글자여야 합니다"),
    TIME_SETTING_BAD_REQUEST(HttpStatus.BAD_REQUEST,"시간 또는 분 설정을 해주세요"),
    AUTO_SETTING_BAD_REQUEST(HttpStatus.BAD_REQUEST,"마감 예약 인원을 설정해주세요"),

    /**
     * 401 Unauthorized
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰의 형식이 올바르지 않습니다. Bearer 타입을 확인해 주세요."),
    INVALID_ACCESS_TOKEN_VALUE(HttpStatus.UNAUTHORIZED, "액세스 토큰의 값이 올바르지 않습니다."),
    EXPIRED_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "액세스 토큰이 만료되었습니다. 재발급 받아주세요."),
    NONE_AUTHORIZATION_HEADER(HttpStatus.BAD_REQUEST,"Authorization 헤더가 존재하지 않습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰의 형식이 올바르지 않습니다."),
    INVALID_REFRESH_TOKEN_VALUE(HttpStatus.UNAUTHORIZED, "리프레시 토큰의 값이 올바르지 않습니다."),
    EXPIRED_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 만료되었습니다. 다시 로그인해 주세요."),
    NOT_MATCH_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "일치하지 않는 리프레시 토큰입니다."),
    VERIFY_IDENTITY(HttpStatus.UNAUTHORIZED,"false여서는 안됩니다.본인 인증이 필요합니다. "),

    /**
     * 403 Forbidden
     */
    FORBIDDEN(HttpStatus.FORBIDDEN, "리소스 접근 권한이 없습니다."),

    /**
     * 404 Not Found
     */
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 회원입니다."),
    ENTITY_NOT_FOUND(HttpStatus.NOT_FOUND, "엔티티를 찾을 수 없습니다."),
    RESOURCE_NOT_FOUND(HttpStatus.NOT_FOUND,"요청한 리소스를 찾을 수 없습니다."),


    /**
     * 405 Method Not Allowed
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "잘못된 HTTP method 요청입니다."),

    /**
     * 409 Conflict
     */
    CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 리소스입니다."),

    /**
     * 500 Internal Server Error
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    @Override
    public ErrorResponseDto getErrorReason() {
        return ErrorResponseDto.builder()
                .status(httpStatus)
                .code(httpStatus.value())
                .message(message)
                .build();
    }

    @Override
    public FieldErrorResponseDto getFieldErrorReason(Object result) {
        return FieldErrorResponseDto.builder()
                .status(httpStatus)
                .code(httpStatus.value())
                .message(message)
                .result(result)
                .build();
    }
}
