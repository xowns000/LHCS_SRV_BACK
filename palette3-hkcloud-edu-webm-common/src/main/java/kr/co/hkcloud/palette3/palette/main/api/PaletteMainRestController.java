package kr.co.hkcloud.palette3.palette.main.api;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.raonwhitehat.cert.cx.CertResult;
import com.raonwhitehat.cert.cx.CertSecuManager;
import com.raonwhitehat.cert.cx.CertToken;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.palette.app.PaletteCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.core.support.PaletteUserContextSupport;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.infra.alimtalk.app.InfraAlimtalkSndngService;
import kr.co.hkcloud.palette3.infra.email.app.InfraEmailSndngService;
import kr.co.hkcloud.palette3.palette.main.app.PaletteMainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;

@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "MainRestController", description = "메인 REST 컨트롤러")
public class PaletteMainRestController {

    private final InnbCreatCmmnService innbCreatCmmnService;
    private final PaletteMainService paletteMainService;
    private final PaletteCmmnService paletteCmmnService;
    private final InfraAlimtalkSndngService infraAlimtalkSndngService;
    private final InfraEmailSndngService infraEmailSndngService;

    /**
     * @param mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @NoBizLog
    @ApiOperation(value = "메인-스크립트", notes = "메인 우측메뉴 채팅스크립트 상세조회한다")
    @PostMapping("/api/palette-main/script/inqire/chatdetail")
    public Object selectRtnChatScriptDetail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return paletteMainService.selectRtnChatScriptDetail(mjsonParams);
    }

    /**
     * @param mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @NoBizLog
    @ApiOperation(value = "메인-스크립트", notes = "메인 우측메뉴 채팅스크립트 조회한다")
    @PostMapping("/api/palette-main/script/inqire/chatlist")
    public Object selectRtnChatScript(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return paletteMainService.selectRtnChatScript(mjsonParams);
    }

    /**
     * @param mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @NoBizLog
    @ApiOperation(value = "메인-스크립트", notes = "메인 우측메뉴 스크립트 상세 조회한다")
    @PostMapping("/api/palette-main/script/inqire/detail")
    public Object selectRtnScriptDetail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return paletteMainService.selectRtnScriptDetail(mjsonParams);
    }

    /**
     * @param mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @NoBizLog
    @ApiOperation(value = "메인-스크립트", notes = "메인 우측메뉴 스크립트 조회한다")
    @PostMapping("/api/palette-main/script/inqire/list")
    public Object selectRtnScript(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return paletteMainService.selectRtnScript(mjsonParams);
    }

    /**
     * @param mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @NoBizLog
    @ApiOperation(value = "메인-메뉴목록", notes = "메인 메뉴목록을 조회한다")
    @PostMapping("/api/palette-main/menu/list")
    public Object selectRtnMenu(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return paletteMainService.selectRtnMenu(mjsonParams);
    }

    /**
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @NoBizLog
    @ApiOperation(value = "메인-권한메뉴목록", notes = "메인 권한에 따른 전체 메뉴목록을 조회한다")
    @PostMapping("/api/palette-main/authority-menu/list")
    public Object getAllMenuListWithAuth(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return paletteMainService.getAllMenuListWithAuth(mjsonParams);
    }

    /**
     * @author lyj
     * @since 2019.12.08
     */
    @NoBizLog
    @ApiOperation(value = "메인-실시간공지목록", notes = "메인 실시간공지목록을 조회한다")
    @PostMapping("/api/palette-main/rltm-notice/list")
    public Object getRtNotice(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return paletteMainService.getRtNotice(mjsonParams);
    }

    /**
     * @author lyj
     * @since 2019.12.08
     */
    @ApiOperation(value = "메인-실시간공지팝업조회", notes = "메인 실시간공지 팝업을 조회한다")
    @PostMapping("/api/palette-main/rltm-notice-popup-inqire")
    public Object getPopRtNotice(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return paletteMainService.getPopRtNotice(mjsonParams);
    }

    /**
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "메인-메뉴정보조회", notes = "메인 메뉴정보를 조회한다")
    @PostMapping("/api/palette-main/menu-info/inqire")
    public Object selectGetMenuBaseInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return paletteMainService.selectGetMenuBaseInfo(mjsonParams);
    }

    /**
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "메인-알림톡템플릿조회", notes = "메인 알림톡템플릿을 조회한다")
    @PostMapping("/api/palette-main/alimtalk/tmplat/inqire")
    public Object selectAlimtalkTmplat(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {

        return paletteMainService.selectAlimtalkTmplat(mjsonParams);
    }

    /**
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "메인-알림톡 전송", notes = "메인 알림톡을 전송한다")
    @PostMapping("/api/palette-main/alimtalk/trnsmis")
    public TelewebJSON trnsmisAlimtalk(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        TelewebJSON objRetParams2 = new TelewebJSON();
        JSONObject returnData = new JSONObject();
        String seq = innbCreatCmmnService.getSeqNo("NTCN_TALK_SEND_HIST");
        String custcoId = PaletteUserContextSupport.getCurrentUser().getCustcoId();
        String userId = (PaletteUserContextSupport.getCurrentUser() == null ? "test01" : PaletteUserContextSupport.getCurrentUser()
            .getName());
        String cstmrId = "";
        objRetParams2 = paletteMainService.selectCstmrId(mjsonParams);
        if (objRetParams2.getHeaderInt("TOT_COUNT") != 0) {
            cstmrId = objRetParams2.getString("CUSTOMER_ID");
        }

        returnData = infraAlimtalkSndngService.trnsmisAlimtalk(mjsonParams); //알림톡 전송 서비스 호출
        mjsonParams.setString("SEQ", seq);
        if ("0000".equals(returnData.getString("code"))) {
            mjsonParams.setString("NTCN_TALK_PROC_RSLT", "0001"); //이메일 서비스 정상처리(as-is 0001 정상처리값)
            objRetParams.setHeader("ERROR_FLAG", false);
        } else {
            mjsonParams.setString("NTCN_TALK_PROC_RSLT", "Fail"); //명시적 fail 처리(임시)
            objRetParams.setHeader("ERROR_FLAG", true);
        }
        paletteMainService.insertAlimtalkHist(mjsonParams); //전송이력 저장 
        objRetParams.setString("code", returnData.getString("code"));
        objRetParams.setString("received_at", returnData.getString("received_at"));
        objRetParams.setString("message", returnData.getString("message"));

        //상담통합이력테이블 저장	
        TelewebJSON cnslUnityJsonParams = new TelewebJSON();
        cnslUnityJsonParams.setString("UNITY_HST_ID", innbCreatCmmnService.getSeqNo("PLT_CNSL_UNITY_HST", "PLT"));
        cnslUnityJsonParams.setString("CUSTCO_ID", custcoId);
        cnslUnityJsonParams.setString("JOBBY_CNSL_HST_ID", seq);
        cnslUnityJsonParams.setString("CNSL_DIV", mjsonParams.getString("CNSL_DIV"));
        cnslUnityJsonParams.setString("CSTMR_TELNO", mjsonParams.getString("CUST_TEL_NO"));
        cnslUnityJsonParams.setString("CSTMR_ID", cstmrId);
        cnslUnityJsonParams.setString("REGR_ID", userId);
        paletteCmmnService.insertRtnCnslUnityHst(cnslUnityJsonParams);

        return objRetParams;
    }

    /**
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "메인-SMS템플릿조회", notes = "메인 SMS템플릿을 조회한다")
    @PostMapping("/api/palette-main/sms/tmplat/inqire")
    public Object selectSmsTmplat(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return paletteMainService.selectSmsTmplat(mjsonParams);
    }

    /**
     * @param mjsonParams
     * @return TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @NoBizLog
    @ApiOperation(value = "사용자 권한 조회", notes = "사용자 권한을 조회한다")
    @PostMapping("/api/palette-main/auth/inqire")
    public Object selectAuth(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return paletteMainService.selectAuth(mjsonParams);
    }

    /**
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "메인-상담이력 조회", notes = "메인 상담이력 개수 조회")
    @PostMapping("/api/palette-main/cnsl-count/inqire")
    public Object selectCountCNSL(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        return paletteMainService.selectCountCNSL(mjsonParams);
    }

    /**
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "사용자 권한 조회", notes = "사용자 권한을조회한다")
    @PostMapping("/api/palette-main/myAuth/inqire")
    public Object getUserAuth(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams.setString("USER_ID", mjsonParams.getString("USER_ID"));
        return paletteMainService.getUserAuth(objRetParams);
    }

    /**
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "라온 암호화서비스 사이트키 발급 api", notes = "라온 암호화서비스 사이트키 발급 api")
    @PostMapping("/api/palette-main/getOmnioneKey")
    public Object getOmnioneKey(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        String cpCode = "CX1000000006";
        String sitekey = "l39fTGMFWoLL3F+Gp3i6LdMH8jMkqlCFMyIwrxt/m1UWoddpL8OR3GeCvRSnF6z3Gijc4yoN2vYIfGnafASpF2TXLdiy4H4JG74zgNvhTMQJsZ4bsHRP95s2Hruvxkk73YB9FMVqq8SPSox6YeZkaVxiCZCwmI2pQ20HIFam3S8=";
        String callbackUrl = "api/palette-main/setOmnioneKey";

        /** CertToken 발급 시작 */
        CertSecuManager csm = new CertSecuManager(sitekey);
        CertToken ct = csm.reqCert(cpCode, callbackUrl);
        if ("C001".equals(ct.getStatus())) {
            ;
            objRetParams.setString("resultCode", "200");
            objRetParams.setString("authToken", ct.getAuthToken());
            objRetParams.setString("certInfo", ct.getCertInfo());
            objRetParams.setString("callbackUrl", callbackUrl);
        }
        // CertToken 발급 실패
        else {
            // CertToken 발급에 실패할 경우 회원사 메뉴얼에 상태 코드를 참조하여 주십시오.
            objRetParams.setString("resultCode", ct.getStatus());
            objRetParams.setString("resultMsg", ct.getMessage());
        }
        return objRetParams;
    }

    /**
     * 간편인증 서비스
     *
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "라온 간편인증 전 고객업데이트", notes = "라온 간편인증 전 고객업데이트")
    @PostMapping("/api/palette-main/setOmniCust")
    public Object setOmnioneCust(@TelewebJsonParam TelewebJSON mjsonParams) throws Exception {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /* 간편인증 고객저장 */
        objRetParams = paletteMainService.setBFOmniAuthUser(mjsonParams);

        return objRetParams;
    }

    /**
     * 간편인증 서비스
     *
     * @param mjsonParams
     * @return
     */
    @ApiOperation(value = "라온 간편인증 환료 후 고객 업데이트", notes = "라온 간편인증 환료 후 고객 업데이트")
    @GetMapping("/api/palette-main/setOmnioneKey")
    public String setOmnioneKey(HttpServletRequest request, HttpServletResponse response) throws Exception {
        TelewebJSON objRetParams = new TelewebJSON();

        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");

        String custKey = "";
        String senderKey = "";

        Enumeration<String> paramNames = request.getParameterNames();
        while (paramNames.hasMoreElements()) {
            String paramName = paramNames.nextElement();
            String paramValue = request.getParameter(paramName);
            log.debug("paramName : " + paramName + "paramValue : " + paramValue);
        }

        String cpCode = "CX1000000006";
        String sitekey = "l39fTGMFWoLL3F+Gp3i6LdMH8jMkqlCFMyIwrxt/m1UWoddpL8OR3GeCvRSnF6z3Gijc4yoN2vYIfGnafASpF2TXLdiy4H4JG74zgNvhTMQJsZ4bsHRP95s2Hruvxkk73YB9FMVqq8SPSox6YeZkaVxiCZCwmI2pQ20HIFam3S8=";

        String resData = (String) request.getParameter("resData");
        String token = (String) request.getParameter("token");
        String authToken = (String) request.getParameter("authToken");
        String name = "";               // 성명
        String birth = "";                // 생년월일
        String nation = "";                // 내외국인 구분값
        String ci = "";                // CI
        String di = "";                // DI
        String ci2 = "";                // CI2
        String ciUpdCnt = "";                // CI 업데이트 횟수
        String txSn = "";               // 거래일련번호
        String retCd = "";            // 결과코드
        String cpParam = "";                // 가맹점 거래번호

        //MDL 부가정보
        String address, asort, dlno, ihidnum, inspectbegend, issude, lcnscndcdnm, locpanm, passwordsn;
        address = asort = dlno = ihidnum = inspectbegend = issude = lcnscndcdnm = locpanm = passwordsn = "";
        Object test = new Object();

        CertSecuManager csm = new CertSecuManager(sitekey);

        CertResult result = csm.resCert(cpCode, authToken, token);

        log.debug("result" + result);
        String resultMsg = "";

        if ("C000".equals(result.getRsltCd())) {
            test = result;
            // 복호화된 데이터 변수에 저장
            name = result.getName();
            birth = result.getBirth();
            ci = result.getCi();
            di = result.getDi();
            ci2 = result.getCi2();
            ciUpdCnt = result.getCiUpdCnt();

            //name 		=	result.get("name");
            //ci 			=	result.get("ci");
            //birth 		=	result.get("birth");
            address = result.get("address");//면허증 주소
            asort = result.get("asort"); // 면허증 종류
            dlno = result.get("dlno"); // 면허번호
            ihidnum = result.get("ihidnum"); //주민벉호 (미제출)
            inspectbegend = result.get("inspctbegend"); // 적성검사기간
            issude = result.get("issude"); // 발행일
            lcnscndcdnm = result.get("lcnscndcdnm"); //면허조건명
            locpanm = result.get("locpanm"); //발행 지방경찰청명의
            passwordsn = result.get("passwordsn"); // 암호일련번호

            objRetParams.setString("name", name);
            objRetParams.setString("birth", birth);
            objRetParams.setString("ci", ci);
            objRetParams.setString("di", di);
            objRetParams.setString("ci2", ci2);
            objRetParams.setString("ciUpdCnt", ciUpdCnt);

            objRetParams.setString("address", address);
            objRetParams.setString("asort", asort);
            objRetParams.setString("dlno", dlno);
            objRetParams.setString("ihidnum", ihidnum);
            objRetParams.setString("inspectbegend", inspectbegend);
            objRetParams.setString("issude", issude);
            objRetParams.setString("lcnscndcdnm", lcnscndcdnm);
            objRetParams.setString("locpanm", locpanm);
            objRetParams.setString("passwordsn", passwordsn);

            /* 간편인증 고객저장 */
            paletteMainService.setAFOmniAuthUser(objRetParams);

            resultMsg = "간편인증이 완료되었습니다.";
        } else {
            objRetParams.setString("resultCode", result.getRsltCd());
            objRetParams.setString("resultMsg", result.getMessage());

            resultMsg = "간편인증 오류\n오류코드 : " + result.getMessage() + "\n오류 메시지 : " + result.getMessage();
        }
        log.debug("objRetParams" + objRetParams);

        //return objRetParams.toString();
        return resultMsg;
    }
}
