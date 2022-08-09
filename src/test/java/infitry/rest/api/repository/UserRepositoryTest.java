package infitry.rest.api.repository;

import infitry.rest.api.configuration.TestConfig;
import infitry.rest.api.configuration.TestJPAConfig;
import infitry.rest.api.repository.domain.user.Authority;
import infitry.rest.api.repository.domain.user.User;
import infitry.rest.api.repository.domain.user.code.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@Import({TestConfig.class, TestJPAConfig.class})
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    public void 사용자_생성() {
        // given
        String username = "test1";
        Authority authority = Authority.createAuthority(Role.ROLE_ADMIN, "어드민 기능 허용");
        User saveUser = User.createUser(List.of(authority), username, "name1", passwordEncoder.encode("password1"));
        userRepository.save(saveUser);
        // when
        User findUser = userRepository.findById(username).orElseThrow(NoSuchElementException::new);
        // then
        assertEquals(saveUser.getUsername(), findUser.getUsername());
    }
}