package kr.co.hkcloud.palette3.config.jwt;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.hkcloud.palette3.exception.model.ErrorCode;
import kr.co.hkcloud.palette3.exception.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * 인증관련 예외발생관련 응답.
 *
 * @author USER
 * @version 1.0
 */
@Slf4j
@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ErrorCode exceptionCode = (ErrorCode)request.getAttribute("exception");
        if(exceptionCode != null) {
            String requestURI = request.getRequestURI();
            if(requestURI.equals("/v3-api/auth/logout") || requestURI.startsWith("/v3-api/user/") || requestURI.startsWith("/v3-api/carrier/")) {
                //restfulapi 에러 형식으로 리턴
                setResponse(response, exceptionCode, "restapi");
            } else {
                setResponse(response, exceptionCode, "palette");
            }
        }
    }

    private void setResponse(HttpServletResponse response, ErrorCode exceptionCode, String responseType) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus( exceptionCode.getStatus() );
        if("palette".equals(responseType)) {
            String message = new StringBuffer("{\"HEADER\":{\"ERROR_FLAG\":true,\"ERROR_MSG\":\""+ exceptionCode.getMessage() +"\"},\"DATA\":[{}]}").toString();
            response.getWriter().print(message);
        } else if("restapi".equals(responseType)) {
            //리턴 예 : {"code": "C902", "message": "인증정보가 만료 되었습니다.(Expired JWT Token)", "status": 401, "errors": []}
            ErrorResponse errorResponse = ErrorResponse.of(exceptionCode);
            ObjectMapper mapper = new ObjectMapper();
            response.getWriter().print(mapper.writeValueAsString(errorResponse));
        }
        
    }
}
