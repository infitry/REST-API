package infitry.rest.api.controller.user;

import infitry.rest.api.common.response.CommonResponse;
import infitry.rest.api.dto.authentication.AuthenticationRequest;
import infitry.rest.api.dto.authentication.AuthenticationResponse;
import infitry.rest.api.dto.token.RefreshTokenRequest;
import infitry.rest.api.dto.token.RefreshTokenResponse;
import infitry.rest.api.dto.token.TokenDto;
import infitry.rest.api.service.user.UserService;
import infitry.rest.api.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.lang.model.element.Name;
import javax.validation.Valid;

@Tag(name = "user", description = "User API")
@RequestMapping(value = "/v1/user")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

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
