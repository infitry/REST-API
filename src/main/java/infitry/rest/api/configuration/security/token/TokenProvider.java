package infitry.rest.api.configuration.security.token;

import infitry.rest.api.common.constant.UserConstant;
import infitry.rest.api.common.response.code.ResponseCode;
import infitry.rest.api.configuration.aop.timer.Timer;
import infitry.rest.api.dto.token.TokenDto;
import infitry.rest.api.dto.user.AuthorityDto;
import infitry.rest.api.dto.user.UserDto;
import infitry.rest.api.exception.ServiceException;
import infitry.rest.api.repository.UserRepository;
import infitry.rest.api.repository.domain.user.User;
import infitry.rest.api.repository.domain.user.code.Role;
import infitry.rest.api.service.redis.RedisService;
import infitry.rest.api.util.DateUtil;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class TokenProvider {
    public static final String SIGNING_KEY = "infitrysignkey";
    public static final String REDIS_GROUP_PREFIX = "refresh-token:";
    private static final Long HOURS_ACCESS_TOKEN_EXPIRE = 1L;
    private static final Long HOURS_REFRESH_TOKEN_EXPIRE = 5L;
    private static final Long HOURS_REDIS_CACHE_EXPIRE = HOURS_REFRESH_TOKEN_EXPIRE;
    private final RedisService redisService;
    private final UserRepository userRepository;

    /** 엑세스, 리프레쉬 토큰을 발급한다. */
    @Transactional
    public TokenDto generateToken(UserDto userDto) {
        String refreshToken = generateRefreshToken();
        redisService.setValue(REDIS_GROUP_PREFIX + refreshToken, userDto.getId(), HOURS_REDIS_CACHE_EXPIRE, TimeUnit.HOURS);

        return TokenDto.builder()
                .accessToken(generateAccessToken(userDto))
                .refreshToken(refreshToken)
                .build();
    }
    /** 엑세스 토큰을 발급한다. */
    public String generateAccessToken(UserDto userDto) {
        return Jwts.builder()
                .setSubject(userDto.getId())
                .claim(UserConstant.AUTHORITIES_KEY, convertStringAuthorities(userDto))
                .claim(UserConstant.NAME_KEY, userDto.getName())
                .claim(UserConstant.PHONE_NUMBER_KEY, userDto.getPhoneNumber())
                .claim(UserConstant.EMAIL_KEY, userDto.getEmail())
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, SIGNING_KEY)
                .setExpiration(DateUtil.addHours(HOURS_ACCESS_TOKEN_EXPIRE))
                .compact();
    }

    /** 사용자의 권한들을 ","를 구분자로 String 으로 변환한다. */
    private String convertStringAuthorities(UserDto userDto) {
        return userDto.getAuthorities().stream()
                .map(authorityDto -> authorityDto.getRole().name())
                .collect(Collectors.joining(UserConstant.AUTHORITIES_SEPARATOR));
    }
    /** 리프레쉬 토큰을 발급한다. */
    public String generateRefreshToken() {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, SIGNING_KEY)
                .setExpiration(DateUtil.addHours(HOURS_REFRESH_TOKEN_EXPIRE))
                .compact();
    }
    /** 유효한 토큰인지 확인한다. */
    public boolean isValidToken(String token) {
        try {
            Jwts.parser().setSigningKey(SIGNING_KEY).parseClaimsJws(token);
            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT Signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT Token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT Token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT Token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT Claims string is empty.");
        }
        return false;
    }
    /**
     * 매 요청 Header의 Token을 파싱하여 인증정보 조회
     * principal : UserDto
     **/
    @Timer
    public Authentication getAuthenticationByToken(String token) {
        return getAuthentication(makeUserByClaims(parseClaims(token)));
    }

    /** 엑세스 토큰을 재발급 한다. */
    public String reissueAccessToken(String refreshToken) {
        return generateAccessToken(findUserById(getRedisValue(refreshToken)).toDto());
    }

    /** 사용자 ID로 유저를 찾는다. */
    private User findUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ServiceException("사용자를 찾을 수 없습니다.", ResponseCode.UNAUTHORIZED));
    }

    private String getRedisValue(String key) {
        return redisService.getValue(REDIS_GROUP_PREFIX + key, String.class);
    }

    private Authentication getAuthentication(UserDto userDto) {
        return new UsernamePasswordAuthenticationToken(userDto, null, convertAuthorityDtosToSecurityAuthorities(userDto.getAuthorities()));
    }

    private static List<GrantedAuthority> convertAuthorityDtosToSecurityAuthorities(List<AuthorityDto> authorityDtos) {
        return authorityDtos.stream().map(authorityDto -> new SimpleGrantedAuthority(authorityDto.getRole().name())).collect(Collectors.toList());
    }

    private UserDto makeUserByClaims(Claims claims) {
        return UserDto.builder()
                .id(claims.getSubject())
                .name(claims.get(UserConstant.NAME_KEY, String.class))
                .email(claims.get(UserConstant.EMAIL_KEY, String.class))
                .phoneNumber(claims.get(UserConstant.PHONE_NUMBER_KEY, String.class))
                .authorities(convertStringAuthoritiesToAuthorityDtos(claims))
            .build();
    }

    private List<AuthorityDto> convertStringAuthoritiesToAuthorityDtos(Claims claims) {
       return Arrays.stream(claims.get(UserConstant.AUTHORITIES_KEY, String.class).split(UserConstant.AUTHORITIES_SEPARATOR))
               .map(authority -> AuthorityDto.builder().role(Role.valueOf(authority)).build()).collect(Collectors.toList());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
