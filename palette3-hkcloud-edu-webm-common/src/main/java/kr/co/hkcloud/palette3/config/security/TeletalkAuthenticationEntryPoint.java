package kr.co.hkcloud.palette3.config.security;


import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.savedrequest.SavedRequest;

import kr.co.hkcloud.palette3.core.util.PaletteAntPathUtils;
import lombok.extern.slf4j.Slf4j;


/**
 * AuthenticationEntryPoint : 인증된 사용자가 SecurityContext에 존재하지도 않고, 어떠한 인증되지 않은 익명의 사용자가 보호된 리소스에 접근하였을 때, 수행되는 EntryPoint 핸들러이다. LoginUrlAuthenticationEntryPoint를 상속한 TeletalkAuthenticationEntryPoint를 등록하였다.
 *
 */
@Slf4j
public class TeletalkAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint
{
    /**
     * 
     * @param loginFormUrl
     */
    public TeletalkAuthenticationEntryPoint(String loginFormUrl) {
        super(loginFormUrl);
        setUseForward(true);
    }


    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException
    {
        log.info("TeletalkAuthenticationEntryPoint.commence ::: {}", request.getRequestURI());
        log.debug("TeletalkAuthenticationEntryPoint.commence ::: {}", authException);

        //서버 오류가 발생한 후 전송된 메뉴 권한 추출 기능을 막는다. 
        String requestURI = request.getRequestURI();
        boolean isExcludeAuthURI = PaletteAntPathUtils.matches("/api/**", requestURI);
        if(isExcludeAuthURI) {
            log.debug("commence :: /api/**");

            String message = new StringBuffer("{\"HEADER\":{\"ERROR_FLAG\":false,\"ERROR_MSG\":\"TeletalkAuthenticationEntryPoint.commence\"},\"DATA\":[{}]}").toString();
            response.setContentType("text/json;charset=" + "UTF-8");
            response.setStatus(401);//HttpStatus.UNAUTHORIZED

            if(PaletteAntPathUtils.matches("/**/selectRtnMenuBtnRole", requestURI)) {
                response.getWriter().print(message);
            }
            if(PaletteAntPathUtils.matches("/**/getSessionInfo", requestURI)) {
                response.getWriter().print(message);
            }
            response.getWriter().flush();
            return;
        }

        SavedRequest savedRequest = (SavedRequest) request.getSession().getAttribute("SPRING_SECURITY_SAVED_REQUEST");
        log.debug("savedRequest={}", savedRequest);

        String errorCode = "-2001";;
        String errorMsg = "권한이 없습니다.";

        log.debug("code={}, msg={}", errorCode, errorMsg);
        request.setAttribute("ERROR_CODE", errorCode);
        request.setAttribute("ERROR_MSG", errorMsg);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        super.commence(request, response, authException);
    }
}
