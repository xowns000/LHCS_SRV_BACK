package kr.co.hkcloud.palette3.config.jwt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import kr.co.hkcloud.palette3.config.jwt.domain.TokenDTO;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.core.profile.util.PaletteProfileUtils;
import kr.co.hkcloud.palette3.exception.model.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

/**
 * 클라이언트 요청 시 JWT 인증을 하기 위해 설치하는 Custom Filter로 UsernamePasswordAuthenticationFilter 이전에 실행
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final PaletteProfileUtils paletteProfileUtils;

    private final JwtTokenProvider jwtTokenProvider;
    private final StringRedisTemplate stringRedisTemplate;
    private final StringEncryptor jasyptStringEncryptor;

    private static final String X_V3_AUTHORIZATION_HEADER = "X-V3-Authorization";   //v3 api연계용
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "bearer";

    private final String JWT_TOKEN_REFRESH = "palette:token:refresh";
    private final String JWT_TOKEN_INFO = "palette:token:info";
    private final String JWT_TOKEN_REISSUE = "palette:token:reissue";

    @Value("${palette.security.private.alg}")
    private String P_ALG;

    @Value("${palette.security.private.key}")
    private String P_KEY;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        RequestWrapper httpServletRequest = new RequestWrapper((HttpServletRequest) request);
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        String xtoken = "";                          // request Header -> v3 api용 토큰 추출
        String token = "";                          // request Header -> JWT 토큰 추출
        String loginId = "";                     //사용자 로그인 아이디
        String userId = "";
        String custcoId = "";
        String tenantId = "";
        String locale = httpServletRequest.getHeader("LOCALE") == null ? "ko" : httpServletRequest.getHeader("LOCALE");

        xtoken = jwtTokenProvider.resolveToken(httpServletRequest, X_V3_AUTHORIZATION_HEADER);   // v3 api용 토큰.
        if (StringUtils.isEmpty(xtoken)) { // 일반적인 서비스 인경우.
            token = jwtTokenProvider.resolveToken(httpServletRequest, AUTHORIZATION_HEADER);
            if (!StringUtils.isEmpty(token) && StringUtils.hasText(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                loginId = String.valueOf(authentication.getName());
                userId = jwtTokenProvider.getTokenValue(token, "userId");
                custcoId = jwtTokenProvider.getTokenValue(token, "custcoId");
                tenantId = jwtTokenProvider.getTokenValue(token, "tenantId");

                log.info("Access-Control-Allow-Origin:" + httpServletResponse.getHeader("Access-Control-Allow-Origin"));
                log.info("CERT-CUSTCO-ID:" + httpServletRequest.getHeader("CERT-CUSTCO-ID"));
                log.info("LOCALE:" + httpServletRequest.getHeader("LOCALE"));
                //
                //                if( !StringUtils.isEmpty( httpServletResponse.getHeader("Access-Control-Allow-Origin" ) ) &&
                //                    !StringUtils.isEmpty( httpServletRequest.getHeader("CERT-CUSTCO-ID" ) ) ) {    //비정상 접근 체크용도.

                ErrorCode validateCode = jwtTokenProvider.validateToken(token);
                if (validateCode != ErrorCode.OK) { /* 토큰이 유효하지 않은경우 (refresh토큰 확인후 필요시 재발급) */
                    // Compony 변경시 인증토큰 변경인 경우
                    if ("/auth-api/changeComponyToken".equals(httpServletRequest.getRequestURI())) {
                        // 새로운 토큰 생성
                        custcoId = httpServletRequest.getParameter("DATA[0][SELECTED_CUSTCO_ID]");
                        userId = httpServletRequest.getParameter("DATA[0][SELECTED_USER_ID]");
                        tenantId = httpServletRequest.getParameter("DATA[0][SELECTED_TENANT_ID]");
                    }

                    log.error(">>>>>>>>>>>>>>>>>>>>>>> [" + validateCode.getMessage() + "] lgnId::" + loginId + ",userId::" + userId
                        + ",custcoId::" + custcoId + ",tenantId::" + tenantId);
                    String returnToken = this.generateAndCheckToken(authentication, token, loginId, userId, custcoId, tenantId);
                    if ("".equals(returnToken)) {
                        request.setAttribute("exception", ErrorCode.ACCESS_TOKEN_EXPIRED);
                    }

                } else /* 토큰이 유효한경우 */ {
                    // Compony 변경시 인증토큰 변경인 경우
                    if ("/auth-api/changeComponyToken".equals(httpServletRequest.getRequestURI())) {
                        // 새로운 토큰 생성
                        custcoId = httpServletRequest.getParameter("DATA[0][SELECTED_CUSTCO_ID]");
                        userId = httpServletRequest.getParameter("DATA[0][SELECTED_USER_ID]");
                        tenantId = httpServletRequest.getParameter("DATA[0][SELECTED_TENANT_ID]");
                        String returnToken = this.generateAndCheckToken(authentication, token, loginId, userId, custcoId, tenantId);
                        if ("".equals(returnToken)) {
                            request.setAttribute("exception", ErrorCode.ACCESS_TOKEN_EXPIRED);
                        }
                    } else {
                        //
                        /* Security Context 인증 정보를 저장 */
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }

                httpServletRequest.setParameter("DATA[0][LOCALE]", locale);
                httpServletRequest.setParameter("DATA[0][USER_ID]", userId);
                httpServletRequest.setParameter("DATA[0][CUSTCO_ID]", custcoId);
                //암복호화 키, 알고리즘 설정 - api 변수로 넘어오지 않는 값으로 명칭 정함. 
                httpServletRequest.setParameter("DATA[0][PP_KEY_PP]", P_KEY);
                httpServletRequest.setParameter("DATA[0][PP_ALG_PP]", P_ALG);
                //                }else {
                //                    log.info(">>>>>>>>>>>>>>>>>>>>>>> 비정상접근 :: " + loginId + ","+ PaletteServletRequestSupport.getClientIp( (HttpServletRequest) request ));
                //                    SecurityContextHolder.getContext().setAuthentication(null);
                //                    request.setAttribute("exception", ErrorCode.ACCESS_DENIED);
                //                }

            } else {
                SecurityContextHolder.getContext().setAuthentication(null);
                request.setAttribute("exception", ErrorCode.ACCESS_DENIED);
            }
        } else { // v3 api 서비스 요청인경우.
            try {
                log.info(X_V3_AUTHORIZATION_HEADER + " >>>>>>>>>>>>>>>>>>>>>>> [" + paletteProfileUtils.getActiveProfile().toString() + "] "
                    + jasyptStringEncryptor.decrypt(xtoken));
                String xtokenDecrypt = jasyptStringEncryptor.decrypt(xtoken);
                String[] xtokenDecrypts = xtokenDecrypt.split(
                    "_##_");  //public_##_1_##_plt30_##_1_##_202312015959_##_bb7c9894-8d00-49b1-b003-e66fead8c501

                log.info(X_V3_AUTHORIZATION_HEADER + " >>>>>>>>>>>>>>>>>>>>>>> xtokenDecrypts.length " + xtokenDecrypts.length);

                if (xtokenDecrypts.length > 5) {
                    String xTenantId = xtokenDecrypts[0];   //테넌트아이디
                    String xCustcoId = xtokenDecrypts[1];   //CUSTCO_ID
                    String xLoginId = xtokenDecrypts[2];    //인증키 로그인아이디
                    String xUserId = xtokenDecrypts[3];    //인증키 로그인아이디
                    String xExpired = xtokenDecrypts[4];    //키 만료일자
                    String xDummy = xtokenDecrypts[5];      //더미데이타
                    String xprofile = "";
                    try {
                        xprofile = xtokenDecrypts[6];
                    } catch (Exception e) {
                    }

                    if (xExpired.length() == 14) {
                        TenantContext.setCurrentCustco(xCustcoId);
                        TenantContext.setCurrentTenant(xTenantId);

                        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
                        authorities.add(new SimpleGrantedAuthority("ROLE_1"));
                        UserDetails principal = new User(xLoginId, "", authorities);
                        SecurityContextHolder.getContext()
                            .setAuthentication(new UsernamePasswordAuthenticationToken(principal, "", authorities));

                        httpServletRequest.setParameter("DATA[0][SCHEMA_ID]", xTenantId);
                        httpServletRequest.setParameter("DATA[0][LOCALE]", locale);
                        httpServletRequest.setParameter("DATA[0][USER_ID]", xUserId);
                        httpServletRequest.setParameter("DATA[0][CUSTCO_ID]", xCustcoId);
                        httpServletRequest.setParameter("DATA[0][PP_KEY_PP]", P_KEY);
                        httpServletRequest.setParameter("DATA[0][PP_ALG_PP]", P_ALG);
                        if ("".equals(xprofile)) {
                            xprofile = xDummy;
                        }

                        log.info(">> :: " + paletteProfileUtils.getActiveProfile().toString());
                        // starlaw에 이미 발급한 키로 인해 starlaw 이외(신성통상등) 에서 호출인경우. 토큰 체크.
                        if (!"allpass".equals(xprofile) && !"starlaw".equals(xTenantId)
                            && (paletteProfileUtils.getActiveProfile().toString()).indexOf("production") > -1) {

                            if ("dev".equals(xprofile)) {
                                xprofile = "production";
                            }

                            if (!(paletteProfileUtils.getActiveProfile().toString()).equals(xprofile)) {
                                SecurityContextHolder.getContext().setAuthentication(null);
                                request.setAttribute("exception", ErrorCode.X_ACCESS_DENIED);
                            }
                        }
                    } else {
                        SecurityContextHolder.getContext().setAuthentication(null);
                        request.setAttribute("exception", ErrorCode.X_ACCESS_DENIED);
                    }
                } else {
                    SecurityContextHolder.getContext().setAuthentication(null);
                    request.setAttribute("exception", ErrorCode.X_ACCESS_DENIED);
                }
            } catch (org.jasypt.exceptions.EncryptionOperationNotPossibleException eonpe) {
                SecurityContextHolder.getContext().setAuthentication(null);
                request.setAttribute("exception", ErrorCode.X_ACCESS_DENIED);
            }
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    private String generateAndCheckToken(Authentication authentication, String token, String loginId, String userId, String custcoId,
        String tenantId) {

        String refToken = "";                   //사용자 Refresh Token
        String accessToken = String.valueOf(
            stringRedisTemplate.opsForValue().get(JWT_TOKEN_REISSUE + ":" + token));   //palette:token:reissue
        if (StringUtils.isEmpty(accessToken) || "null".equals(accessToken)) {
            accessToken = token;
        }
        // 레디스에서 토큰값 조회 하여 refresh토큰과 비교하여 유효성체크
        refToken = String.valueOf(
            stringRedisTemplate.opsForValue().get(JWT_TOKEN_REFRESH + ":" + loginId));                     //palette:token:refresh
        if ("null".equals(refToken)) {
            refToken = "";
        }
        if (!StringUtils.isEmpty(refToken)) { /* 빈값이 아니면서 추출된 RefreshToken 동일한 경우 */

            // reflash token 유효시간 존재 시 존재여부 확인 하여 access/reflash token 재 발행
            Long refreshGetExpiration = jwtTokenProvider.getExpiration(refToken);
            if (refreshGetExpiration > 0) {
                // 새로운 토큰 생성
                TokenDTO.TokenInfo tokenInfo = jwtTokenProvider.generateToken(authentication,
                    "custcoId::" + custcoId + ",userId::" + userId + ",lgnId::" + loginId + ",tenantId::" + tenantId);

                String refreshToken = "";

                accessToken = String.valueOf(tokenInfo.getAccessToken());
                refreshToken = String.valueOf(tokenInfo.getRefreshToken());

                /* Redis Delete */
                //test
                stringRedisTemplate.delete(JWT_TOKEN_REISSUE + ":" + token);         //palette:token:reissue
                stringRedisTemplate.delete(JWT_TOKEN_INFO + ":" + loginId + ":" + token);        //palette:token:info
                stringRedisTemplate.delete(JWT_TOKEN_REFRESH + ":" + loginId);                   //palette:token:refresh

                /* Redis Set */
                stringRedisTemplate.opsForValue()
                    .set(JWT_TOKEN_REISSUE + ":" + token, accessToken, tokenInfo.getRefreshTokenExpirationTime(),
                        TimeUnit.MILLISECONDS);        //palette:token:reissue
                stringRedisTemplate.opsForValue()
                    .set(JWT_TOKEN_INFO + ":" + loginId + ":" + accessToken, refreshToken, tokenInfo.getRefreshTokenExpirationTime(),
                        TimeUnit.MILLISECONDS); // palette:token:info 사용자_AccessToken 기준 Refresh Token
                stringRedisTemplate.opsForValue()
                    .set(JWT_TOKEN_REFRESH + ":" + loginId, refreshToken, tokenInfo.getRefreshTokenExpirationTime(),
                        TimeUnit.MILLISECONDS); // palette:token:refresh 리프레쉬 토큰 저장

                /* 변경된 토큰 Authentication 객체를 가지고 와서 SecurityContext 에 저장 */
                authentication = jwtTokenProvider.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info(">>>>>>>>>>>>>>>>>>>>>>> 2. [ JWT 재발급 ] lgnId::" + loginId + ",userId::" + userId + ",custcoId::" + custcoId
                    + ",tenantId::" + tenantId);

                return accessToken;
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * Parameter 조작을 위한.
     */
    private class RequestWrapper extends HttpServletRequestWrapper {

        HashMap<String, Object> params;

        public RequestWrapper(HttpServletRequest request) {
            super(request);
            this.params = new HashMap<String, Object>(request.getParameterMap());
        }

        public String getParameter(String name) {
            String returnValue = null;
            String[] paramArray = getParameterValues(name);
            if (paramArray != null && paramArray.length > 0) {
                returnValue = paramArray[0];
            }
            return returnValue;
        }

        @SuppressWarnings("unchecked")
        public Enumeration getParameterNames() {
            return Collections.enumeration(params.keySet());
        }

        public String[] getParameterValues(String name) {
            String[] result = null;
            String[] temp = (String[]) params.get(name);
            if (temp != null) {
                result = new String[temp.length];
                System.arraycopy(temp, 0, result, 0, temp.length);
            }
            return result;
        }

        public void setParameter(String name, String value) {
            String[] oneParam = {value};
            setParameter(name, oneParam);
        }

        public void setParameter(String name, String[] value) {
            params.put(name, value);
        }

    }
}