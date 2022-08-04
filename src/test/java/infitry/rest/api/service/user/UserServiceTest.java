package infitry.rest.api.service.user;

import infitry.rest.api.repository.UserRepository;
import infitry.rest.api.repository.domain.user.User;
import infitry.rest.api.repository.domain.user.code.Authority;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

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
    public void 사용자_조회() {
        final String username = "test1";

        //given
        User user = User.createUser(Authority.ROLE_USER, username, "name1", passwordEncoder.encode("password1"));
        //when
        userRepository.save(user);
        UserDetails user2 = userService.loadUserByUsername(username);
        //then
        assertEquals(username, user2.getUsername());
    }
}