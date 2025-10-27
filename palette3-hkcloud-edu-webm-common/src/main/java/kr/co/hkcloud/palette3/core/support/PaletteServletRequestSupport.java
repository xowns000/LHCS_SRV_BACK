package kr.co.hkcloud.palette3.core.support;


import javax.servlet.http.HttpServletRequest;

import org.jcodec.common.StringUtils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class PaletteServletRequestSupport {
    /**
     * 접속자 IP 반환
     * 
     * @param  request
     * @return
     */
    public static String getClientIp(HttpServletRequest request) {
        //PROXY 환경 체크
        final String[] checkHeaderKeyList = {"X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
        for(String checkHeaderKey : checkHeaderKeyList) {
            if(!StringUtils.isEmpty(request.getHeader(checkHeaderKey)) && !"unknown".equalsIgnoreCase(request.getHeader(checkHeaderKey))){
                log.debug("request.getHeader[{}] = {}", checkHeaderKey, request.getHeader(checkHeaderKey));
                return request.getHeader(checkHeaderKey);
            }
        }
        log.debug("request.getRemoteAddr() = {}", request.getRemoteAddr());
        return request.getRemoteAddr();
    }
}
