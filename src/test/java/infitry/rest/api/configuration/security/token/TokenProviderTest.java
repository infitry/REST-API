package infitry.rest.api.configuration.security.token;

import infitry.rest.api.dto.token.TokenDto;
import infitry.rest.api.dto.user.AddressDto;
import infitry.rest.api.dto.user.UserDto;
import infitry.rest.api.repository.domain.user.Authority;
import infitry.rest.api.repository.domain.user.User;
import infitry.rest.api.repository.domain.user.code.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class TokenProviderTest {

    @Autowired
    TokenProvider tokenProvider;
    @Autowired
    PasswordEncoder passwordEncoder;

    Authentication authentication;

    @BeforeEach
    void 사용자_생성() {
        // given
        final String encodedPassword = passwordEncoder.encode("password");
        Authority authority = Authority.createAuthority(Role.ROLE_ADMIN, "어드민 기능 허용");
        AddressDto addressDto = AddressDto.builder().zipCode("111-111").address("서울시 마포구").addressDetail("성산동 111").build();
        User user = User.createUser(List.of(authority), UserDto.builder().id("user1").name("회원1").password(encodedPassword).addressDto(addressDto).build());
        authentication = new UsernamePasswordAuthenticationToken(user, encodedPassword, List.of(new SimpleGrantedAuthority(authority.getRole().name())));
    }

    @Test
    public void 토큰발급_테스트() {
        //given
        UserDto userDto = ((User) authentication.getPrincipal()).toDto();

        //when
        TokenDto tokenDto = tokenProvider.generateToken(userDto);

        System.out.println("tokenDto = " + tokenDto);

        //then
        assertTrue(tokenProvider.isValidToken(tokenDto.getAccessToken()));
        assertTrue(tokenProvider.isValidToken(tokenDto.getRefreshToken()));
    }
}