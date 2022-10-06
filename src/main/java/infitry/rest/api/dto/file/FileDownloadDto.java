package infitry.rest.api.dto.file;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FileDownloadDto {
    Resource resource;          // resource
    MediaType mediaType;        // mimeType
    String contentDisposition;  // content disposition
}
