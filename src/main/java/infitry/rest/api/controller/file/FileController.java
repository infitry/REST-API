package infitry.rest.api.controller.file;

import infitry.rest.api.common.response.CommonResponse;
import infitry.rest.api.dto.file.FileUploadRequest;
import infitry.rest.api.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "FileController", description = "파일 업,다운로드 컨트롤러")
@RestController
@RequestMapping(value = "/v1/file")
public class FileController {

    @Operation(summary = "파일 업로드", description = "파일을 업로드 하는 공통 API")
    @PostMapping("/upload")
    public CommonResponse fileUpload(FileUploadRequest fileUploadRequest) {
        fileUploadRequest.getMultipartFiles().stream().map(MultipartFile::getName).forEach(System.out::println);
        return ResponseUtil.successResponse();
    }
}
