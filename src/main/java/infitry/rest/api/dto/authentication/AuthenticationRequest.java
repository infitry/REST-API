package infitry.rest.api.dto.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Schema(name = "AuthenticationRequest", description = "토큰 발급을 위한 인증을 요청한다.")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {

    @NotBlank(message = "아이디가 올바르지 않습니다.")
    @Size(min = 1, max = 50, message = "아이디가 올바르지 않습니다.")
    @Schema(name = "id", description = "로그인 아이디", type = "String", example = " ")
    String id;

    @NotBlank(message = "비밀번호가 올바르지 않습니다.")
    @Size(min = 1, max = 100, message = "비밀번호가 올바르지 않습니다.")
    @Schema(name = "password", description = "비밀번호", type = "String", example = " ")
    String password;
}
