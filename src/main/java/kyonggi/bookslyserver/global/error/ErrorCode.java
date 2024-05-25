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
    DISCOUNT_SETTING_BAD_REQUEST(HttpStatus.BAD_REQUEST,"할인율 설정이 되어있지 않습니다"),
    MENU_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "카테고리에 메뉴가 존재합니다. 해당 카테고리를 삭제할 수 없습니다."),
    RESERVATION_CLOSED_BAD_REQUEST(HttpStatus.BAD_REQUEST,"예약 인원이 마감된 시간대 입니다."),
    DUPLICATED_REPEAT_SETTING_BAD_REQUEST(HttpStatus.BAD_REQUEST, "요일 반복과 기간 반복의 중복 설정이 불가능합니다."),
    INCOMPLETE_REPEAT_SETTING_BAD_REQUEST(HttpStatus.BAD_REQUEST, "반복 설정을 완료해주세요."),
    MISSING_REQUIRED_QUERY_PARAM(HttpStatus.BAD_REQUEST,"필수 쿼리 파라미터가 누락되었습니다."),
    REFUSAL_REASON_MISSING(HttpStatus.BAD_REQUEST,"거절 사유는 필수로 등록해야 합니다."),
    SCHEDULE_NOT_INCLUDED(HttpStatus.BAD_REQUEST, "요청 시간 범위는 하나 이상의 예약 일정을 포함해야 합니다."),
    START_TIME_IS_AFTER_END_TIME(HttpStatus.BAD_REQUEST,"이벤트 시작 시간은 이벤트 마감 시간 이전이어야 합니다."),
    START_DATE_IS_AFTER_END_DATE(HttpStatus.BAD_REQUEST,"이벤트 시작일은 이벤트 마감일 이전이어야 합니다."),
    MENU_IS_NOT_EMPLOYEEMENU(HttpStatus.BAD_REQUEST,"직원 담당 메뉴가 아닌 메뉴가 존재합니다."),
    PAGE_NUMBER_OVER(HttpStatus.BAD_REQUEST, "페이지는 9 이하여야 합니다."),
    EMPLOYEE_NOT_BELONG_SHOP(HttpStatus.BAD_REQUEST,"해당 가게의 직원이 아닙니다"),
    NOT_OWNER_OF_SHOP(HttpStatus.BAD_REQUEST,"해당 가게의 주인이 아닙니다."),
    START_DATE_IS_BEFORE_NOW_DATE(HttpStatus.BAD_REQUEST, "이벤트 시작 날짜는 현재 날짜보다 이전일 수 없습니다."),
    STRAT_TIME_IS_BEFORE_NOW(HttpStatus.BAD_REQUEST,"이벤트 시작 시간은 현재 시각보다 이를 수 없습니다." ),
    EMPLOYEE_NOT_OWN_RESERVATION_SCHEDULE(HttpStatus.BAD_REQUEST,"직원 소유의 예약 일정이 아닙니다."),
    EMPLOYEE_NOT_SETTING_CLOSING_EVENT(HttpStatus.BAD_REQUEST,"마감 이벤트 설정을 먼저 완료해주세요"),
    CURRENT_PASSWORD_IS_NULL(HttpStatus.BAD_REQUEST,"현재 비밀번호를 입력해주세요."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),


    /**
     * 401 Unauthorized
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인이 필요합니다."),
    LOGIN_FAILURE(HttpStatus.UNAUTHORIZED,"아이디 또는 비밀번호가 틀렸습니다."),
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
    SETTING_NOT_FOUND(HttpStatus.NOT_FOUND,"예약 설정을 먼저 완료해주세요"),
    EMPLOYEE_NOT_FOUND(HttpStatus.NOT_FOUND,"해당하는 직원을 찾을 수 없습니다"),
    EMPLOYEE_MENU_NOT_FOUND(HttpStatus.NOT_FOUND,"직원 메뉴를 찾을 수 없습니다"),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND,"해당하는 시간대가 없습니다"),
    RESERVE_EMPLOYEES_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 가게에 직원이 존재하지 않습니다."),
    SHOP_NOT_FOUND(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다"),
    MENU_NOT_FOUND(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다"),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND,"해당하는 예약이 존재하지 않습니다"),
    RESERVATION_SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 예약 일정입니다."),
    EMPLOYEES_NOT_FOUND(HttpStatus.NOT_FOUND, "직원이 없습니다. 직원을 등록 해 주세요."),
    MENUCATEGORIES_NOT_FOUND(HttpStatus.NOT_FOUND, "메뉴 카테고리가 존재하지 않습니다. 하나 이상의 메뉴 카테고리를 등록 해 주세요."),
    MENUCATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "메뉴 카테고리가 존재하지 않습니다."),
    CLOSING_EVENT_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 마감 이벤트입니다."),
    TIME_EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 타임 이벤트입니다."),
    CATEGORY_NOT_FOUNT(HttpStatus.NOT_FOUND, "존재하지 않는 카테고리입니다."),

    /**
     * 405 Method Not Allowed
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "잘못된 HTTP method 요청입니다."),

    /**
     * 409 Conflict
     */
    CONFLICT(HttpStatus.CONFLICT, "이미 존재하는 리소스입니다."),
    MENUCATEGORY_ALREADY_EXIST(HttpStatus.CONFLICT, "해당 카테고리가 이미 존재합니다."),
    SHOP_NAME_ALREADY_EXIST(HttpStatus.CONFLICT, "동일한 이름의 가게가 이미 존재합니다."),
    EXIST_EVENTS_CONFLICT(HttpStatus.CONFLICT, "새로운 이벤트의 시간이 기존 이벤트와 겹칩니다."),
    EVENT_SETTING_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 등록된 이벤트 설정이 존재합니다."),

    /**
     * 500 Internal Server Error
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."), ;

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
