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

import static org.junit.jupiter.api.Assertions.*;

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
        Authority authority2 = Authority.createAuthority(Role.ROLE_USER, "사용자 기능 허용");
        AddressDto addressDto = AddressDto.builder().zipCode("111-111").address("서울시 마포구").addressDetail("성산동 111").build();
        User user = User.createUser(List.of(authority, authority2), UserDto.builder().id("user1").name("회원1").phoneNumber("01000000000").email("test@testtt.com").password(encodedPassword).addressDto(addressDto).build());
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
}