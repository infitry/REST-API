package infitry.rest.api.controller.file;

import infitry.rest.api.common.response.CommonResponse;
import infitry.rest.api.dto.file.FileUploadRequest;
import infitry.rest.api.service.file.FileService;
import infitry.rest.api.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Files", description = "파일 업,다운로드 컨트롤러")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/files")
public class FileController {

    private final FileService fileService;

    @Operation(summary = "파일 업로드", description = "파일을 업로드 하는 공통 API")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public CommonResponse fileUpload(@Valid FileUploadRequest fileUploadRequest) {
        return ResponseUtil.getListResult(fileService.save(fileUploadRequest.getMultipartFiles()));
    }

    @Operation(summary = "파일 다운로드", description = "파일을 다운로드 하는 공통 API")
    @GetMapping("/{fileId}")
    public CommonResponse fileDownload(@PathVariable Long fileId) {
        System.out.println("fileId = " + fileId);
        return ResponseUtil.successResponse();
    }
}
