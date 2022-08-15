package infitry.rest.api.service.user;

import infitry.rest.api.common.constant.UserConstant;
import infitry.rest.api.common.response.code.ResponseCode;
import infitry.rest.api.configuration.security.token.TokenProvider;
import infitry.rest.api.dto.token.TokenDto;
import infitry.rest.api.dto.user.UserDto;
import infitry.rest.api.exception.ServiceException;
import infitry.rest.api.repository.AuthorityRepository;
import infitry.rest.api.repository.UserRepository;
import infitry.rest.api.repository.domain.user.Authority;
import infitry.rest.api.repository.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
@Slf4j
public class UserService implements UserDetailsService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException("not found " + username));
    }

    @Transactional
    public TokenDto authentication(String userId, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, password);
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        return tokenProvider.generateToken(authentication);
    }

    public String reissueAccessToken(String refreshToken) {
        if (!tokenProvider.isValidToken(refreshToken)) {
            throw new ServiceException("로그인이 만료 되었습니다.", ResponseCode.UNAUTHORIZED);
        }
        return tokenProvider.reissueAccessToken(refreshToken);
    }

    @Transactional
    public void signUp(UserDto userDto) {
        log.debug("userDto : {}", userDto);
        encryptPassword(userDto);
        userRepository.save(User.createUser(List.of(getDefaultUserAuthority()), userDto));
    }

    private void encryptPassword(UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
    }

    private Authority getDefaultUserAuthority() {
        return authorityRepository.findById(UserConstant.ROLE_USER_ID).orElseThrow(() -> new ServiceException("권한이 존재하지 않습니다."));
    }
}
