package infitry.rest.api.dto.authentication;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationRequest {

    @NotBlank(message = "아이디가 올바르지 않습니다.")
    @Size(min = 1, max = 50, message = "아이디가 올바르지 않습니다.")
    String id;

    @NotBlank(message = "비밀번호가 올바르지 않습니다.")
    @Size(min = 1, max = 100, message = "비밀번호가 올바르지 않습니다.")
    String password;
}
