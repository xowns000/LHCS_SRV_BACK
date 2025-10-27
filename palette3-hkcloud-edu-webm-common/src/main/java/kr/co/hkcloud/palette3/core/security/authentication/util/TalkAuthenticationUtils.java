package kr.co.hkcloud.palette3.core.security.authentication.util;


import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import kr.co.hkcloud.palette3.core.util.PaletteAntPathUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


/**
 * 
 * @author Orange
 *
 */
@Slf4j
@Component
public class TalkAuthenticationUtils
{
    private static final List<String> EXCLUDE_AUTH_URI_LIST = new ArrayList<String>();


    @PostConstruct
    public void loading()
    {
        //권한체크만 제외 페이지
        EXCLUDE_AUTH_URI_LIST.add("/web/login");  //로그인
        EXCLUDE_AUTH_URI_LIST.add("/palette/web/main");  //메인
        EXCLUDE_AUTH_URI_LIST.add("/monitoring/web/**");  //모니터링...
        EXCLUDE_AUTH_URI_LIST.add("/mng/mypage/web/**");  //자신만...
        EXCLUDE_AUTH_URI_LIST.add("/web/login/web-session-idle-timeout-popup");  //세션 타임아웃
        EXCLUDE_AUTH_URI_LIST.add("/popup/web/TalkCustWaiting");  // 고객 배정 대기중 알림
        EXCLUDE_AUTH_URI_LIST.add("/RepositoryPublic/**");  //RepositoryPublic (라인메신저 외부 오픈 )
    }

//    /**
//     * 
//     * @param request
//     * @return
//     */
//    public String getRequestHeaderRefererUri(HttpServletRequest request)
//    {
//        String referer = request.getHeader("referer");
//        if (StringUtils.isEmpty(referer))
//        {
//            log.info("referer is null or empty!, return null");
//            return null;
//        }
//        log.debug("referer={}", referer);
//        int slashLastPosi = referer.indexOf("/web");
//        return referer.substring(slashLastPosi);
//    }


    /**
     * 
     * @param  requestURI
     * @return
     * @throws TelewebUtilException
     */
    public boolean isExcludeAuthMenu(String requestURI, JSONObject authLogInfoJson) throws TelewebUtilException
    {
        boolean isExcludeAuthURI = false;
        for(String excludeAuthURI : EXCLUDE_AUTH_URI_LIST) {
            isExcludeAuthURI = PaletteAntPathUtils.matches(excludeAuthURI, requestURI);
            if(isExcludeAuthURI) {
                authLogInfoJson.put("excludeAuthURI", excludeAuthURI);
                authLogInfoJson.put("isExcludeAuthURI", isExcludeAuthURI);
                return true;
            }
        }
        return false;
    }
}
