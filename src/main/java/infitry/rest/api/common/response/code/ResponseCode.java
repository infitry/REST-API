package infitry.rest.api.common.response.code;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ResponseCode {
    OK(200),
    SERVER_ERROR(500),
    UNAUTHORIZED(401),
    UNPROCESSABLE_ENTITY(422);

    @JsonValue
    public final int statusCode;

    ResponseCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
