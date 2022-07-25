package infitry.rest.api.common.response;

import infitry.rest.api.common.response.code.ResponseCode;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CommonResponse {
    String message;
    ResponseCode responseCode;
}
