package infitry.rest.api.configuration.security.token;

import infitry.rest.api.common.response.code.ResponseCode;
import infitry.rest.api.dto.token.TokenDto;
import infitry.rest.api.exception.ServiceException;
import infitry.rest.api.repository.UserRepository;
import infitry.rest.api.repository.domain.user.User;
import infitry.rest.api.service.redis.RedisService;
import infitry.rest.api.util.DateUtil;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class TokenProvider {
    public static final String AUTHORITIES_KEY = "auth";
    public static final String SIGNING_KEY = "infitrysignkey";
    public static final String REDIS_GROUP_PREFIX = "refresh-token:";
    private static final Long HOURS_ACCESS_TOKEN_EXPIRE = 1L;
    private static final Long HOURS_REFRESH_TOKEN_EXPIRE = 5L;
    private static final Long HOURS_REDIS_CACHE_EXPIRE = HOURS_REFRESH_TOKEN_EXPIRE;
    private final RedisService redisService;
    private final UserRepository userRepository;

    @Transactional
    public TokenDto generateToken(Authentication authentication) {
        String refreshToken = generateRefreshToken();
        redisService.setValue(REDIS_GROUP_PREFIX + refreshToken, authentication.getName(), HOURS_REDIS_CACHE_EXPIRE, TimeUnit.HOURS);

        return TokenDto.builder()
                .accessToken(generateAccessToken(authentication))
                .refreshToken(refreshToken)
                .build();
    }
    public String generateAccessToken(Authentication authentication) {
        User user = getUser(authentication);

        return Jwts.builder()
                .setSubject(user.getId())
                .claim(AUTHORITIES_KEY, getAuthorities(user))
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, SIGNING_KEY)
                .setExpiration(DateUtil.addHours(HOURS_ACCESS_TOKEN_EXPIRE))
                .compact();
    }

    private User getUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    private String getAuthorities(User user) {
        return user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    public String generateRefreshToken() {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS512, SIGNING_KEY)
                .setExpiration(DateUtil.addHours(HOURS_REFRESH_TOKEN_EXPIRE))
                .compact();
    }

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
    public Authentication getAuthenticationByToken(String token) {
        return getAuthentication(findUserById(getUserIdInClaims(parseClaims(token))));
    }

    public String reissueAccessToken(String refreshToken) {
        return generateAccessToken(getAuthentication(findUserById(getRedisValue(refreshToken))));
    }

    private User findUserById(String userId) {
        return userRepository.findById(userId).orElseThrow(() -> new ServiceException("사용자를 찾을 수 없습니다.", ResponseCode.UNAUTHORIZED));
    }

    private String getRedisValue(String key) {
        return redisService.getValue(REDIS_GROUP_PREFIX + key, String.class);
    }

    private Authentication getAuthentication(User user) {
        return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .parseClaimsJws(token)
                .getBody();
    }

    private String getUserIdInClaims(Claims claims) {
        return claims.getSubject();
    }
}
