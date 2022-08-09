package infitry.rest.api.service.user;

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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional(readOnly = true)
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @Transactional
    public void 사용자_조회() {
        //given
        final String username = "test1";
        Authority authority = Authority.createAuthority(Role.ROLE_ADMIN, "어드민 기능 허용");
        User user = User.createUser(List.of(authority), username, "name1", passwordEncoder.encode("password1"));
        //when
        userRepository.save(user);
        UserDetails user2 = userService.loadUserByUsername(username);
        //then
        assertEquals(username, user2.getUsername());
    }
}