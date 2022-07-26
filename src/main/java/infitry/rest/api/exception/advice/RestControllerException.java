package infitry.rest.api.exception.advice;

import infitry.rest.api.common.response.CommonResponse;
import infitry.rest.api.exception.ServiceException;
import infitry.rest.api.util.ResponseUtil;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages =  "infitry.rest.api.controller")
public class RestControllerException {

    @ExceptionHandler(ServiceException.class)
    protected CommonResponse handleException(ServiceException se) {
        return ResponseUtil.failResponse(se);
    }

    @ExceptionHandler(Exception.class)
    protected CommonResponse handleException(Exception e) {
        return ResponseUtil.failResponse(e.getMessage());
    }
}
