package kr.co.hkcloud.palette3.v3.auth.service.impl;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import kr.co.hkcloud.palette3.config.jwt.JwtTokenProvider;
import kr.co.hkcloud.palette3.config.jwt.domain.TokenDTO.TokenInfo;
import kr.co.hkcloud.palette3.core.security.authentication.domain.PaletteUserDetailsVO;
import kr.co.hkcloud.palette3.exception.teleweb.BusinessException;
import kr.co.hkcloud.palette3.v3.auth.api.dto.V3AuthLoginRequestDto;
import kr.co.hkcloud.palette3.v3.auth.api.dto.V3UserAppUpdateTokenRequestDto;
import kr.co.hkcloud.palette3.v3.auth.service.V3AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service("v3AuthService")
public class V3AuthServiceImpl extends EgovAbstractServiceImpl implements V3AuthService {
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;
    
    @Value("${jwt.token.info}")
    private String JWT_TOKEN_INFO;

    @Value("${jwt.token.refresh}")
    private String JWT_TOKEN_REFRESH;
    
    private final SqlSessionTemplate paletteRoutingSqlSessionTemplate;
    private String namespace = "kr.co.hkcloud.palette3.v3.auth.dao.V3AuthMapper.";
    
    
    @Override
    @Transactional(readOnly=true)
    public String selectTenantByLgnId(V3AuthLoginRequestDto requestDto) throws BusinessException {
        HashMap<String, Object> certCustco = paletteRoutingSqlSessionTemplate.selectOne(namespace + "selectTenantByLgnId", requestDto);
        /* 사용자 체크 */
        if (ObjectUtils.isEmpty(certCustco)) {
            throw new BadCredentialsException("Invalid username or password :::");
        }
        return (String)certCustco.get("schema_id");
    }
    
    
    @Override
    public TokenInfo changeTokenExpireTime(String currentAccessToken) throws BusinessException {
        Authentication authentication = jwtTokenProvider.getAuthentication(currentAccessToken);
        String loginId = authentication.getName();
        PaletteUserDetailsVO newAuthentication = new PaletteUserDetailsVO(
            Integer.parseInt(jwtTokenProvider.getTokenValue(currentAccessToken, "userId")), 
            String.valueOf(loginId), 
            null, null, "",
            false, false, false, false, 
            authentication.getAuthorities(), null, null, 0, null, null, 
            jwtTokenProvider.getTokenValue(currentAccessToken, "custcoId"),
            jwtTokenProvider.getTokenValue(currentAccessToken, "tenantId"), null, null, null
        );
        
        //9시간짜리 토큰 발행 후 기존 토큰 대체.
        long accessTokenExpireTime = 24 * 60 * 60 * 365 * 1000L;
        TokenInfo tokenInfo = jwtTokenProvider.generateTokenWithExpireTime(newAuthentication, accessTokenExpireTime);
        String accessToken = tokenInfo.getAccessToken();
        String refreshToken = tokenInfo.getRefreshToken();
        redisTemplate.delete(JWT_TOKEN_INFO + ":" + loginId + ":" + currentAccessToken);    //Redis SHA256 ENC 키값 삭제
        redisTemplate.delete(JWT_TOKEN_REFRESH + ":" + loginId);    //리프레쉬 토큰 삭제
        
        redisTemplate.opsForValue().set(JWT_TOKEN_INFO + ":" + loginId + ":" + accessToken, refreshToken, tokenInfo
            .getRefreshTokenExpirationTime(), TimeUnit.MILLISECONDS); //사용자_AccessToken 기준 Refresh Token
        redisTemplate.opsForValue().set(JWT_TOKEN_REFRESH + ":" + loginId, refreshToken, tokenInfo.getRefreshTokenExpirationTime(),
            TimeUnit.MILLISECONDS); // 리프레쉬 토큰 저장
        
        return tokenInfo;
    }
    
    
    @Transactional(readOnly=false)
    public long updateUserToken(V3UserAppUpdateTokenRequestDto requestDto) throws BusinessException {
        paletteRoutingSqlSessionTemplate.update(namespace + "updateUserToken", requestDto);
        return requestDto.getUserId();
    }
    
}
