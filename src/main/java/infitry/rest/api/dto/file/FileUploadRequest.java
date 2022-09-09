package infitry.rest.api.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Schema(name = "FileRequest", description = "파일 업로드")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileUploadRequest {
    List<MultipartFile> multipartFiles; // 업로드할 파일들
}
