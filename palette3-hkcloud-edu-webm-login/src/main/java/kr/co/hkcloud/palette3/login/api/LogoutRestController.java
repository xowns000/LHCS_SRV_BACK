package kr.co.hkcloud.palette3.login.api;


import javax.security.auth.login.AccountExpiredException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.login.app.LogoutService;
import kr.co.hkcloud.palette3.login.domain.LoginDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController("LogoutRestController")
@Api(value = "LogoutRestController", description = "로그아웃 REST 컨트롤러")
public class LogoutRestController
{
    private final TwbComDAO            mobjDao;
    private final InnbCreatCmmnService innbCreatCmmnService;

    private final LogoutService logoutService;

    @Value("${jwt.token.nm}")
    private String JWT_TOKEN_NM;


    /**
     * 로그아웃처리를 한다.
     * 
     * @param  Object HashMap. Key List : (HttpServletRequest)mobjRequest, (HttpServletResponse)mobjResponse, (HttpSession)mobjSession, (TelewebJSON)mjsonParams, (String)strDBMS
     * @return        TelewebJSON 형식의 조회결과 데이터
     */
    /*
     * SpringSecurity > logoutRequestMatcher
     * 
     * @ApiOperation(value = "로그아웃처리", notes = "로그아웃처리를 한다.")
     * 
     * @PostMapping("/api/TwbMain/logoutRtn") public Object logoutRtn(@TelewebJsonParam TelewebJSON mjsonParams, HttpServletRequest mobjRequest, HttpSession mobjSession) throws TelewebApiException { //client에서 전송받은 파라메터
     * 생성 // TelewebJSON mjsonParams = (TelewebJSON)inHashMap.get("mjsonParams"); // HttpServletRequest mobjRequest = (HttpServletRequest)inHashMap.get("mobjRequest"); // HttpSession mobjSession =
     * (HttpSession)inHashMap.get("mobjSession"); //반환 파라메터 생성 TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
     * 
     * // //사용자ID를 가져온다. // String strUserID = talkAuthenticationService.getSessionInfo(mobjSession, 0); // //화면 강제 종료시 이중으로 호출되는 것을 막기 위해 USER_ID가 존재할 경우만 로그를 남긴다. // if (strUserID != null && !strUserID.equals("")) // {
     * // twbUserLogBiz.insertRtn(mjsonParams, strUserID, "LOGOUT", mobjRequest.getRemoteAddr(), strUserID); // } // //사용자정보를 업데이트 한다. // objRetParams = twbUserBiz.updateRtnLogoutInfo(mjsonParams,
     * mjsonParams.getString("USER_ID")); // // //세션정보를 삭제한다. // talkAuthenticationService.removeSession(mobjSession); // // //최종 반환정보 세팅 // // //2018.11.15 kmg 이석상태에서 로그오프 시, 이석상태 기록 //
     * insertTalkReadyOffIsLogOff(mjsonParams.getString("USER_ID"));
     * 
     * if (!objRetParams.getHeaderBoolean("ERROR_FLAG")) { objRetParams.setHeader("ERROR_MSG", "정상적으로 로그아웃이 되었습니다."); }
     * 
     * //최종결과값 반환 return objRetParams; }
     */

    @ApiOperation(value = "로그아웃",
                  notes = "로그아웃 처리")
    @PostMapping("/auth-api/logout")
    //public Object logout(@Validated UserRequestDto.Logout logout, Errors errors, HttpServletRequest req) throws AccountExpiredException, TelewebAppException
    public Object logout(@TelewebJsonParam TelewebJSON mjsonParams, HttpServletRequest req) throws AccountExpiredException, TelewebAppException
    {
        LoginDTO.Logout logout = new LoginDTO.Logout();

        /* 파라메터 생성 */
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String accessToken = "";
        String jessionid = "";
        int userid;

        accessToken = mjsonParams.getString(JWT_TOKEN_NM);
        jessionid = mjsonParams.getString("JSESSIONID");
        userid = Integer.parseInt(mjsonParams.getString("user_id"));

        // validation check
        if(accessToken.isEmpty() || accessToken == null) {

            //throw new BadCredentialsException(String.valueOf(Helper.refineErrors(errors)));
            throw new BadCredentialsException("잘못된요청입니다.");
        }

        logout.setAccessToken(accessToken);
        logout.setJessionid(jessionid);
        logout.setUserid(userid);

        objRetParams = logoutService.logout(logout, req);

        return objRetParams;
    }


    /**
     * 강제 로그아웃처리를 한다. (중복로그인 방지 )
     * 
     * @param  mjsonParams
     * @param  mobjRequest
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "강제 로그아웃처리",
                  notes = "강제 로그아웃처리를 한다. (중복로그인 방지 )")
    @PostMapping("/api/TwbMain/forceLogout")
    public Object forceLogout(@TelewebJsonParam TelewebJSON mjsonParams, HttpServletRequest mobjRequest) throws TelewebApiException
    {
//        //client에서 전송받은 파라메터 생성
//        //        TelewebJSON mjsonParams         = (TelewebJSON)inHashMap.get("mjsonParams");
//        //        HttpServletRequest mobjRequest  = (HttpServletRequest)inHashMap.get("mobjRequest");

        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //사용자ID를 가져온다.
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));
        objRetParams.setHeader("ERROR_FLAG", false);
//
//        //서비스 운영모드(개발/운영)가 '운영' 상태일 시 파라메터 복호화
//        //OTP방식으로 사용자ID와 비밀번호 복호화 후 암호화 키 파기
//        if("REAL".equals(paletteProperties.getServiceMode().toString()))
//        {
//            //세션정보에 존재하는 사용자ID 및 비밀번호정보 복호화 키정보를 이용하여 암호화되어 전송된 사용자ID와 비밀번호 복호화 처리 및 세션정보 키 정보 소멸.
//            String strDecryptKey = (String) jsonParams.getString("ENCRYPT_KEY");
//            if (strDecryptKey == null || "".equals(strDecryptKey))
//            {
//                objRetParams.setHeader("ERROR_FLAG", true);
//                objRetParams.setHeader("ERROR_MSG", "암호화 키 정보가 존재하지 않습니다.");
//            }
//            else
//            {
//                //암호화되어 전송된 사용자ID와 비밀번호 복호화 처리 및 세션정보 키 정보 소멸.
//                jsonParams.setString("USER_ID", 0, AES128Cipher.decryptString(jsonParams.getString("USER_ID").replace(" ", "+"), strDecryptKey));
//                jsonParams.setString("ENCRYPT_USER_ID", 0, AES128Cipher.decryptString(jsonParams.getString("ENCRYPT_USER_ID").replace(" ", "+"), strDecryptKey));
//                jsonParams.setString("PWD", 0, AES128Cipher.decryptString(jsonParams.getString("PWD").replace(" ", "+"), strDecryptKey));
//            }
//        }
//
//        String strUserID = jsonParams.getString("USER_ID");              //사용자ID
//
//        //세션정보를 삭제한다.
//        String[] objArraySessionInfo = TeletalkHttpSesssionWebListener.getInstance().expireDuplicatedSession(strUserID, mobjRequest.getSession().getId());
//
//        // 정상세션 종료시 db 값 변경 처리함.
//        if (objArraySessionInfo != null && objArraySessionInfo.length > 0)
//        {
//            //사용자정보를 업데이트 한다.
//            objRetParams = twbUserBiz.updateRtnLogoutInfo(mjsonParams, strUserID);
//
//            //최종 반환정보 세팅
//            twbUserLogBiz.insertRtn(mjsonParams, strUserID, "LOGOUT", objArraySessionInfo[2], strUserID);
//
//            //2018.11.15 kmg 이석상태에서 로그오프 시, 이석상태 기록
//            insertTalkReadyOffIsLogOff(strUserID);
//
//            if (!objRetParams.getHeaderBoolean("ERROR_FLAG"))
//            {
//
//                objRetParams.setHeader("ERROR_MSG", "정상적으로 로그아웃이 되었습니다.");
//            }
//        }

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 채팅Off 시점 저장
     * 
     * @param  jsonParams   전송된 파라메터 데이터
     * @param  objRetParams 반환파라메터
     * @return              true:처리가능상태, false:처리불가능상태
     */
    @ApiOperation(value = "채팅Off 시점 저장",
                  notes = "채팅Off 시점 저장")
    @PostMapping("/api/TwbMain/insertTalkReadyOff")
    public Object insertTalkReadyOff(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette.main.dao.PaletteMainMapper", "selectTalkReadyOffUserId", mjsonParams);
        objRetParams.setHeader("ERROR_FLAG", false);

        mjsonParams.setString("SEQ", innbCreatCmmnService.getSeqNo("TWB_SEQ_TALK_READY_OFF"));    // idgen 수정 sjh 20190429

        // 이석상태 연속 선택 처리
        if(objRetParams.getInt("CNT") > 0) {
            if(!objRetParams.getString("USER_STATUS_CD").equals(mjsonParams.getString("USER_STATUS_CD"))) {
                //1. 채팅OFF 종료시간 업데이트
                mobjDao.update("kr.co.hkcloud.palette.main.dao.PaletteMainMapper", "updateTalkReadyOffInEndTime", mjsonParams);
                //2. 채팅OFF 히스토리 기록
                mobjDao.insert("kr.co.hkcloud.palette.main.dao.PaletteMainMapper", "insertTalkReadyOffHist", mjsonParams);
                //3. 채팅OFF 초기화
                mobjDao.delete("kr.co.hkcloud.palette.main.dao.PaletteMainMapper", "deleteTalkReadyOff", mjsonParams);
                //4. 채팅OFF 등록
                objRetParams = mobjDao.insert("kr.co.hkcloud.palette.main.dao.PaletteMainMapper", "insertTalkReadyOff", mjsonParams);
            }
        }
        else {
            //1. 채팅OFF 등록
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette.main.dao.PaletteMainMapper", "insertTalkReadyOff", mjsonParams);
        }
        //이석 상태 등록

        //최종결과값 반환
        return objRetParams;
    }

//    /**
//     * 로그아웃 시, 채팅OFf 기록
//     * 
//     * @author kmg
//     * @since 2018.11.15
//     * @param String
//     *            전송된 파라메터 데이터
//     */
//    @ApiOperation(value = "로그아웃 시, 채팅OFf 기록", notes = "로그아웃 시, 채팅OFf 기록")
//    @PostMapping("/api/TwbMain/insertTalkReadyOffIsLogOff")
//    public void insertTalkReadyOffIsLogOff(String userId) throws TelewebApiException
//    {
//        TelewebJSON jsonParams = new TelewebJSON();
//        String telIdentifier = DateFormatUtils.format((new Date()), "HH:mm:ss") + "-" + UUID.randomUUID().toString().substring(0, 8);
//        jsonParams.setHeader("TELEWEB_IDENTIFIER", telIdentifier);
//        jsonParams.setString("USER_ID", userId);
//
//        //DAO검색 메서드 호출
//        TelewebJSON objRetParams = mobjDao.select("kr.co.hkcloud.palette.main.dao.PaletteMainMapper", "selectTalkReadyOffUserId", jsonParams);
//
//        if (objRetParams.getInt("CNT") > 0)
//        {
//            //1. 채팅OFF 종료시간 업데이트
//            mobjDao.insert("kr.co.hkcloud.palette.main.dao.PaletteMainMapper", "updateTalkReadyOffInEndTime", jsonParams);
//
//            //2. 채팅OFF 히스토리 기록
//            mobjDao.insert("kr.co.hkcloud.palette.main.dao.PaletteMainMapper", "insertTalkReadyOffHist", jsonParams);
//
//            //3. 채팅OFF 초기화
//            mobjDao.delete("kr.co.hkcloud.palette.main.dao.PaletteMainMapper", "deleteTalkReadyOff", jsonParams);
//        }
//    }

}
