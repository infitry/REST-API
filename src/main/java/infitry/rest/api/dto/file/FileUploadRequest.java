package infitry.rest.api.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Schema(name = "FileRequest", description = "파일 업로드 요청")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileUploadRequest {

    @NotBlank(message = "업로드할 파일이 존재하지 않습니다.")
    @Schema(name = "multipartFiles", description = "업로드 파일들", type = "file", example = " ")
    List<MultipartFile> multipartFiles; // 업로드할 파일들
}
