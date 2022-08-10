package infitry.rest.api.dto.token;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;

@Schema(name = "RefreshTokenRequest", description = "엑세스 토큰 재발급 요청")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RefreshTokenRequest {
    @NotBlank(message = "토큰 값이 올바르지 않습니다.")
    @Schema(name = "refreshToken", description = "리프레쉬 토큰", type = "String", example = " ")
    String refreshToken;
}
