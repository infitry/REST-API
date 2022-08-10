package infitry.rest.api.common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ListResponse<T> extends CommonResponse {
    @Schema(name = "list", description = "목록 응답 값", type = "ArrayList")
    List<T> data;
}
