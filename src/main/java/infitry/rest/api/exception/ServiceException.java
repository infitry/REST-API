package infitry.rest.api.exception;

import infitry.rest.api.exception.code.ErrorCode;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceException extends RuntimeException {
    ErrorCode errorCode;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
}