package infitry.rest.api.common.response;

import infitry.rest.api.common.response.code.ResponseCode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Schema(name = "CommonResponse", description = "공통 응답")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommonResponse {
    @Schema(name = "message", description = "응답 메시지", type = "String", example = " ")
    String message;
    @Schema(name = "responseCode", description = "에러 코드", type = "Integer", example = "0")
    ResponseCode responseCode;
}
