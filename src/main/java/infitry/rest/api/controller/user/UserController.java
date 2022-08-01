package infitry.rest.api.controller.user;

import infitry.rest.api.common.response.CommonResponse;
import infitry.rest.api.dto.authentication.AuthenticationRequest;
import infitry.rest.api.dto.authentication.AuthenticationResponse;
import infitry.rest.api.dto.token.RefreshTokenRequest;
import infitry.rest.api.dto.token.RefreshTokenResponse;
import infitry.rest.api.dto.token.TokenDto;
import infitry.rest.api.service.user.UserService;
import infitry.rest.api.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/user")
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/authentication")
    public CommonResponse authentication(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        TokenDto tokenDto = userService.authentication(authenticationRequest.getId(), authenticationRequest.getPassword());
        return ResponseUtil.getSingleResult(AuthenticationResponse.builder()
                    .accessToken(tokenDto.getAccessToken())
                    .refreshToken(tokenDto.getRefreshToken())
                .build());
    }

    @PostMapping(value = "/token")
    public CommonResponse reissueAccessToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return ResponseUtil.getSingleResult(RefreshTokenResponse.builder()
                .accessToken(userService.reissueAccessToken(refreshTokenRequest.getRefreshToken()))
                .build());
    }
}
