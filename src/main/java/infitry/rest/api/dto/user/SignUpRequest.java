package infitry.rest.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Schema(name = "SignUpRequest", description = "회원가입 요청")
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SignUpRequest {
    @NotBlank(message = "ID를 입력해 주세요.")
    @Schema(name = "id", description = "아이디", type = "String", example = " ")
    String id;

    @NotBlank(message = "비밀번호를 입력해 주세요.")
    @Schema(name = "password", description = "아이디", type = "String", example = " ")
    String password;

    @NotBlank(message = "이메일을 입력해 주세요.")
    @Email(message = "이메일이 올바르지 않습니다.")
    @Schema(name = "email", description = "아이디", type = "String", example = " ")
    String email;

    @NotBlank(message = "이름을 입력해 주세요.")
    @Schema(name = "name", description = "이름", type = "String", example = " ")
    String name;

    @NotBlank(message = "휴대전화번호를 입력해 주세요.")
    @Schema(name = "phoneNumber", description = "휴대전화번호", type = "String", example = " ")
    String phoneNumber;

    @NotBlank(message = "우편번호를 입력해 주세요.")
    @Schema(name = "zipCode", description = "우편번호", type = "String", example = " ")
    String zipCode;

    @NotBlank(message = "주소를 입력해 주세요.")
    @Schema(name = "address", description = "주소", type = "String", example = " ")
    String address;

    @NotBlank(message = "주소상세를 입력해 주세요.")
    @Schema(name = "addressDetail", description = "주소상세", type = "String", example = " ")
    String addressDetail;
}
