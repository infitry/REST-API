package infitry.rest.api.common.response.code;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ResponseCode {
    OK(200),
    SERVER_ERROR(500),
    FORBIDDEN(403);

    int statusCode;
    ResponseCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
