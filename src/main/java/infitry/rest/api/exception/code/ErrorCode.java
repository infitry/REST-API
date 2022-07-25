package infitry.rest.api.exception.code;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    SERVER_ERROR(500);

    int statusCode;
    String message;
    ErrorCode(int statusCode) {
        this.statusCode = statusCode;
    }
    ErrorCode(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }
}
