package infitry.rest.api.common.response.code;

public enum ResponseCode {
    OK(200),
    SERVER_ERROR(500),
    FORBIDDEN(403);

    private int statusCode;
    ResponseCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
