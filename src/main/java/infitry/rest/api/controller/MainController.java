package infitry.rest.api.controller;

import infitry.rest.api.common.response.CommonRes;
import infitry.rest.api.util.ResponseUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
    @GetMapping("/")
    public CommonRes success() {
        return ResponseUtil.successResponse();
    }
}
