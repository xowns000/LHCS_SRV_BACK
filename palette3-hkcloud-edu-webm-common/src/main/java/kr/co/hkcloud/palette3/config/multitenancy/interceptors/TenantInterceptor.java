package kr.co.hkcloud.palette3.config.multitenancy.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import kr.co.hkcloud.palette3.common.palette.app.PaletteCmmnService;
import kr.co.hkcloud.palette3.config.jwt.JwtTokenProvider;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.AsyncHandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * 테넌트 Interceptor
 *
 * 모든 api 요청을 받아 header에서 CERT_CUSTCO_ASP_CUST_KEY 값을 TenantContext에 세팅한다.
 * TenantContext.setCurrentTenant(tenantCode)
 *
 * @author 황종혁
 */
@Component
@Slf4j
public class TenantInterceptor implements AsyncHandlerInterceptor {

    private static final String X_V3_AUTHORIZATION_HEADER = "X-V3-Authorization";   //v3 api연계용
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "bearer";

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private StringEncryptor jasyptStringEncryptor;

    @Autowired
    private PaletteCmmnService paletteCmmnService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {
        //멀티테넌시(multitenancy) 처리 : frontend에서 보내준 헤더값 "CERT_CUSTCO_TENANT_ID"을 받아서 TenantContext 설정.
        //frontend에서 backend api를 axios로 호출 시, axios interceptor에서 아래와 같이 header를 세팅함.
        //request.headers.CERT_CUSTCO_TENANT_ID = store.getters["authStore/GE_CUSTCO_TENANT_ID"];

        String custcoCode = request.getHeader("CERT-CUSTCO-ID");
        String tenantCode = request.getHeader("CERT-CUSTCO-TENANT-ID");
        boolean isUploadImage = false;
        String requestURI = request.getRequestURI();

        String token = jwtTokenProvider.resolveToken(request, AUTHORIZATION_HEADER);   // jwt 토큰.
        if (StringUtils.isEmpty(token)) {
            //인증 고객사 관리는 custco 스키마에서 관리되므로 관련 api 호출 시 custco 로 강제 지정.
            if (requestURI.startsWith("/admin-api/") || "/error".equals(requestURI)) {
                tenantCode = TenantContext.DEFAULT_TENANT_CODE;
            }
            if (requestURI.startsWith("/upload/images/")) {
                isUploadImage = true;
            }
            //채팅으로 들어온 api는 테넌트명을 검색해와야 함
            if (requestURI.startsWith("/reference") || requestURI.startsWith("/message") || requestURI.startsWith("/expired_session")) {
                //채팅 인입시 테넌트 구분 => TeletalkReceiverUtiles로 변경
                if (TenantContext.getCurrentCustco() == null && TenantContext.getCurrentCustco().equals("")) {
                    custcoCode = TenantContext.DEFAULT_CUSTCO_CODE;
                }
                if (TenantContext.getCurrentTenant() == null && TenantContext.getCurrentTenant().equals("")) {
                    tenantCode = TenantContext.DEFAULT_TENANT_CODE;
                }
            }
            if (StringUtils.isEmpty(custcoCode)) {
                custcoCode = TenantContext.DEFAULT_CUSTCO_CODE;
            }
            if (StringUtils.isEmpty(tenantCode)) {
                tenantCode = TenantContext.DEFAULT_TENANT_CODE;
            }

            if (!isUploadImage && (tenantCode == null || !StringUtils.hasText(tenantCode))) {
                log.error("=========== requestURI ::: " + requestURI);
                log.error("멀티네넌시(multitenancy) 관련 오류 발생 ::: CERT_CUSTCO_TENANT_ID IS NULL");
                throw new IllegalArgumentException("CERT_CUSTCO_TENANT_ID IS NULL");
            }
        } else {
            // jwt 토큰이 있는경우에는 tenantId, custcoId 를 토큰에서 추출하여 처리함.
            custcoCode = jwtTokenProvider.getTokenValue(token, "custcoId");
            tenantCode = jwtTokenProvider.getTokenValue(token, "tenantId");
        }

        if ( requestURI.startsWith("/v3-api/")) {
            //로그인은 테넌트가 없으므로 custco로 지정
            if(requestURI.equals("/v3-api/auth/login")) {
                custcoCode = TenantContext.DEFAULT_CUSTCO_CODE;
                tenantCode = TenantContext.DEFAULT_TENANT_CODE;
            } else if(requestURI.equals("/v3-api/auth/logout") || requestURI.startsWith("/v3-api/user/") || requestURI.startsWith("/v3-api/carrier/")) {
                //위에서 AUTHORIZATION_HEADER로 토큰을 받아서 custcoCode, tenantCode 값이 설정됨
            } else {
                String xtoken = jwtTokenProvider.resolveToken(request, X_V3_AUTHORIZATION_HEADER);   // v3 api용 토큰.
                try {
                    String xtokenDecrypt = jasyptStringEncryptor.decrypt(xtoken);
                    String[] xtokenDecrypts = xtokenDecrypt.split(
                        "_##_");  //public_##_1_##_plt30_##_1_##_202312015959_##_bb7c9894-8d00-49b1-b003-e66fead8c501
                    if (xtokenDecrypts.length > 5) {
                        String xTenantId = xtokenDecrypts[0];   // 테넌트아이디
                        String xCustcoId = xtokenDecrypts[1];   // CUSTCO_ID
                        tenantCode = xTenantId;
                        custcoCode = xCustcoId;
                    }
                } catch (org.jasypt.exceptions.EncryptionOperationNotPossibleException eonpe) {
                    log.error(eonpe.toString());
                }
            }
        }
        TenantContext.setCurrentCustco(custcoCode);
        TenantContext.setCurrentTenant(tenantCode);

        return true;
    }

    /**
     * 멀티테넌시(multitenancy) 처리 : TenantContext clear 처리
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        TenantContext.clear();
    }
}