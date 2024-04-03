package kyonggi.bookslyserver.global.error.exception;

import kyonggi.bookslyserver.global.error.ErrorCode;

public class CustomNurigoException extends BusinessException {
    public CustomNurigoException() {
        super(ErrorCode.NURIGO_BAD_REQUEST);
    }

    public CustomNurigoException(ErrorCode errorCode) {
        super(errorCode);
    }
}
