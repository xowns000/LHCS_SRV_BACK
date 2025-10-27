package kr.co.hkcloud.palette3.config.security;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import kr.co.hkcloud.palette3.config.security.util.TeletalkSecurityLoggerUtils;
import kr.co.hkcloud.palette3.core.security.authentication.app.TalkAuthenticationService;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


/**
 * 텔레톡 권한 인터셉터
 * 
 * @author Orange
 *
 */
@Slf4j
@Component
public class TeletalkAuthenticationInterceptor implements HandlerInterceptor
{
    private TalkAuthenticationService talkAuthenticationService;


    /**
     * @param talkAuthenticationService
     */
    @Autowired
    @Lazy
    public void setTalkAuthenticationService(TalkAuthenticationService talkAuthenticationService)
    {
        this.talkAuthenticationService = talkAuthenticationService;
    }


    private TeletalkSecurityLoggerUtils teletalkSecurityLoggerUtils;


    @Autowired
    public void setTeletalkSecurityLoggerUtils(TeletalkSecurityLoggerUtils teletalkSecurityLoggerUtils)
    {
        this.teletalkSecurityLoggerUtils = teletalkSecurityLoggerUtils;
    }


    /**
     * 맵핑되기 전 처리
     * 
     * @see kr.co.hkcloud.palette.config.webmvc.SpringWebMvcConfig
     * @see kr.co.hkcloud.palette.config.security.SpringSecurityConfig
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception
    {
        log.debug("===================       MENU AUTHENTICATION START       ===================");

        StopWatch stopWatch = new StopWatch(this.getClass().getName());
        /////////////////////////////////////////////////////////////////////////////////
        //CHECK AUTH
        stopWatch.start("check-auth");

        JSONObject authLogInfoJson = new JSONObject();
        authLogInfoJson.put("Url", request.getRequestURL().toString());
        authLogInfoJson.put("Uri", String.format("%s    (%s)", request.getRequestURI(), request.getMethod()));
        {
            if(!talkAuthenticationService.isAuthMenuPage(request, authLogInfoJson)) {
                authLogInfoJson.put("Check Result", "...Access Denied");
                teletalkSecurityLoggerUtils.menuAuthenticationLog(authLogInfoJson);

                //메뉴ID와 세션정보를 이용하여 페이지 권한체크 대상일 경우 권한이 없을 경우 자동 잘못된 페이지 요청으로 분기시킨다.
                response.sendRedirect(TalkAuthenticationService.FAIL_PAGE);
                return false;
            }
            authLogInfoJson.put("Check Result", "Allow Access...");
            teletalkSecurityLoggerUtils.menuAuthenticationLog(authLogInfoJson);
        }
        teletalkSecurityLoggerUtils.menuAuthenticationLog(stopWatch);
        return true;
    }


    /**
     * 맵핑되고난 후 처리
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception
    {
        log.debug("===================       MENU AUTHENTICATION END       ===================");
    }


    /**
     * 모든 작업이 완료된 후 실행
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
    {
        log.trace("===================       MENU AUTHENTICATION COMPLETION       ===================");
    }
}
