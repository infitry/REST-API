package infitry.rest.api.common.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SingleResponse<T> extends CommonResponse {
    T data;
}
