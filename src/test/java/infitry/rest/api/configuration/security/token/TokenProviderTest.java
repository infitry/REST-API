package infitry.rest.api.configuration.security.token;

import infitry.rest.api.dto.token.TokenDto;
import infitry.rest.api.dto.user.UserDto;
import infitry.rest.api.repository.UserRepository;
import infitry.rest.api.repository.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional(readOnly = true)
class TokenProviderTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    TokenProvider tokenProvider;
    Authentication authentication;

    @BeforeEach
    void 사용자_생성() {
        // given
        User user = userRepository.findById(1L).orElseThrow(NoSuchElementException::new);
        authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
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

    @Test
    public void 토큰으로_사용자_인증조회() {
        // given
        UserDto userDto = ((User) authentication.getPrincipal()).toDto();
        TokenDto tokenDto = tokenProvider.generateToken(userDto);
        // when
        Authentication auth = tokenProvider.getAuthenticationByToken(tokenDto.getAccessToken());
        UserDto tokenUserDto = (UserDto) auth.getPrincipal();
        // then
        assertEquals(userDto.getId(), tokenUserDto.getId(), "기존 아이디와 토큰 파싱 후 아이디가 같아야 한다.");
        assertEquals(userDto.getName(), tokenUserDto.getName(), "기존 이름과 토큰 파싱 후 이름이 같아야 한다.");
        assertEquals(userDto.getPhoneNumber(), tokenUserDto.getPhoneNumber(), "기존 휴대전호번호와 토큰 파싱 후 휴대전호번호이 같아야 한다.");
        assertEquals(userDto.getEmail(), tokenUserDto.getEmail(), "기존 이메일과 토큰 파싱 후 이메일이 같아야 한다.");
    }

    @Test
    @Transactional
    public void 엑세스토큰_재발급() {
        //given
        UserDto userDto = ((User) authentication.getPrincipal()).toDto();
        //when
        TokenDto tokenDto = tokenProvider.generateToken(userDto);
        String accessToken = tokenProvider.reissueAccessToken(tokenDto.getRefreshToken());
        // then
        assertTrue(tokenProvider.isValidToken(accessToken), "재발급 된 토큰은 유효한 토큰 이어야 한다.");
    }
}