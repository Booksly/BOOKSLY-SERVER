package kyonggi.bookslyserver.global.error.exception;


import kyonggi.bookslyserver.global.error.ErrorCode;

public class InternalServerException extends BusinessException {
    public InternalServerException(ErrorCode errorCode) {
        super(errorCode);
    }
}