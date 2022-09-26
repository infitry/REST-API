package infitry.rest.api.controller.file;

import infitry.rest.api.common.response.CommonResponse;
import infitry.rest.api.dto.file.FileUploadRequest;
import infitry.rest.api.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@Tag(name = "Files", description = "파일 업,다운로드 컨트롤러")
@RestController
@RequestMapping(value = "/files")
public class FileController {

    @Operation(summary = "파일 업로드", description = "파일을 업로드 하는 공통 API")
    @PostMapping("/upload")
    public CommonResponse fileUpload(@Valid @RequestBody FileUploadRequest fileUploadRequest) {
        fileUploadRequest.getMultipartFiles().stream().map(MultipartFile::getName).forEach(System.out::println);
        return ResponseUtil.successResponse();
    }

    @Operation(summary = "파일 다운로드", description = "파일을 다운로드 하는 공통 API")
    @GetMapping("/{fileId}")
    public CommonResponse fileDownload(@PathVariable Long fileId) {
        System.out.println("fileId = " + fileId);
        return ResponseUtil.successResponse();
    }
}
