package infitry.rest.api.configuration.security.token;

import infitry.rest.api.dto.token.TokenDto;
import infitry.rest.api.repository.domain.user.User;
import infitry.rest.api.repository.domain.user.code.Authority;
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
        String encodedPassword = passwordEncoder.encode("password");
        Authority authority = Authority.ROLE_USER;
        User user = User.createUser(authority, "user1", "회원1", encodedPassword);
        authentication = new UsernamePasswordAuthenticationToken(user, encodedPassword, List.of(new SimpleGrantedAuthority(authority.name())));
    }

    @Test
    public void 토큰발급_테스트() {
        //when
        TokenDto tokenDto = tokenProvider.generateToken(authentication);

        //then
        assertTrue(tokenProvider.isValidToken(tokenDto.getAccessToken()));
        assertTrue(tokenProvider.isValidToken(tokenDto.getRefreshToken()));
    }
}