package kr.co.hkcloud.palette3.v3.auth.api;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.security.auth.login.AccountExpiredException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.app.TwbUserBizServiceImpl;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.jwt.JwtTokenProvider;
import kr.co.hkcloud.palette3.config.jwt.domain.TokenDTO.TokenInfo;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.core.security.crypto.Hash;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.login.app.LoginService;
import kr.co.hkcloud.palette3.login.app.LogoutService;
import kr.co.hkcloud.palette3.login.domain.LoginDTO;
import kr.co.hkcloud.palette3.login.domain.LoginDTO.Login;
import kr.co.hkcloud.palette3.v3.auth.api.dto.V3AuthLoginRequestDto;
import kr.co.hkcloud.palette3.v3.auth.api.dto.V3AuthLoginResponseDto;
import kr.co.hkcloud.palette3.v3.auth.api.dto.V3UserAppUpdateTokenRequestDto;
import kr.co.hkcloud.palette3.v3.auth.service.V3AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "V3AuthRestApiController", description = "팔레트 인증(로그인/로그아웃) 컨트롤러")
public class V3AuthRestApiController {
    private final V3AuthService v3AuthService;
    private final TwbUserBizServiceImpl twbUserBizServiceImpl;
    private final RedisTemplate<String, String> redisTemplate;
    private final LoginService loginService;
    private final LogoutService logoutService;
    private final JwtTokenProvider jwtTokenProvider;
    
    @Value("${jwt.token.shc256}")
    private String JWT_TOKEN_SHC256;

    @Value("${jwt.token.nm}")
    private String JWT_TOKEN_NM;
    
    private static final String AUTHORIZATION_HEADER = "Authorization";
    
    
    //토큰 expired  시간 : 9시간
    @ApiOperation(value = "팔레트3 로그인", notes = "팔레트 로그인 인증 처리 및 인증 토큰 반환")
    @PostMapping("/v3-api/auth/login")
    public ResponseEntity<V3AuthLoginResponseDto> login(@Validated @RequestBody V3AuthLoginRequestDto requestDto, HttpServletRequest req) throws InvalidAlgorithmParameterException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, AccountExpiredException {
        String schemaId = v3AuthService.selectTenantByLgnId(requestDto);
        TenantContext.setCurrentTenant(schemaId);
        
        Login login = new Login();

        String username = "";
        String userpassword = "";

        username = requestDto.getId();
        userpassword = requestDto.getPw();

        String shaPwd;
        try {
            shaPwd = Hash.encryptSHA256(userpassword, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            throw new TelewebAppException(e.getLocalizedMessage());
        }
        //비번 EncryptKey 값 추출
        String strEncryptKey = twbUserBizServiceImpl.createEncryptKey();
        //Redis 삭제 
        redisTemplate.delete(JWT_TOKEN_SHC256 + ":" + username);

        //Redis 저장 
        redisTemplate.opsForValue().set(JWT_TOKEN_SHC256 + ":" + username, strEncryptKey);
        redisTemplate.expire(JWT_TOKEN_SHC256 + ":" + username, 3, TimeUnit.MINUTES); // SHA256 암호화 키값 Redis 유효시간 설정
        
        //비번 sha2==> aes256
        String encPswd = twbUserBizServiceImpl.pwdEncString(shaPwd, strEncryptKey);

        userpassword = encPswd;

        if(username.isEmpty() || username == null || userpassword.isEmpty() || userpassword == null) { throw new BadCredentialsException("필수 입력값을 입력하세요"); }

        login.setUsername(username);
        login.setUserpassword(userpassword);
        login.toAuthentication();

        //최종결과값 반환
        TelewebJSON objRetParams = loginService.login(login, req);
        String accessToken = objRetParams.getHeaderString("ACCESS_TOKEN");
        
        //!!!1시간 짜리 토큰을 삭제 후 9시간 짜리 토큰으로 변경한다.!!!
        TokenInfo tokenInfo = v3AuthService.changeTokenExpireTime(accessToken);
        
        V3AuthLoginResponseDto responseDto = new V3AuthLoginResponseDto();
        responseDto.setAccessToken(tokenInfo.getAccessToken());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    
    
    @ApiOperation(value = "팔레트3 로그아웃", notes = "팔레트 로그인 아웃 처리")
    @PostMapping("/v3-api/auth/logout")
    public ResponseEntity<Object> logout(HttpServletRequest req) throws InvalidAlgorithmParameterException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, AccountExpiredException {
        LoginDTO.Logout logout = new LoginDTO.Logout();

        String accessToken = jwtTokenProvider.resolveToken(req, AUTHORIZATION_HEADER);
        
        // validation check
        if(accessToken.isEmpty() || accessToken == null) {
            //throw new BadCredentialsException(String.valueOf(Helper.refineErrors(errors)));
            throw new BadCredentialsException("잘못된요청입니다.");
        }
        String jessionid = "";
        int userid = Integer.parseInt(jwtTokenProvider.getTokenValue(accessToken, "userId"));
        
        logout.setAccessToken(accessToken);
        logout.setJessionid(jessionid);
        logout.setUserid(userid);
        
        TelewebJSON objRetParams = logoutService.logout(logout, req);
        
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
    
    
  //토큰 expired  시간 : 9시간
    @ApiOperation(value = "사용자 앱 알림 토큰 수정", notes = "사용자의 앱 알림 토큰 수정")
    @PutMapping("/v3-api/user/app/updateToken")
    public ResponseEntity<Object> updateToken(@Validated @RequestBody V3UserAppUpdateTokenRequestDto requestDto, HttpServletRequest req) throws InvalidAlgorithmParameterException, UnsupportedEncodingException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException, AccountExpiredException {
        String accessToken = jwtTokenProvider.resolveToken(req, AUTHORIZATION_HEADER);
        requestDto.setUserId(Long.parseLong(jwtTokenProvider.getTokenValue(accessToken, "userId")));
        
        v3AuthService.updateUserToken(requestDto);
        
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}
