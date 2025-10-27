package kr.co.hkcloud.palette3.core.security.authentication.app;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import net.sf.json.JSONObject;


/**
 * 
 * @author Orange
 *
 */
public interface TalkAuthenticationService
{
    static final String MAIN_PAGE     = "/main/hkcdv/web/TwbMain";  //메인페이지
    static final String EXPRE_PAGE    = "/error/web/excp01";      //만료페이지
    static final String URL_FAIL_PAGE = "/error/web/excp02";      //잘못된페이지요청
    static final String FAIL_PAGE     = "/error/web/excp04";      //접근실패 알림 페이지


    boolean isAuthMenuPage(HttpServletRequest request, JSONObject authLogInfoJson) throws TelewebAppException;

    boolean hasMenuAuthorSession(String strMenuID, String strUserID) throws TelewebAppException;

    String createSession(TelewebJSON jsonParams, HttpSession objSession, String strUserID, String strLogID) throws TelewebAppException;

    void removeSession(HttpSession objSession) throws TelewebAppException;

    boolean isLogin(HttpSession objSession) throws TelewebAppException;

    String getSessionInfo(HttpSession objSession) throws TelewebAppException;

    String getSessionInfo(HttpSession objSession, int intPosi) throws TelewebAppException;

    String getSessionInfo(String strSessionInfo, int intPosi) throws TelewebAppException;

}
