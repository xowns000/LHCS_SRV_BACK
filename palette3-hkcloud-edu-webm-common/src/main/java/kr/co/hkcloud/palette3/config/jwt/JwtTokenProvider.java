package kr.co.hkcloud.palette3.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import kr.co.hkcloud.palette3.core.security.authentication.domain.PaletteUserDetailsVO;
import kr.co.hkcloud.palette3.exception.model.ErrorCode;
import kr.co.hkcloud.palette3.config.jwt.domain.TokenDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * packageName    : kr.co.hkcloud.palette3.config.jwt
 * fileName       : JwtTokenProvider.java
 * author         : USER
 * date           : 0000-10-24
 * description    : JWT 토큰 생성, 토큰 복호화 및 정보 추출, 토큰 유효성 검증의 기능이 구현된 클래스
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023-10-24       USER       최초 생성
 */
@Slf4j
@Component
public class JwtTokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 60 * 60 * 1000L;             // 60분 60 * 60 * 1000L
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 9 * 60 * 60 * 1000L;            // 9시간 9 * 60 * 60 * 1000L

    private final Key key;

    /**
     *
     */
    public JwtTokenProvider(@Value("${jwt.token.secret}") String secretKey) {

        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 유저 정보 -> AccessToken, RefreshToken 생성
     */
    public TokenDTO.TokenInfo generateToken(PaletteUserDetailsVO authentication) {

        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder().setSubject(authentication.getName()).claim(AUTHORITIES_KEY, authorities)
            .claim("info", "custcoId::" + authentication.getCustcoId() + ",userId::" + authentication.getUserId() + ",lgnId::" + authentication.getLgnId()+",tenantId::" + authentication.getTenantId())
            .setExpiration(accessTokenExpiresIn).signWith(key, SignatureAlgorithm.HS256).compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder().setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME)).signWith(key, SignatureAlgorithm.HS256).compact();

        return TokenDTO.TokenInfo.builder().grantType(BEARER_TYPE).accessToken(accessToken).accessTokenExpirationTime(ACCESS_TOKEN_EXPIRE_TIME)
            .refreshToken(refreshToken).refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME).build();
    }

    /**
     * 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
     */
    public TokenDTO.TokenInfo generateToken(Authentication authentication, String info) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder().setSubject(authentication.getName()).claim("info", info).claim(AUTHORITIES_KEY, authorities)
            .setExpiration(accessTokenExpiresIn).signWith(key, SignatureAlgorithm.HS256).compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder().setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME)).signWith(key, SignatureAlgorithm.HS256).compact();

        return TokenDTO.TokenInfo.builder().grantType(BEARER_TYPE).accessToken(accessToken).accessTokenExpirationTime(ACCESS_TOKEN_EXPIRE_TIME)
            .refreshToken(refreshToken).refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME).build();
    }
    
    
    /**
     * 토큰 유효 기간을 변경하여 생성할때 사용.
     * @param authentication
     * @return
     */
    public TokenDTO.TokenInfo generateTokenWithExpireTime(PaletteUserDetailsVO authentication, long accessTokenExpireTime) {

        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + accessTokenExpireTime);
        String accessToken = Jwts.builder().setSubject(authentication.getName()).claim(AUTHORITIES_KEY, authorities)
            .claim("info", "custcoId::" + authentication.getCustcoId() + ",userId::" + authentication.getUserId() + ",lgnId::" + authentication.getLgnId()+",tenantId::" + authentication.getTenantId())
            .setExpiration(accessTokenExpiresIn).signWith(key, SignatureAlgorithm.HS256).compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder().setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME)).signWith(key, SignatureAlgorithm.HS256).compact();

        return TokenDTO.TokenInfo.builder().grantType(BEARER_TYPE).accessToken(accessToken).accessTokenExpirationTime(accessTokenExpireTime)
            .refreshToken(refreshToken).refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME).build();
    }

    /**
     * JWT 토큰을 복호화 -> 토큰 정보 추출
     */
    public Authentication getAuthentication(String accessToken) {

        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if(claims.get(AUTHORITIES_KEY) == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }

        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(",")).map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    public String getTokenValue(String accessToken, String key) {
        String result = "";
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);
        String[] aud = claims.get("info").toString().split(",");
        for(int i = 0; i < aud.length; i++) {
            if(aud[i].startsWith(key + "::")) {
                result = aud[i].replaceAll(key + "::", "");
                break;
            }
        }
        return result;
    }

    public String getInfo(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);
        return claims.get("info").toString();
    }

    /**
     * 토큰 검증
     */
    public ErrorCode validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return ErrorCode.OK;
        } catch(io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            return ErrorCode.ACCESS_TOKEN_INVALID;  //"Invalid JWT Token"
        } catch(ExpiredJwtException e) {
            return ErrorCode.ACCESS_TOKEN_EXPIRED;  //"Expired JWT Token"
        } catch(UnsupportedJwtException e) {
            return ErrorCode.ACCESS_TOKEN_UNSUPPORTED; // "Unsupported JWT Token"
        } catch(IllegalArgumentException e) {
            return ErrorCode.ACCESS_TOKEN_UNKNOWN;  //"JWT claims string is empty"
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch(ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getExpiration(String accessToken) {

        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        Long now = new Date().getTime();            // 현재 시간

        return (expiration.getTime() - now);
    }

    public boolean checkExpiration(String token) {

        // accessToken 남은 유효시간
        try {
            //Date expirationCheck = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
            //Object expirationCheck = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().values();
            //Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
            //Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().values();
            return true;
        } catch(ExpiredJwtException ignored) {
            //log.debug("jwt test33###################################!!!!!!!!!!!!!!!" );
            return false;
        } catch(IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);

        }
        return false;
    }

    /**
     * Request Header 에서 토큰 정보 추출
     */
    public String resolveToken(HttpServletRequest req, String authHeader) {

        String bearerToken = req.getHeader(authHeader);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            String returnString = bearerToken.substring(7);
            return "null".equals(returnString)?null:returnString;
        }
        return null;
    }

}