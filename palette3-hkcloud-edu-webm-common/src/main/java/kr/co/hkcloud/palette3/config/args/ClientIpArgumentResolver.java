package kr.co.hkcloud.palette3.config.args;


import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


/**
 * 
 * @author Orange
 *
 */
@Component
public class ClientIpArgumentResolver implements HandlerMethodArgumentResolver
{
    /**
     * resolveArgument를 실행 할 수 있는 method인지 판별
     * 
     * @param  methodParameter
     * @return
     */
    @Override
    public boolean supportsParameter(MethodParameter parameter)
    {
        //@ClientIP 어노테이션이 붙은 파라미터에 대해 적용
        return parameter.hasParameterAnnotation(ClientIP.class);

    }


    /**
     * Method parameter에 대한 Argument Resovler로직 처리
     * 
     * @param  parameter
     * @param  mavContainer
     * @param  webRequest
     * @param  binderFactory
     * @return
     */
    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory)
    {
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        String clientIp = request.getHeader("X-Forwarded-For");
        if(StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("Proxy-Client-IP");
        }
        if(StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("WL-Proxy-Client-IP");
        }
        if(StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_CLIENT_IP");
        }
        if(StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if(StringUtils.isEmpty(clientIp) || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
        return clientIp;
    }

}
