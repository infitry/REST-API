package infitry.rest.api.exception.advice;

import infitry.rest.api.common.response.CommonResponse;
import infitry.rest.api.common.response.code.ResponseCode;
import infitry.rest.api.exception.ServiceException;
import infitry.rest.api.util.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages =  "infitry.rest.api.controller")
public class RestControllerException {

    @ExceptionHandler(ServiceException.class)
    protected CommonResponse handleException(ServiceException se) {
        return ResponseUtil.failResponse(se);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected CommonResponse handleException(MethodArgumentNotValidException manve) {
        return ResponseUtil.failResponse(manve.getBindingResult().getAllErrors().get(0).getDefaultMessage(), ResponseCode.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(BindException.class)
    protected CommonResponse handleException(BindException be) {
        return ResponseUtil.failResponse(be.getBindingResult().getAllErrors().get(0).getDefaultMessage(), ResponseCode.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected CommonResponse handleException(BadCredentialsException bce) {
        return ResponseUtil.failResponse("인증에 실패 하였습니다.", ResponseCode.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    protected CommonResponse handleException(Exception e) {
        log.error("Exception.class Handle Exception - ", e);
        return ResponseUtil.failResponse("일시적인 오류로 인해 서버에서 응답할 수 없습니다.");
    }
}
