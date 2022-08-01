package infitry.rest.api.dto.token;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshTokenRequest {
    @NotBlank(message = "토큰 값이 올바르지 않습니다.")
    String refreshToken;
}
