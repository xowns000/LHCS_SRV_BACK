package kr.co.hkcloud.palette3.signup.app;


import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.chat.setting.app.ChatSettingMessengerConnectManageService;
import kr.co.hkcloud.palette3.common.twb.app.TwbUserBizServiceImpl;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.properties.palette.PaletteProperties;
import kr.co.hkcloud.palette3.core.chat.transfer.app.TransToKakaoService;
import kr.co.hkcloud.palette3.core.security.crypto.Hash;
import kr.co.hkcloud.palette3.core.util.PaletteMailUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.setting.system.app.SettingSystemCorporateAccountManageService;
import kr.co.hkcloud.palette3.signup.domain.TtalkInitChannelEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("TalkAspSignUpService")
public class TalkSignUpServiceImpl implements TalkSignUpService
{
    private final TwbComDAO                                  mobjDao;
    private final PaletteProperties                          paletteProperties;
    private final SettingSystemCorporateAccountManageService settingSystemCorporateAccountManageService;
    private final TwbUserBizServiceImpl                      twbUserBizServiceImpl;
    private final TransToKakaoService                        transToKakaoService;
    private final ChatSettingMessengerConnectManageService   chatSettingMessengerConnectManageService;
    private final PaletteMailUtils                           paletteMailUtils;

    // @formatter:off
    private String signUpMail = "<div style=\"width:460px;margin:0 auto;font-family:'Segoe UI','맑은 고딕','Malgun Gothic','애플 SD 산돌고딕 Neo','Apple SD Gothic Neo',sans-serif;font-size:12px;color:#4d4d4d;\">" 
            + "<div style=\"margin:40px 0 60px;text-align:center;\">"
            + "<img src=\"%s/common/img/new/logo-login.png\" style=\"border-width:0;margin:0;padding:0;\"></div>"
            + "<h2 style=\"display:inline-block;width:100%%;margin:0 0 35px;padding:0;font-size:28px;font-weight:400;letter-spacing:-2px;\">회원가입 인증 안내</h2>"
            + "<h5 style=\"display:inline-block;width:100%%;margin:0;padding:0;font-size:16px;font-weight:600;letter-spacing:-1px;\">안녕하세요. "
            + "<em style=\"font-style:normal;font-size:inherit;color:#e27e00;font-weight:inherit;\">%s(%s)</em>님 텔레톡 가입을 환영합니다.</h5>"
            + "<p style=\"display:inline-block;width:100%%;margin:10px 0 0;padding:0;font-weight:400;color:#666;font-size:13px;line-height:1.5;\">아래 인증하기 버튼을 클릭하면 회원가입이 완료 됩니다.</p>"
            + "<div style=\"width:100%%;margin-top:60px;text-align:center;\">" 
            + "<a href=\"%s\" target=\"_black\"" 
            + "style=\"width:100%%;height:60px;padding:0 20px 2px;border-radius:6px;border:1px solid #527acc;background:#527acc;font-family:'Segoe UI','맑은 고딕','Malgun Gothic','애플 SD 산돌고딕 Neo','Apple SD Gothic Neo',sans-serif;font-size:20px;line-height:58px;color:#fff;text-decoration:none;cursor:pointer;overflow:hidden;outline:none;box-sizing:border-box;-webkit-appearance:button;appearance:button;\">"
            + "인증하기</a></div>"
            + "<div style=\"margin-top:20px;padding-top:10px;padding-bottom:15px;padding-right:20px;padding-left:20px;border-radius:3px;background-color:#dadee6;background-image:none;background-repeat:repeat;background-position:top left;background-attachment:scroll;box-sizing:border-box;\">"
            + "<p style=\"display:inline-block;font-size:inherit;font-weight:inherit;color:inherit;position:relative;height:16px;margin-top:5px;padding-word-break:break-all;word-spacing:-1px;letter-spacing:-0.2px;line-height:1.8\">"
            + "<em style=\"font-style:normal;font-size:inherit;font-weight:500;color:#e27e00;\">본 메일은 발신 전용 메일입니다.</em>"
            + "<br style=\"content:'';display:block;margin-top:0;margin-bottom:0;margin-right:0;margin-left:0;\">문의사항이 있으신 경우 아래 연락처로 문의 주시기 바랍니다"
            + "<br style=\"content:'';display:block;margin-top:0;margin-bottom:0;margin-right:0;margin-left:0;\">Tel : 02-6363-2224</p></div>"
            + "<div style=\"width:100%%;height:70px;margin:50px 0;color:#999;text-align:center;box-sizing:border-box;\">Copyrights Hankook Cloud Corp. All Rights Reserved.</div>";
    // @formatter:on
    /**
     * ASP고객사 사용자ID 중복 확인
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON chkDupleUserId(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "SELECT_PLT_USER_PWD", jsonParams);
    }


    /**
     * ASP고객사 이메일 중복 확인
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON chkDupleEmail(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "selectChkDupleEmail", jsonParams);
    }


    /**
     * ASP고객사 사용자 등록
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON sendMailSignUp(TelewebJSON jsonParams) throws TelewebAppException
    {
        //이메일 인증 메일 주소
        String signUpURI = paletteProperties.getAsp().getSingnuoEmailActivationUrl().toString();
        String signUpEmailURI = paletteProperties.getAsp().getSingnuoEmailActivationUrl().toString() + "/signup/eml-certi/";

        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        String emlTo = jsonParams.getString("EML");
        String subject = "[회원가입 인증 안내]";
        String emalFrom = "no-reply@hkcloud.co.kr";
        String userId = jsonParams.getString("USER_ID");
        String userNm = jsonParams.getString("USER_NM");
        String certiUri = signUpEmailURI;
        certiUri = certiUri + jsonParams.getString("CERTI_VALUE");

        String sendContents = String.format(signUpMail, signUpURI, userId, userNm, certiUri);

        try {
            paletteMailUtils.sendMimeMessage(emlTo, subject, sendContents, emalFrom, jsonParams);
        }
        catch(UnsupportedEncodingException | MessagingException e) {
            log.error(e.getLocalizedMessage());
            throw new TelewebAppException(e.getLocalizedMessage(), e);
        }

        objRetParams.setHeader("ERROR_FLAG", false); // errcode를 asp-000으로 해서 메시지는 화면에서 처리하자

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * ASP고객사 이메일 인증 재 전송
     * 
     * @param  (TelewebJSON)jsonParams, (HttpServletRequest)hsr
     * @return                          TelewebJSON 형식의 조회결과 데이터
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON sendEmailRequest(TelewebJSON jsonParams, HttpServletRequest hsr) throws TelewebAppException
    {
        //이메일 인증 메일 주소
        String signUpURI = paletteProperties.getAsp().getSingnuoEmailActivationUrl().toString();
        String signUpEmailURI = paletteProperties.getAsp().getSingnuoEmailActivationUrl().toString() + "/signup/eml-certi/";

        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        //DB 리턴 파라메터 생성
        TelewebJSON dbParams = new TelewebJSON(jsonParams);

        dbParams = mobjDao.select("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "selectRequestCertiId", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG") || dbParams.getHeaderInt("TOT_COUNT") < 1) {
            objRetParams.setString("TALK_MSG", "사용자 아이디/비밀번호와 일치하는 인증 정보가  없습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        //패스워드를 복호화.
        HttpSession mobjSession = hsr.getSession();
        String encVal = jsonParams.getString("PWD").replace(" ", "+");
        String keyVal = (String) mobjSession.getAttribute("ENCRYPT_KEY");
        String shaPwd = twbUserBizServiceImpl.pwdDecString(encVal, keyVal);
        mobjSession.setAttribute("ENCRYPT_KEY", "");

        String encryptKey = dbParams.getString("ENCRYPT_KEY");
        String decryptKey = twbUserBizServiceImpl.keyDecString(encryptKey);
        String lastPwd = twbUserBizServiceImpl.pwdEncString(shaPwd, decryptKey);
        jsonParams.setString("PWD", lastPwd);

        dbParams = mobjDao.select("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "selectRequestCerti", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG") || dbParams.getHeaderInt("TOT_COUNT") < 1) {
            objRetParams.setString("TALK_MSG", "사용자 아이디/비밀번호와 일치하는 인증 정보가  없습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        if("Y".equals(dbParams.getString("CERTI_YN"))) {
            objRetParams.setString("TALK_MSG", "사용자 아이디/비밀번호와 일치하는 인증 정보가 이미  존재합니다.");
            return objRetParams;
        }

        //메일 인증 재 전송
        String emlTo = dbParams.getString("EML_ADDR");
        String subject = "[회원가입 인증 안내]";
        String emalFrom = "no-reply@hkcloud.co.kr";
        String userId = dbParams.getString("USER_ID");
        String userNm = dbParams.getString("USER_NM");
        String certiUri = signUpEmailURI;
        certiUri = certiUri + dbParams.getString("CERTI_VALUE");

        String sendContents = String.format(signUpMail, signUpURI, userId, userNm, certiUri);

        try {
            paletteMailUtils.sendMimeMessage(emlTo, subject, sendContents, emalFrom, jsonParams);
        }
        catch(UnsupportedEncodingException | MessagingException e) {
            log.error(e.getLocalizedMessage());
            throw new TelewebAppException(e.getLocalizedMessage());
        }

        objRetParams.setString("TALK_MSG", "등록하신 계정(" + emlTo + ")으로 인증 정보가 발송되었습니다.");

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * ASP고객사 신규 등록
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON insertRtnSignUp(TelewebJSON jsonParams) throws TelewebAppException
    {

        Date today = new Date();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
        String srvcBgngDt = format1.format(today) + "0000";

        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        int intAspTrialDuration = paletteProperties.getAsp().getTrialDuration().toDays() > 0 ? Long.valueOf(paletteProperties.getAsp().getTrialDuration().toDays()).intValue() : 6;
        cal.add(Calendar.DATE, intAspTrialDuration);
        String srvcEndDt = format1.format(cal.getTime()) + "2359";

        jsonParams.setString("CO_NM", jsonParams.getString("CO_NM"));
        jsonParams.setString("PHN_NO", "00000000000");
        jsonParams.setString("EML", jsonParams.getString("EML"));
        jsonParams.setString("HOMEPAGE_ADDR", "https://");
        jsonParams.setString("PRODUCT_CD", "TRIAL");
        jsonParams.setString("SERV_CD", "trial");
        jsonParams.setString("SRVC_BGNG_DT", srvcBgngDt);
        jsonParams.setString("SRVC_END_DT", srvcEndDt);
        jsonParams.setString("SRVC_MAINT_YN", "Y");
        jsonParams.setString("BZMN_COR", "");
        jsonParams.setString("REGR_DEPT_CD", "X");
        jsonParams.setString("REGR_ID", "teleweb");
        jsonParams.setString("AMDR_DEPT_CD", "X");
        jsonParams.setString("AMDR_ID", "teleweb");
        jsonParams.setString("PROC_ID", "teleweb");
        jsonParams.setString("CUST_AGREE_YN", "Y");

        return settingSystemCorporateAccountManageService.insertRtnAspCust(jsonParams);
    }


    /**
     * ASP고객사 사용자 등록
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON insertRtnTwbBas01(TelewebJSON jsonParams) throws TelewebAppException
    {
        jsonParams.setString("PROC_ID", "teleweb");
        jsonParams.setString("EML_ADDR", jsonParams.getString("EML"));
        //signUp에서 등록 시 비밀번호가 초기화 되지 않도록 한다.
        jsonParams.setString("NO_ENC_PWD", "Y");
        //signUp에서 처리된 것을 명시한다.
        jsonParams.setString("SIGN_UP", "Y");

        return twbUserBizServiceImpl.insertRtn(jsonParams);
    }


    /**
     * 메일 사용자 정보 조회
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON processSignUpUserNmId(TelewebJSON jsonParams) throws TelewebAppException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "");

        //DB 리턴 파라메터 생성
        TelewebJSON dbParams = new TelewebJSON(jsonParams);

        //인증 대상 데이터 조회
        dbParams = mobjDao.select("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "selectSignUpUserNmId", jsonParams);

        if(dbParams.getHeaderInt("TOT_COUNT") < 1) {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "인증 대상 데이터가 존재하지 않습니다.");
            objRetParams.setString("USER_NM", "인증 실패하였습니다");
            objRetParams.setString("USER_ID", "미인증");
            objRetParams.setString("CERTI_MSG", "인증 완료 후 텔레톡을 시작할 수 있습니다.");
            objRetParams.setString("TALK_MSG", "");
            return objRetParams;
        }
        else {
            objRetParams.setString("USER_NM", dbParams.getString("USER_NM"));
            objRetParams.setString("USER_ID", dbParams.getString("USER_ID"));
            objRetParams.setString("CERTI_MSG", "가입이  완료되었습니다.");
            objRetParams.setString("TALK_MSG", "텔레톡을 바로 시작할 수 있습니다.");
        }

        String custcoId = dbParams.getString("CUSTCO_ID");
        String userId = dbParams.getString("USER_ID");
        String coNm = dbParams.getString("CO_NM");
        jsonParams.setString("CUSTCO_ID", custcoId);
        jsonParams.setString("USER_ID", userId);
        jsonParams.setString("CO_NM", coNm);

        //초기 데이터 적재
        //INIT_ASP_TWB_PROHIBITE_WORD, 금칙어설정
        //INIT_ASP_TWB_TALK_ENV, 텔레톡 설정정보
        //INIT_ASP_TWB_TALK_SYSTEM_MSG, 시스템메시지 관리
        //INIT_ASP_TWB_TALK_SYSTEM_MSG_LINKS, 시스템메시지링크
        //INIT_ASP_TWB_TALK_WORK_TIME_HIST, 업무시간내역
        //INIT_ASP_TWB_USER_CHAT_AGENT, 상담사채팅허용설정 (사용자 등록 시 입력되고 데이터 초기화 시 디폴트 값으로 업데이트 수행한다.
        //INIT_ASP_USER_BELONG_ITEM, 사용자소속항목
        //INIT_ASP_TWB_TALK_CNSL_TYP 상담유형
        //INIT_ASP_TWB_TALK_INQRY_TYP 문의유형
        //사용자 소속 항목 등록 : 권한 처리 정리 시 까지 시스템 관리자 그룹으로 등록한다.
        //인증 정보 초기화 : 등록된 이메일 인증 정보를 초기화 한다 

        //deleteInitProhibiteWord
        dbParams = mobjDao.delete("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "deleteInitProhibiteWord", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setString("CERTI_MSG", "");
            objRetParams.setString("TALK_MSG", "인증 정보 처리가 실패하였습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        //deleteIniTalkEnv
        dbParams = mobjDao.delete("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "deleteIniTalkEnv", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setString("CERTI_MSG", "");
            objRetParams.setString("TALK_MSG", "인증 정보 처리가 실패하였습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        //deleteInitTalkSystemMsg
        dbParams = mobjDao.delete("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "deleteInitTalkSystemMsg", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setString("CERTI_MSG", "");
            objRetParams.setString("TALK_MSG", "인증 정보 처리가 실패하였습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        //deleteInitTalkSystemMsgLinks
        dbParams = mobjDao.delete("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "deleteInitTalkSystemMsgLinks", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setString("CERTI_MSG", "");
            objRetParams.setString("TALK_MSG", "인증 정보 처리가 실패하였습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        //deleteInitTalkWorkTimeHist
        dbParams = mobjDao.delete("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "deleteInitTalkWorkTimeHist", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setString("CERTI_MSG", "");
            objRetParams.setString("TALK_MSG", "인증 정보 처리가 실패하였습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        //deleteInitUserBelongItem
        dbParams = mobjDao.delete("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "deleteInitUserBelongItem", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setString("CERTI_MSG", "");
            objRetParams.setString("TALK_MSG", "인증 정보 처리가 실패하였습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        //deleteInitTalkCnslTyp
        dbParams = mobjDao.delete("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "deleteInitTalkCnslTyp", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setString("CERTI_MSG", "");
            objRetParams.setString("TALK_MSG", "인증 정보 처리가 실패하였습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        // deleteInitCustChannel
        dbParams = mobjDao.delete("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "deleteInitCustChannel", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setString("CERTI_MSG", "");
            objRetParams.setString("TALK_MSG", "인증 정보 처리가 실패하였습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        //deleteInitTalkInqryTyp
        dbParams = mobjDao.delete("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "deleteInitTalkInqryTyp", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setString("CERTI_MSG", "");
            objRetParams.setString("TALK_MSG", "인증 정보 처리가 실패하였습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        //insertInitProhibiteWord
        dbParams = mobjDao.insert("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "insertInitProhibiteWord", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setString("CERTI_MSG", "");
            objRetParams.setString("TALK_MSG", "인증 정보 처리가 실패하였습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        //insertIniTalkEnv
        dbParams = mobjDao.insert("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "insertIniTalkEnv", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setString("CERTI_MSG", "");
            objRetParams.setString("TALK_MSG", "인증 정보 처리가 실패하였습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        //insertInitTalkSystemMsg
        dbParams = mobjDao.insert("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "insertInitTalkSystemMsg", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setString("CERTI_MSG", "");
            objRetParams.setString("TALK_MSG", "인증 정보 처리가 실패하였습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        //insertInitTalkSystemMsgLinks
        dbParams = mobjDao.insert("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "insertInitTalkSystemMsgLinks", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setString("CERTI_MSG", "");
            objRetParams.setString("TALK_MSG", "인증 정보 처리가 실패하였습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        //insertInitTalkWorkTimeHist
        dbParams = mobjDao.insert("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "insertInitTalkWorkTimeHist", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setString("CERTI_MSG", "");
            objRetParams.setString("TALK_MSG", "인증 정보 처리가 실패하였습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        //updateInitUserChatAgent
        dbParams = mobjDao.update("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "updateInitUserChatAgent", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setString("CERTI_MSG", "");
            objRetParams.setString("TALK_MSG", "인증 정보 처리가 실패하였습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        //insertInitUserBelongItem
        dbParams = mobjDao.insert("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "insertInitUserBelongItem", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setString("CERTI_MSG", "");
            objRetParams.setString("TALK_MSG", "인증 정보 처리가 실패하였습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        //insertInitTalkCnslTyp
        dbParams = mobjDao.insert("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "insertInitTalkCnslTyp", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setString("CERTI_MSG", "");
            objRetParams.setString("TALK_MSG", "인증 정보 처리가 실패하였습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        //insertInitTalkInqryTyp
        dbParams = mobjDao.insert("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "insertInitTalkInqryTyp", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setString("CERTI_MSG", "");
            objRetParams.setString("TALK_MSG", "인증 정보 처리가 실패하였습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        //사용자 소속 항목 삭제
        dbParams = mobjDao.delete("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "deleteInitTwbBas07", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setString("CERTI_MSG", "");
            objRetParams.setString("TALK_MSG", "인증 정보 처리가 실패하였습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }
        //사용자 소속 항목 등록
        dbParams = mobjDao.insert("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "insertInitTwbBas07", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setString("CERTI_MSG", "");
            objRetParams.setString("TALK_MSG", "인증 정보 처리가 실패하였습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        //인증 정보 초기화
        dbParams = mobjDao.update("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "updatEmailSignUp", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG")) {
            objRetParams.setString("CERTI_MSG", "");
            objRetParams.setString("TALK_MSG", "인증 정보 처리가 실패하였습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        return objRetParams;
    }


    /**
     * 사용자 ID 찾기
     * 
     * @param  (TelewebJSON) jsonParams, (HttpServletRequest) hsr
     * @return               TelewebJSON 형식의 조회결과 데이터
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON selectFindUserId(TelewebJSON jsonParams, HttpServletRequest hsr) throws TelewebAppException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "");

        //DB 리턴 파라메터 생성
        TelewebJSON dbParams = new TelewebJSON(jsonParams);

        dbParams = mobjDao.select("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "selectFindUserIdEnc", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG") || dbParams.getHeaderInt("TOT_COUNT") < 1) {
            objRetParams.setString("TALK_MSG", "이메일이 존재하지 않습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        //패스워드를 복호화.
        HttpSession mobjSession = hsr.getSession();
        String encVal = jsonParams.getString("PWD").replace(" ", "+");
        String keyVal = (String) mobjSession.getAttribute("ENCRYPT_KEY");
        String shaPwd = twbUserBizServiceImpl.pwdDecString(encVal, keyVal);
        mobjSession.setAttribute("ENCRYPT_KEY", "");

        String encryptKey = dbParams.getString("ENCRYPT_KEY");
        String decryptKey = twbUserBizServiceImpl.keyDecString(encryptKey);
        String lastPwd = twbUserBizServiceImpl.pwdEncString(shaPwd, decryptKey);
        jsonParams.setString("PWD", lastPwd);

        dbParams = mobjDao.select("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "selectFindUserId", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG") || dbParams.getHeaderInt("TOT_COUNT") < 1) {
            objRetParams.setString("TALK_MSG", "사용자ID가 존재하지 않습니다. 관리자에게 문의하여 주십시요.");
        }
        else {
            objRetParams.setString("TALK_MSG", "요청하신 사용자 아이디는  " + dbParams.getString("USER_ID") + " 입니다");
        }

        return objRetParams;
    }


    /**
     * 사용자 패스워드 찾기
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON selectFindPwd(TelewebJSON jsonParams) throws TelewebAppException
    {

        String signUpURI = paletteProperties.getAsp().getSingnuoEmailActivationUrl().toString();
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "");

        //DB 리턴 파라메터 생성
        TelewebJSON dbParams = new TelewebJSON(jsonParams);

        dbParams = mobjDao.select("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "selectFindPwd", jsonParams);
        if(dbParams.getHeaderBoolean("ERROR_FLAG") || dbParams.getHeaderInt("TOT_COUNT") < 1) {
            objRetParams.setString("TALK_MSG", "사용자ID와 이메일 주소가  존재하지 않습니다. 관리자에게 문의하여 주십시요.");
            return objRetParams;
        }

        String emlAddr = dbParams.getString("EML_ADDR");

        //임시 비밀 번호 발급
        String imsiPwd = UUID.randomUUID().toString().replace("-", "").substring(0, 8);
        String shaPwd;
        try {
            shaPwd = Hash.encryptSHA256(imsiPwd, StandardCharsets.UTF_8);
        }
        catch(NoSuchAlgorithmException e) {
            throw new TelewebAppException(e.getLocalizedMessage(), e);
        }
        String encryptKey = dbParams.getString("ENCRYPT_KEY");
        String decryptKey = twbUserBizServiceImpl.keyDecString(encryptKey);
        String pwd = twbUserBizServiceImpl.pwdEncString(shaPwd, decryptKey);
        //임시 비밀번호 업데이트

        jsonParams.setString("PWD", pwd);

        dbParams = mobjDao.select("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "updateInitPwd", jsonParams);

        String subject = "[임시 비밀 번호 발급]";
        String emalFrom = "no-reply@hkcloud.co.kr";
        String imsiPwdMail = "<div style=\"width:460px;margin:0 auto;font-family:'Segoe UI','맑은 고딕','Malgun Gothic','애플 SD 산돌고딕 Neo','Apple SD Gothic Neo',sans-serif;font-size:12px;color:#4d4d4d;\">" + "<div style=\"margin:40px 0 60px;text-align:center;\">" + "<img src=\"%s/common/img/new/logo-login.png\" style=\"border-width:0;margin:0;padding:0;\"></div>" + "<h2 style=\"display:inline-block;width:100%%;margin:0 0 35px;padding:0;font-size:28px;font-weight:400;letter-spacing:-2px;\">임시 비밀번호 발급</h2>" + "<h5 style=\"display:inline-block;width:100%%;margin:0;padding:0;font-size:16px;font-weight:600;letter-spacing:-1px;\">안녕하세요. " + "<br>임시 비밀번호는 " + "<em style=\"font-style:normal;color:#5697d9;font-size:20px;font-weight:400;margin:0 4px;\">(%s)</em>입니다.</h5>" + "<div style=\"margin-top:80px;padding-top:10px;padding-bottom:15px;padding-right:20px;padding-left:20px;border-radius:3px;background-color:#dadee6;background-image:none;background-repeat:repeat;background-position:top left;background-attachment:scroll;box-sizing:border-box;\">" + "<p style=\"display:inline-block;font-size:inherit;font-weight:inherit;color:inherit;position:relative;height:16px;margin-top:5px;padding-word-break:break-all;word-spacing:-1px;letter-spacing:-0.2px;line-height:1.8\">" + "<em style=\"font-style:normal;font-size:inherit;font-weight:500;color:#e27e00;\">본 메일은 발신 전용 메일입니다.</em>" + "<br style=\"content:'';display:block;margin-top:0;margin-bottom:0;margin-right:0;margin-left:0;\">문의사항이 있으신 경우 아래 연락처로 문의 주시기 바랍니다" + "<br style=\"content:'';display:block;margin-top:0;margin-bottom:0;margin-right:0;margin-left:0;\">Tel : 02-6363-2224</p></div>" + "<div style=\"width:100%%;height:70px;margin:50px 0;color:#999;text-align:center;box-sizing:border-box;\">Copyrights Hankook Cloud Corp. All Rights Reserved.</div>";
        String sendContents = String.format(imsiPwdMail, signUpURI, imsiPwd);

        try {
            paletteMailUtils.sendMimeMessage(emlAddr, subject, sendContents, emalFrom, jsonParams);
        }
        catch(UnsupportedEncodingException | MessagingException e) {
            throw new TelewebAppException(e.getLocalizedMessage(), e);
        }
        objRetParams.setString("TALK_MSG", emlAddr + " 계정으로 임시 비밀 번호가 발송되었습니다.");

        return objRetParams;

    }


    /**
     * Trial 기간 확인
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON selectSignUpTrial(TelewebJSON jsonParams) throws TelewebAppException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "");

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "selectSignUpTrial", jsonParams);

        return objRetParams;
    }


    /**
     * 서비스 유지 여부 확인
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON selectSignUpServiceKeep(TelewebJSON jsonParams) throws TelewebAppException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams.setHeader("ERROR_FLAG", false);
        objRetParams.setHeader("ERROR_MSG", "");

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.signup.dao.TalkSignUpMapper", "selectSignUpServiceKeep", jsonParams);

        return objRetParams;
    }


    /**
     * ttalk 채널연동 ( 계정생성에 영향이 없도록 티톡서버가 죽어도..)
     */
    @Override
    @EventListener
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public void initTtalkChannel(final TtalkInitChannelEvent ttalkInitChannelEvent) throws TelewebAppException
    {
        TelewebJSON jsonParams = ttalkInitChannelEvent.getJsonParams();

        // 티톡 자동 채널 생성 
        jsonParams.setString("CHN_CLSF_CD", "TTALK");
        jsonParams.setString("SRVC_MAINT_YN", "Y");
        jsonParams.setString("DSPTCH_PRF_NM", jsonParams.getString("CO_NM"));
        jsonParams.setString("REGR_DEPT_CD", "x");
        jsonParams.setString("REGR_ID", "teleweb");
        jsonParams.setString("AMDR_DEPT_CD", "x");
        jsonParams.setString("AMDR_ID", "teleweb");
        jsonParams.setString("PROC_ID", "teleweb");

        JSONObject sendKakao = new JSONObject();
        sendKakao.put("custco_id", jsonParams.getString("CUSTCO_ID"));
        sendKakao.put("profile_name", jsonParams.getString("DSPTCH_PRF_NM"));
        sendKakao.put("profile_status", jsonParams.getString("SRVC_MAINT_YN"));
        sendKakao.put("CHT_CUTT_DTL_ID", "99999999999");
        sendKakao.put("sndrKey", "");    // 티톡에서 생성
        sendKakao.put("uuid", "");          // 티톡에서 생성  

        JSONObject ret = transToKakaoService.transToKakao("profile", sendKakao, jsonParams.getString("CHN_CLSF_CD")); //이미지 처리

        if(ret != null && ret.getString("code").equals("0")) {
            jsonParams.setString("DSPTCH_PRF_KEY", ret.getString("sndrKey"));
            jsonParams.setString("UUID", ret.getString("uuid"));

            chatSettingMessengerConnectManageService.insertRtnAspCustChannel(jsonParams);
        }
    }

}
