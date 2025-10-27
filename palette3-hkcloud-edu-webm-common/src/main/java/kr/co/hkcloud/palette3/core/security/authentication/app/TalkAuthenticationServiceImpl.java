package kr.co.hkcloud.palette3.core.security.authentication.app;


import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.properties.palette.PaletteProperties;
import kr.co.hkcloud.palette3.core.security.authentication.dao.TalkAuthenticationMapper;
import kr.co.hkcloud.palette3.core.security.authentication.domain.PaletteUserDetailsVO;
import kr.co.hkcloud.palette3.core.security.authentication.domain.TalkAuthenticationVO;
import kr.co.hkcloud.palette3.core.security.authentication.util.TalkAuthenticationUtils;
import kr.co.hkcloud.palette3.core.support.PaletteUserContextSupport;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


/**
 * @author Orange
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("TalkAuthenticationService")
public class TalkAuthenticationServiceImpl implements TalkAuthenticationService
{
    private final PaletteProperties paletteProperties;

    private final TwbComDAO               mobjDao;
    private final TalkAuthenticationUtils talkAuthenticationUtils;

    private TalkAuthenticationMapper talkAuthenticationDao;


    @Autowired
    public void setTalkAuthenticationMapper(TalkAuthenticationMapper talkAuthenticationDao)
    {
        this.talkAuthenticationDao = talkAuthenticationDao;
    }


    /**
     * 권한체크만
     */
    @Override
    public boolean isAuthMenuPage(HttpServletRequest request, JSONObject authLogInfoJson) throws TelewebAppException
    {
        String requestURI = request.getRequestURI();
        if(StringUtils.isNotEmpty(requestURI)) {
            if(talkAuthenticationUtils.isExcludeAuthMenu(requestURI, authLogInfoJson)) {
                return true;
            }
            else {
                String menuId = "";
//                if (requestURI.indexOf("/web") >= 0 || requestURI.indexOf("/Twb") >= 0 || requestURI.indexOf("/Servlet") >= 0)
//                {
//                    String requestHeaderRefererUri = getRequestHeaderRefererUri(request);
//                    if(isExcludeMenuAuth(requestHeaderRefererUri))
//                    {
//                        return true;
//                    }
//
//                    //메뉴ID추출
//                    if (requestHeaderRefererUri.indexOf("MENU_ID=") >= 0)
//                    {
//                        menuId = requestHeaderRefererUri.substring(requestHeaderRefererUri.indexOf("MENU_ID=") + 8);
//                        if (menuId.indexOf("&") >= 0)
//                        {
//                            menuId = menuId.substring(0, menuId.indexOf("&"));
//                        }
//                    }
//                }
//                else
//                {
//                    menuId = request.getParameter("MENU_ID");
//                }

                menuId = request.getParameter("MENU_ID");

                //메뉴ID가 없을 경우 자동 잘못된 페이지 요청으로 분기시킨다.
                if(StringUtils.isEmpty(menuId) || menuId.equals("undefined")) {
                    authLogInfoJson.put("Message", "menuId is null or empty!");
                    return false;
                }

                PaletteUserDetailsVO talkUserDetailsVO = PaletteUserContextSupport.getCurrentUser();
                if(ObjectUtils.isEmpty(talkUserDetailsVO)) {
                    authLogInfoJson.put("Message", "talkUserDetailsVO is null!");
                    return false;
                }

                String userId = talkUserDetailsVO.getUsername();
                if(StringUtils.isEmpty(userId)) {
                    authLogInfoJson.put("Message", "UserId is null!");
                    return false;
                }
                authLogInfoJson.put("UserId", userId);
                authLogInfoJson.put("MenuId", menuId);
                TalkAuthenticationVO talkAuthenticationVO = TalkAuthenticationVO.builder().userId(userId).menuId(menuId).build();
                int hasCnt = talkAuthenticationDao.selectChkAuthMenu(talkAuthenticationVO);
                return (hasCnt > 0);
            }
        }
        else {
            authLogInfoJson.put("Message", "requestURI is null!");
        }
        return false;
    }


    /**
     * 
     * @param  strMenuID
     * @param  strUserID
     * @return
     */
    @Override
    public boolean hasMenuAuthorSession(String strMenuID, String strUserID) throws TelewebAppException
    {
        TelewebJSON jsonParams = new TelewebJSON();
        jsonParams.setHeader("TELEWEB_IDENTIFIER", DateFormatUtils.format((new Date()), "HH:mm:ss") + "-" + UUID.randomUUID().toString().substring(0, 8));

        try {
            //파라메터체크
            if(strMenuID.equals("") || strUserID.equals("")) { return false; }

            jsonParams.setString("USER_ID", 0, strUserID);
            jsonParams.setString("MENU_ID", 0, strMenuID);

            //메뉴ID에 설정된 사용자 검색
            TelewebJSON jsonRetParams = mobjDao.select("com.hcteletalk.teletalk.mng.bas.bas01.dao.TwbBas01Mapper", "chkHasMenuAuthoity", jsonParams);

            if(jsonRetParams.getInt("HAS_CNT", 0) == 0) { return false; }
        }
        catch(Exception e) {
            return false;
        }

        return true;
    }

//    /**
//     * 페이지 필터를 이용하여 세션체크 대상일 경우 세션여부를 체크한다.
//     * @param objRequest 
//     * @param objSession 
//     * @return
//     */
//    @Override
//    public boolean isNormaltyRequest(HttpServletRequest objRequest, HttpSession objSession) throws TelewebAppException
//    {
//        try
//        {
//            String strRequestURL = objRequest.getRequestURI();                                          //요청URL + 페이지
//            String strMenuID = "";
//            
//            //채널호출일 경우에는 해더 정보에서 요청 URL을 추출한다.
//            if (strRequestURL.indexOf("/TelewebChannel") >= 0 || strRequestURL.indexOf("Servlet") >= 0)
//            {
//                strRequestURL = objRequest.getHeader("referer");
//
//                //메뉴ID추출
//                if (strRequestURL.indexOf("MENU_ID=") >= 0)
//                {
//                    strMenuID = strRequestURL.substring(strRequestURL.indexOf("MENU_ID=") + 8);
//                    if (strMenuID.indexOf("&") >= 0)
//                    {
//                        strMenuID = strMenuID.substring(0, strMenuID.indexOf("&"));
//                    }
//                }
//
//                //페이지 URL 추출
//                strRequestURL = strRequestURL.substring(strRequestURL.indexOf("webapps") - 1);
////                strRequestURL = strRequestURL.substring(0, strRequestURL.indexOf(".jsp") + 4);
//            }
//            else
//            {
//                strMenuID = objRequest.getParameter("MENU_ID");                                         //권한 체크할 메뉴ID 추출
//            }
//
//            int intPosi = strRequestURL.lastIndexOf("/") + 1;                                           //URL과 페이지 구분 위치
//            String strRequestPath = strRequestURL.substring(0, intPosi);                                    //요청URL
//            String strRequestPage = strRequestURL.substring(intPosi);                                   //요청페이지
//
//            //메뉴권한 체크 대상 페이지 체크
//            boolean isCheck = true;
//            if ("/".equals(strRequestPath))
//            {
//                isCheck = false;
//            }
//
//            for (int i = 0; i < mstrExcludeAuthList.length; i++)
//            {
//                if (strRequestPage.equals(mstrExcludeAuthList[i]))
//                {
//                    isCheck = false;
//                    break;
//                }
//            }
//            if (isCheck == true)
//            {
//                for (int j = 0; j < mstrExcludeAuthList.length; j++)
//                {
//                    if (strRequestPath.indexOf(mstrExcludeAuthList[j]) >= 0)
//                    {
//                        isCheck = false;
//                        break;
//                    }
//                }
//            }
//
//            if (isCheck == true)
//            {
//                //메뉴ID가 없을 경우 자동 잘못된 페이지 요청으로 분기시킨다.
//                if (strMenuID == null || strMenuID.equals("") || strMenuID.equals("undefined")) { return false; }
//            }
//        }
//        catch (Exception e)
//        {
//            return false;
//        }
//        return true;
//    }
//
//    /**
//     * 페이지 필터를 이용하여 세션체크 대상일 경우 세션여부를 체크한다.
//     * @param objRequest 
//     * @param objSession 
//     * @return
//     */
//    @Override
//    public boolean isSessionCheck(HttpServletRequest objRequest, HttpSession objSession) throws TelewebAppException
//    {
//        try
//        {
//            String strRequestURL = objRequest.getRequestURI();                                          //요청URL + 페이지
//
//            // 웹소켓 상태 체크 url 세션 체크 제외 , sjh 20181115 
//            if (strRequestURL != null
//                    && (strRequestURL.equals("/TelewebChannel_BIZ_SERVICE.TwbTalk.syncChatStatus.do")
//                            || strRequestURL.equals("/TelewebChannel_BIZ_SERVICE.TwbTalk.checkReadyTimeout.do")))
//            {
//                return true;
//            }
//
//            //채널호출일 경우에는 해더 정보에서 요청 URL을 추출한다.
//            if (strRequestURL.indexOf("/TelewebChannel") >= 0 || strRequestURL.indexOf("Servlet") >= 0)
//            {
//                strRequestURL = objRequest.getHeader("referer");
//                strRequestURL = strRequestURL.substring(strRequestURL.indexOf("webapps") - 1);
////                strRequestURL = strRequestURL.substring(0, strRequestURL.indexOf(".jsp") + 4);
//            }
//
//            int intPosi = strRequestURL.lastIndexOf("/") + 1;                                           //URL과 페이지 구분 위치
//            String strRequestPath = strRequestURL.substring(0, intPosi);                                    //요청URL
//            String strRequestPage = strRequestURL.substring(intPosi);                                   //요청페이지
//
//            //세션체크 대상 페이지 체크
//            boolean isCheck = true;
//            if ("/".equals(strRequestPath))
//            {
//                isCheck = false;
//            }
//
//            for (int i = 0; i < mstrExcludeLoginList.length; i++)
//            {
//                if (strRequestPage.equals(mstrExcludeLoginList[i]))
//                {
//                    isCheck = false;
//                    break;
//                }
//            }
//
//            if (isCheck == true)
//            {
//                for (int j = 0; j < mstrExcludeLoginList.length; j++)
//                {
//                    if (strRequestPath.indexOf(mstrExcludeLoginList[j]) >= 0)
//                    {
//                        isCheck = false;
//                        break;
//                    }
//                }
//            }
//
//            if (isCheck == true)
//            {
//                String strSessionInfo = getSessionInfo(objSession);
//                if (strSessionInfo == null || strSessionInfo.equals("")) { return false; }
//            }
//
//        }
//        catch (Exception e)
//        {
//            return false;
//        }
//        return true;
//    }

//    /**
//     * 페이지 필터를 이용하여 메뉴ID와 세션의 권한체크를 한다.
//     * @param objRequest 
//     * @param objSession 
//     * @return
//     */
//    @Override
//    public boolean isAuthMenuPage(HttpServletRequest objRequest, HttpSession objSession) throws TelewebAppException
//    {
//        try
//        {
//            String strRequestURL = objRequest.getRequestURI();                                          //요청URL + 페이지
//            String strMenuID = "";
//
//            // 웹소켓 상태 체크 url 세션 체크 제외 , sjh 20181115 
//            if (strRequestURL != null && (strRequestURL.equals("/TelewebChannel_BIZ_SERVICE.TwbTalk.syncChatStatus.do") || strRequestURL.equals("/TelewebChannel_BIZ_SERVICE.TwbTalk.checkReadyTimeout.do"))) { return true; }
//
//            //채널호출일 경우에는 해더 정보에서 요청 URL을 추출한다.
//            if (strRequestURL.indexOf("/TelewebChannel") >= 0 || strRequestURL.indexOf("Servlet") >= 0)
//            {
//                strRequestURL = objRequest.getHeader("referer");
//
//                //메뉴ID추출
//                if (strRequestURL.indexOf("MENU_ID=") >= 0)
//                {
//                    strMenuID = strRequestURL.substring(strRequestURL.indexOf("MENU_ID=") + 8);
//                    if (strMenuID.indexOf("&") >= 0)
//                    {
//                        strMenuID = strMenuID.substring(0, strMenuID.indexOf("&"));
//                    }
//                }
//
//                //페이지 URL 추출
//                strRequestURL = strRequestURL.substring(strRequestURL.indexOf("webapps") - 1);
////                strRequestURL = strRequestURL.substring(0, strRequestURL.indexOf(".jsp") + 4);
//            }
//            else
//            {
//                strMenuID = objRequest.getParameter("MENU_ID");                                         //권한 체크할 메뉴ID 추출
//            }
//
//            int intPosi = strRequestURL.lastIndexOf("/") + 1;                                           //URL과 페이지 구분 위치
//            String strRequestPath = strRequestURL.substring(0, intPosi);                                    //요청URL
//            String strRequestPage = strRequestURL.substring(intPosi);                                   //요청페이지
//
//            //메뉴권한 체크 대상 페이지 체크
//            boolean isCheck = true;
//            if ("/".equals(strRequestPath))
//            {
//                isCheck = false;
//            }
//
//            for (int i = 0; i < mstrExcludeAuthList.length; i++)
//            {
//                if (strRequestPage.equals(mstrExcludeAuthList[i]))
//                {
//                    isCheck = false;
//                    break;
//                }
//            }
//            if (isCheck == true)
//            {
//                for (int j = 0; j < mstrExcludeAuthList.length; j++)
//                {
//                    if (strRequestPath.indexOf(mstrExcludeAuthList[j]) >= 0)
//                    {
//                        isCheck = false;
//                        break;
//                    }
//                }
//            }
//
//            if (isCheck == true)
//            {
//                //메뉴ID가 없을 경우 자동 잘못된 페이지 요청으로 분기시킨다.
//                if (strMenuID == null || strMenuID.equals("") || strMenuID.equals("undefined")) { return false; }
//
//                //사용자 세션정보 Biz Class 생성
//                String strUserID = getSessionInfo(objSession, 0);
//
//                //메뉴ID, 사용자ID 화면별 권한 체크를 한다.
//                boolean blnIsAuthPage = hasMenuAuthorSession(strMenuID, strUserID);
//
//                //권한이 없을 경우 자동 잘못된 페이지 요청으로 분기시킨다.
//                if (!blnIsAuthPage) { return false; }
//            }
//        }
//        catch (Exception e)
//        {
//            return false;
//        }
//        return true;
//    }


    /**
     * 세션정보를 생성한다.
     * 
     * @param  jsonParams
     * @param  objSession 세션객체
     * @param  strUserID  사용자ID
     * @param  strLogID   로그인시 생성되는 아이디
     * @return            생성된 세션정보
     * @author            MPC R&D Team
     */
    @Override
    public String createSession(TelewebJSON jsonParams, HttpSession objSession, String strUserID, String strLogID) throws TelewebAppException
    {
        String strSessionInfo = "";
        if(!isLogin(objSession)) {
            //세션을 생성할 상세정보를 검색한다.
            TelewebJSON jsonParam01 = new TelewebJSON(jsonParams);
            jsonParam01.setString("USER_ID", strUserID);
            //세션정보에 추가적으로 정보가 필요한 상황으로서 쿼리 변경 후 권한정보, 조직정보까지 추가적으로 세팅.
            TelewebJSON jsonUserInfo = mobjDao.select("com.hcteletalk.teletalk.main.hkcdv.dao.TwbMainMapper", "selectRtnUserSessionInfo", jsonParam01);

            //세션정보문자열생성
            if(!jsonUserInfo.getHeaderBoolean("ERROR_FLAG") && jsonUserInfo.getHeaderInt("COUNT") > 0) {
                strSessionInfo = jsonUserInfo.getString("USER_ID");                         //사용자ID 0
                strSessionInfo += "|" + jsonUserInfo.getString("USER_NM");                      //사용자이름 1
                strSessionInfo += "|" + jsonUserInfo.getString("CNNCT_IP");                     //접속IP 2
                strSessionInfo += "|" + jsonUserInfo.getString("LAST_LOGIN_DT");                //마지막접속일시 3
                strSessionInfo += "|" + jsonUserInfo.getString("AUTHOR_GROUP_ID_LIST");         //권한그룹리스트 4
                strSessionInfo += "|" + jsonUserInfo.getString("DEPT_CD");                      //조직코드 5
                strSessionInfo += "|" + jsonUserInfo.getString("DEPT_NM");                      //조직명 6
                strSessionInfo += "|" + jsonUserInfo.getString("AUTH01_ORG_CD");                //본부코드(필요시사용) 7
                strSessionInfo += "|" + jsonUserInfo.getString("AUTH02_ORG_CD");                //지사코드(필요시사용) 8
                strSessionInfo += "|" + jsonUserInfo.getString("AUTH03_ORG_CD");                //센터코드(필요시사용) 9
                strSessionInfo += "|" + jsonUserInfo.getString("AUTH04_ORG_CD");                //협력업체코드(필요시사용) 10
                strSessionInfo += "|" + jsonUserInfo.getString("DEPT_NM_FULL");                 //부서전체경로명 11
                strSessionInfo += "|" + jsonUserInfo.getString("DEPT_CD_FULL");                 //부서전체경로코드 12
                strSessionInfo += "|" + jsonUserInfo.getString("SPEC_CNSL_NM");                 //부서전체경로코드 13
                strSessionInfo += "|" + jsonUserInfo.getString("MNGR_YN");                      //관리자여부 14
                strSessionInfo += "|" + jsonUserInfo.getString("USER_NICK");                    //사용자닉네임 15

                //              <!-- 20190509 ojw added for new user dept -->
                strSessionInfo += "|" + jsonUserInfo.getString("USER_ATTR_A");                  //사용자닉네임 16
                strSessionInfo += "|" + jsonUserInfo.getString("USER_ATTR_B");                  //사용자닉네임 17
                strSessionInfo += "|" + jsonUserInfo.getString("USER_ATTR_C");                  //사용자닉네임 18
                strSessionInfo += "|" + jsonUserInfo.getString("USER_ATTR_D");                  //사용자닉네임 19
            }
            //세션정보 생성
            objSession.setAttribute("TWB_SESSION_INFO", strSessionInfo);

            //파트너ID 세션 저장
            String partnerId = paletteProperties.getPartnerId();
            objSession.setAttribute("PARTNER_ID", partnerId);

            //2019.01.08 KMG 서비스버전 저장
            String serviceVersion = paletteProperties.getLicense().toString();
            objSession.setAttribute("SERVICE_VERSION", serviceVersion);
        }

        return strSessionInfo;
    }


    /**
     * 세션정보를 삭제한다.
     * 
     * @param  objSession          세션객체
     * @param  strUserID           사용자ID
     * @throws TelewebAppException
     * @author                     MPC R&D Team
     */
    @Override
    public void removeSession(HttpSession objSession) throws TelewebAppException
    {
        //세션정보 삭제
        objSession.setAttribute("TWB_SESSION_INFO", "");
        objSession.removeAttribute("TWB_SESSION_INFO");
        objSession.invalidate();
    }


    /**
     * 세션정보를 이용하여 로그인 상태여부를 판단한다.
     * 
     * @param  objSession          세션정보
     * @return                     true:로그인상태, false:로그아웃상태
     * @throws TelewebAppException
     */
    @Override
    public boolean isLogin(HttpSession objSession) throws TelewebAppException
    {
        try {
            if("".equals(getSessionInfo(objSession))) { return false; }
            return true;
        }
        catch(NullPointerException ex) {
            return false;
        }
    }


    /**
     * 세션정보를 이용하여 로그인 사용자 정보를 반환한다.
     * 
     * @param  objSession          세션정보
     * @return                     빈문자:로그아웃상태, 로그인한사용자정보(USER_ID|USER_NM|CONECT_IP|IT_PROCESSING)
     * @throws TelewebAppException
     */
    @Override
    public String getSessionInfo(HttpSession objSession) throws TelewebAppException
    {
        try {
            String strSessionInfo = (String) objSession.getAttribute("TWB_SESSION_INFO");
            if(strSessionInfo == null || strSessionInfo.equals("")) { return ""; }
            return strSessionInfo;
        }
        catch(NullPointerException ex) {
            return "";
        }
    }


    /**
     * @param  objSession
     * @param  intPosi
     * @return
     */
    @Override
    public String getSessionInfo(HttpSession objSession, int intPosi) throws TelewebAppException
    {
        return getSessionInfo(getSessionInfo(objSession), intPosi);
    }


    /**
     * @param  strSessionInfo
     * @param  intPosi
     * @return
     */
    @Override
    public String getSessionInfo(String strSessionInfo, int intPosi) throws TelewebAppException
    {
        try {
            if(strSessionInfo.equals("")) { return ""; }
            String[] objArraySessionInfo = strSessionInfo.split("\\|");
            if(objArraySessionInfo.length < intPosi) { return ""; }
            return objArraySessionInfo[intPosi];
        }
        catch(NullPointerException ex) {
            return "";
        }
    }
}
