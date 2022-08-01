package infitry.rest.api.exception;

import infitry.rest.api.common.response.code.ResponseCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServiceException extends RuntimeException {
    ResponseCode responseCode;

    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, ResponseCode responseCode) {
        super(message);
        this.responseCode = responseCode;
    }
}