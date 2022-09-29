package infitry.rest.api.dto.file;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileDto {
    Long fileId;        // 파일 ID
    String filePath;    // 파일 경로
    String fileName;    // 실제 파일명
    String saveFileName;// 저장된 파일명
    String extension;   // 확장자
    int fileSize;       // 파일 사이즈
}
