package infitry.rest.api.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SingleResponse<T> extends CommonResponse {
    @Schema(name = "object", description = "객체 응답 값", type = "Object")
    T data;
}
