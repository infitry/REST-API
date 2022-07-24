package infitry.rest.api.util;

import infitry.rest.api.common.response.CommonRes;
import infitry.rest.api.common.response.ResponseCode;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ResponseUtil {
    private static final String DEFAULT_FAIL_MESSAGE = "요청에 실패 하였습니다.";
    private static final String DEFAULT_SUCCESS_MESSAGE = "요청에 성공 하였습니다.";

    public CommonRes successResponse() {
        return CommonRes.builder()
                .message(DEFAULT_SUCCESS_MESSAGE)
                .responseCode(ResponseCode.OK)
                .build();
    }
    public CommonRes failResponse() {
        return CommonRes.builder()
                .message(DEFAULT_FAIL_MESSAGE)
                .responseCode(ResponseCode.SERVER_ERROR)
                .build();
    }
}
