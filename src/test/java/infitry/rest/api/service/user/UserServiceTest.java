package infitry.rest.api.service.user;

import infitry.rest.api.configuration.security.token.TokenProvider;
import infitry.rest.api.dto.token.TokenDto;
import infitry.rest.api.dto.user.AddressDto;
import infitry.rest.api.dto.user.UserDto;
import infitry.rest.api.repository.UserRepository;
import infitry.rest.api.repository.domain.user.Authority;
import infitry.rest.api.repository.domain.user.User;
import infitry.rest.api.repository.domain.user.code.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional(readOnly = true)
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    TokenProvider tokenProvider;

    @Test
    @Transactional
    public void 사용자_조회() {
        //given
        final String id = "test1";
        final String name = "회원1";
        final String encodedPassword = passwordEncoder.encode("password");
        Authority authority = Authority.createAuthority(Role.ROLE_ADMIN, "어드민 기능 허용");
        AddressDto addressDto = AddressDto.builder().zipCode("111-111").address("서울시 마포구").addressDetail("성산동 111").build();
        User user = User.createUser(List.of(authority), UserDto.builder().id(id).name(name).password(encodedPassword).addressDto(addressDto).build());
        //when
        userRepository.save(user);
        UserDetails user2 = userService.loadUserByUsername(id);
        //then
        assertEquals(id, user2.getUsername());
    }

    @Test
    @Transactional
    public void 사용자_저장() {
        // given
        final String id = "test1";
        final String name = "회원1";
        final String password = "password";
        AddressDto addressDto = AddressDto.builder().zipCode("111-111").address("서울시 마포구").addressDetail("성산동 111").build();
        // when
        userService.signUp(UserDto.builder().id(id).name(name).password(password).addressDto(addressDto).build());
        User user = userRepository.findById(id).orElseThrow(NoSuchElementException::new);
        // then
        assertEquals(id, user.getId());
        assertEquals(name, user.getName());
    }

    @Test
    @Transactional
    public void 사용자_인증() {
        final String id = "user1";
        final String name = "회원1";
        final String password = "password";
        AddressDto addressDto = AddressDto.builder().zipCode("111-111").address("서울시 마포구").addressDetail("성산동 111").build();
        // when
        userService.signUp(UserDto.builder().id(id).name(name).password(password).addressDto(addressDto).build());
        TokenDto tokenDto = userService.authentication(id, password);
        // then
        assertTrue(tokenProvider.isValidToken(tokenDto.getAccessToken()));
        assertTrue(tokenProvider.isValidToken(tokenDto.getRefreshToken()));
    }
}