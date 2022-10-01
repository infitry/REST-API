package infitry.rest.api.dto.file;

import lombok.*;
import lombok.experimental.FieldDefaults;

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
    String savedFileName;// 저장된 파일명
    String extension;   // 확장자
    int fileSize;       // 파일 사이즈
}
