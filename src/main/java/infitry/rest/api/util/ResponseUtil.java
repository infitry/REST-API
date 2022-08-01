package infitry.rest.api.util;

import infitry.rest.api.common.response.CommonResponse;
import infitry.rest.api.common.response.ListResponse;
import infitry.rest.api.common.response.SingleResponse;
import infitry.rest.api.common.response.code.ResponseCode;
import infitry.rest.api.exception.ServiceException;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class ResponseUtil {
    private static final String DEFAULT_FAIL_MESSAGE = "요청에 실패 하였습니다.";
    private static final String DEFAULT_SUCCESS_MESSAGE = "요청에 성공 하였습니다.";

    public CommonResponse successResponse() {
        return CommonResponse.builder()
                .message(DEFAULT_SUCCESS_MESSAGE)
                .responseCode(ResponseCode.OK)
                .build();
    }
    public CommonResponse failResponse() {
        return CommonResponse.builder()
                .message(DEFAULT_FAIL_MESSAGE)
                .responseCode(ResponseCode.SERVER_ERROR)
                .build();
    }
    public CommonResponse failResponse(String message) {
        return CommonResponse.builder()
                .message(message)
                .responseCode(ResponseCode.SERVER_ERROR)
                .build();
    }
    public CommonResponse failResponse(String message, ResponseCode responseCode) {
        return CommonResponse.builder()
                .message(message)
                .responseCode(responseCode)
                .build();
    }
    public CommonResponse failResponse(ServiceException se) {
        return CommonResponse.builder()
                .message(se.getMessage())
                .responseCode(se.getResponseCode())
                .build();
    }
    public static <T> SingleResponse<T> getSingleResult(T data) {
        SingleResponse<T> result = new SingleResponse<>();
        result.setData(data);
        setDefaultSuccess(result);
        return result;
    }
    public static <T> ListResponse<T> getListResult(List<T> list) {
        ListResponse<T> result = new ListResponse<>();
        result.setData(list);
        setDefaultSuccess(result);
        return result;
    }
    private static void setDefaultSuccess(CommonResponse commonResponse) {
        commonResponse.setMessage(DEFAULT_SUCCESS_MESSAGE);
        commonResponse.setResponseCode(ResponseCode.OK);
    }
}
