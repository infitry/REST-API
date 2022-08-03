package infitry.rest.api.service.user;

import infitry.rest.api.common.response.code.ResponseCode;
import infitry.rest.api.configuration.security.token.TokenProvider;
import infitry.rest.api.dto.token.TokenDto;
import infitry.rest.api.exception.ServiceException;
import infitry.rest.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService implements UserDetailsService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

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
}
