package infitry.rest.api.controller;

import infitry.rest.api.common.response.CommonResponse;
import infitry.rest.api.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "main", description = "기본 컨트롤러")
@RestController
public class MainController {
    @GetMapping("/")
    @Operation(summary = "메인 컨트롤러", description = "메인 컨트롤러")
    public CommonResponse success() {
        return ResponseUtil.successResponse();
    }
}
