package infitry.rest.api.controller.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import infitry.rest.api.common.response.CommonResponse;
import infitry.rest.api.dto.user.AddressDto;
import infitry.rest.api.dto.user.UserDto;
import infitry.rest.api.dto.user.SignUpRequest;
import infitry.rest.api.dto.authentication.AuthenticationRequest;
import infitry.rest.api.dto.authentication.AuthenticationResponse;
import infitry.rest.api.dto.token.RefreshTokenRequest;
import infitry.rest.api.dto.token.RefreshTokenResponse;
import infitry.rest.api.dto.token.TokenDto;
import infitry.rest.api.service.user.UserService;
import infitry.rest.api.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "Users", description = "사용자 관련 API")
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    @PostMapping("/new")
    @Operation(summary = "회원 가입", description = "회원가입 처리")
    public CommonResponse signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        UserDto userDto = objectMapper.convertValue(signUpRequest, UserDto.class);
        userDto.setAddressDto(objectMapper.convertValue(signUpRequest, AddressDto.class));
        userService.signUp(userDto);
        return ResponseUtil.successResponse();
    }

    @PostMapping(value = "/authentication")
    @Operation(summary = "회원 인증", description = "로그인 및 엑세스 토큰발급")
    public CommonResponse authentication(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        TokenDto tokenDto = userService.authentication(authenticationRequest.getId(), authenticationRequest.getPassword());
        return ResponseUtil.getSingleResult(AuthenticationResponse.builder()
                    .accessToken(tokenDto.getAccessToken())
                    .refreshToken(tokenDto.getRefreshToken())
                .build());
    }

    @PostMapping(value = "/token")
    @Operation(summary = "엑세스토큰 재발급", description = "리프레쉬 토큰으로 엑세스 토큰을 재발급한다.")
    public CommonResponse reissueAccessToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseUtil.getSingleResult(RefreshTokenResponse.builder()
                .accessToken(userService.reissueAccessToken(refreshTokenRequest.getRefreshToken()))
                .build());
    }
}
