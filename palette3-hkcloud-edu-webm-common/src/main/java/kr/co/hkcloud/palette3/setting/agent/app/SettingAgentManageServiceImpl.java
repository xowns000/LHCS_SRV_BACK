package kr.co.hkcloud.palette3.setting.agent.app;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.app.TwbUserBizServiceImpl;
import kr.co.hkcloud.palette3.common.twb.app.TwbUserLogBizServiceImpl;
import kr.co.hkcloud.palette3.common.twb.constants.TwbCmmnConst;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.security.crypto.Hash;
import kr.co.hkcloud.palette3.core.util.PaletteUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Slf4j

@RequiredArgsConstructor
@Service("settingAgentManageService")
public class SettingAgentManageServiceImpl implements SettingAgentManageService {

    private final TwbComDAO mobjDao;
    private final PaletteUtils paletteUtils;
    private final TwbUserBizServiceImpl twbUserBizServiceImpl;
    private final TwbUserLogBizServiceImpl twbUserLogBizServiceImpl;
    private final InnbCreatCmmnService innbCreatCmmnService;

    /**
     * 호분배 사용자 생성시
     */

    @Override
    public TelewebJSON UserBatchInterface(TelewebJSON mjsonParams) throws TelewebAppException {
        try {
            String custcoId = mjsonParams.getString("ASP_NEWCUST_KEY").toString();
            String custcoIdArray[] = custcoId.split(",");

            String aspUserId = mjsonParams.getString("USER_ID");
            String inLineNo = mjsonParams.getString("INLNE_NO");
            //String custcoId = mjsonParams.getString("ASP_NEWCUST_KEY");
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000); //타임아웃 설정 5초
            factory.setReadTimeout(5000);//타임아웃 설정 5초

            RestTemplate restTemplate = new RestTemplate(factory);

            HttpHeaders header = new HttpHeaders();
            String url = "http://121.67.187.236:60080/API/";

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("REQ", "agent_forwarding");
            jsonObject.put("AGENT_ID", aspUserId);
            jsonObject.put("EXTENSION_NO", inLineNo);
            jsonObject.put("FKEY", "QUEUE");

            JSONArray req_array = new JSONArray();
            for (int i = 0; i < custcoIdArray.length; i++) {
                //data.put(null, custcoIdArray[i]);
                req_array.add(custcoIdArray[i]);
            }

            jsonObject.put("FDATA", req_array);

            log.debug("jsonObject =====================" + jsonObject);

            MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();

            parameters.add("tplexParam", jsonObject);
            log.debug("parameters ====" + parameters.toString());
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parameters, header);

            String result = restTemplate.postForObject(new URI(url), request, String.class);

            JSONObject jsonObj = JSONObject.fromObject(result);

            String ResultCode = jsonObj.getString("result_code");
            String ErrorMsg = "";

            switch (ResultCode) {
                case "1":
                    ErrorMsg = "등록되지 않은 상담사 ID 입니다.";
                    break;
                case "2":
                    ErrorMsg = "중복된 큐가 있습니다.";
                    break;
                case "3":
                    ErrorMsg = "등록되지 않은 큐 입니다.";
                    break;
                case "4":
                    ErrorMsg = "상담사가 로그인 상태입니다.";
                    break;
                case "5":
                    ErrorMsg = "FDATA 값이 없습니다.";
                    break;
                case "6":
                    ErrorMsg = "FKEY 값이 없습니다.";
                    break;
                case "7":
                    ErrorMsg = "중복된 업체코드가 있습니다.";
                    break;
                case "8":
                    ErrorMsg = "등록되지 않은 업체코드가 있습니다.";
                    break;

            }

            mjsonParams.setString("userVaild", jsonObj.getString("result_code"));
            mjsonParams.setString("Error", ErrorMsg);

            log.debug("jsonObj=======" + jsonObj);

        } catch (HttpClientErrorException | HttpServerErrorException e) {

            System.out.println(e.toString());

        } catch (Exception e) {

            System.out.println(e.toString());
        }

        return mjsonParams;
    }

    /**
     * 로그아웃 사용자 생성 or 수정
     */
    @Override
    public TelewebJSON UserLogoutInterface(TelewebJSON mjsonParams) throws TelewebAppException {

        try {
            log.debug("mjsonParams ============" + mjsonParams);
            String aspUserId = mjsonParams.getString("USER_ID");
            String inLineNo = mjsonParams.getString("INLNE_NO");
            //String custcoId = mjsonParams.getString("ASP_NEWCUST_KEY");
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000); //타임아웃 설정 5초
            factory.setReadTimeout(5000);//타임아웃 설정 5초

            RestTemplate restTemplate = new RestTemplate(factory);

            HttpHeaders header = new HttpHeaders();

            String url = "http://121.67.187.236:60080/API/";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("SP_CODE", "0000");
            jsonObject.put("REQ", "force_logout");
            jsonObject.put("AGENT_ID", aspUserId);
            jsonObject.put("EXTENSION_NO", inLineNo);

            log.debug("jsonObject =====================" + jsonObject);
            MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
            parameters.add("tplexParam", jsonObject);
            log.debug("parameters ====" + parameters.toString());
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(parameters, header);

            String result = restTemplate.postForObject(new URI(url), request, String.class);

            JSONObject jsonObj = JSONObject.fromObject(result);

            String ResultCode = jsonObj.getString("result_code");
            String ErrorMsg = "";

            switch (ResultCode) {
                case "1":
                    ErrorMsg = "등록되지 않은 상담사 ID 입니다.";
                    break;

            }

            mjsonParams.setString("userVaild", jsonObj.getString("result_code"));
            mjsonParams.setString("Error", ErrorMsg);

            log.debug("jsonObj=======" + jsonObj);

        } catch (HttpClientErrorException | HttpServerErrorException e) {

            System.out.println(e.toString());

        } catch (Exception e) {

            System.out.println(e.toString());
        }

        return mjsonParams;
    }

    /**
     * 
     * 사용자관리 - 사용자 목록 조회
     * 
     * @Method Name : selectRtnUserInfo
     * @date : 2023. 6. 15.
     * @author : NJY
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams 조회할 사용자 정보
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnUserInfo(TelewebJSON mjsonParams) throws TelewebAppException {
        log.debug("mjsonParams =================" + mjsonParams);
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //권한그룹 조건이 추가되고 상담사는 권한이 그룹정보가 없을 수 있기 때문에 동적 쿼리 조건을 추가함
        //        String strAtrtGroupID = mjsonParams.getString("ATRT_GROUP_ID");
        //        if(!strAtrtGroupID.equals("")) {
        //            if(strAtrtGroupID.equals("20151106173043466TWB14896")) {
        //                mjsonParams.setString("GROUP_F", "Y");
        //            }
        //            else {
        //                mjsonParams.setString("GROUP_F", "N");
        //            }
        //        }

        //        mjsonParams.setString("SORT_ORDR", mjsonParams.getString("SORT_ORDR"));
        //:GS인증 요청사항 %, _ 치환. - 20.06.17 knj
        //        String paramUserId = paletteUtils.replacePercentUnderBar(mjsonParams.getString("USER_ID")); // 사용자id
        //        String paramUserNm = paletteUtils.replacePercentUnderBar(mjsonParams.getString("USER_NM")); // 사용자이름
        //        String paramUserNick = paletteUtils.replacePercentUnderBar(mjsonParams.getString("USER_NICK")); // 사용자 닉네임
        //        String paramCnnctIp = paletteUtils.replacePercentUnderBar(mjsonParams.getString("CNNCT_IP")); // 접속 IP
        //        if(!"".equals(paramUserId))
        //            mjsonParams.setString("USER_ID", paramUserId);
        //        if(!"".equals(paramUserNm))
        //            mjsonParams.setString("USER_NM", paramUserNm);
        //        if(!"".equals(paramUserNick))
        //            mjsonParams.setString("USER_NICK", paramUserNick);
        //        if(!"".equals(paramCnnctIp))
        //            mjsonParams.setString("CNNCT_IP", paramCnnctIp);

        //사용자정보를 조회한다.
        //        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "selectRtnUserInfo", mjsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "selectRtnUserInfo_new",
            mjsonParams);
        log.debug("this select param" + mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 설정사용자관리 사용자 소속을 등록한다.
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON processRtnAttr(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "processRtnAttr", mjsonParams);
    }

    /**
     * 
     * 사용자관리 - 사용자 등록 및 수정
     * 
     * @Method Name : processRtn
     * @date : 2023. 7. 26.
     * @author : NJY
     * @version : 1.1
     * ----------------------------------------
     * @param mjsonParams 등록,수정할 사용자 정보
     * @return
     * @throws TelewebAppException
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON processRtn(TelewebJSON mjsonParams) throws TelewebAppException {

        log.debug("mjsonParams =================" + mjsonParams);

        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        /* 등록/수정 DATA_FLAG 초기화 */

        String strTransFlag = mjsonParams.getString("DATA_FLAG");

        /* 프론트에서 넘어오는 USER_ID는 로그인한 사용자의 ID이기 때문에 구분을 위해 RGTR_ID로 변경 */

        String RGTR_ID = mjsonParams.getString("USER_ID");
        mjsonParams.setString("RGTR_ID", RGTR_ID);

        /* 초기 비밀번호값 세팅을 위해 LGN_ID를 PSWD 키와 밸류 생성 */

        //        if(StringUtils.isBlank(mjsonParams.getString("PSWD"))) {
        //          String GEN_PSWD = mjsonParams.getString("LGN_ID");
        //          mjsonParams.setString("PSWD", GEN_PSWD);
        //        }  >> 화면에서 암호화 해서 넘겨주기 떄문에 불필요 -- 삭제

        //데이터 체크가 통과할 경우만 처리
        if (validationProcess(mjsonParams, objRetParams)) {
            // 사용자 허용 계정수 체크
            if (chkUserAcntCnt(mjsonParams, objRetParams)) {

                //신규/수정 상태에 따라 insert, update 함수 호출
                if (strTransFlag.equals(TwbCmmnConst.TRANS_INS)) {

                    /*   시퀀스로 등록할 사용자의 USER_ID 생성   */
                    int GEN_USER_ID = innbCreatCmmnService.createSeqNo("USER_ID");
                    mjsonParams.setInt("GEN_USER_ID", GEN_USER_ID);

                    int GEN_CERT_USER_ID = innbCreatCmmnService.createSeqNo("CERT_USER_ID");
                    mjsonParams.setInt("GEN_CERT_USER_ID", GEN_CERT_USER_ID);

                    objRetParams = twbUserBizServiceImpl.insertRtn(mjsonParams);
                    objRetParams = twbUserBizServiceImpl.insertCertUser(mjsonParams);
                    objRetParams.setHeader("GEN_USER_ID", GEN_USER_ID); //개인정보 로깅용도.

                    //채팅 사용자 업데이트
                    twbUserBizServiceImpl.insertChatUser(mjsonParams);
                } else if (strTransFlag.equals(TwbCmmnConst.TRANS_UPD)) {
                    objRetParams = twbUserBizServiceImpl.updateRtn(mjsonParams);
                }
                // 관리자가 회원을 활성화 할 때, 사용자 개인 채팅허용수를 저장\
                //20.05.19 ChatAgent 초기화를 twbUserBiz.insertRtn이동하고,twbUserBiz.updateRtn에서 동작하지 않도록함.
                //소속, 닉네임 등이 변경되어서 값이 초기화 될 수는 없다(기설정된 값이 관리지가 인지하지 못한 상태에서 초기화되는 문제가 있을 수 있음).
                //mobjDao.insert("kr.co.hkcloud.palette.setting.agent.dao.SettingAgentManageMapper", "mergeUserChatAgent", mjsonParams);
            } else {
                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", "허용된 상담사 계정 개수를 초과하였습니다.\n 시스템 관리자에게 문의 바랍니다.");
            }
        }
        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 데이터 처리시 데이터체크
     * 
     * @param jsonParams 전송된 파라메터 데이터
     * @param objRetParams 반환파라메터
     * @return true:처리가능상태, false:처리불가능상태
     */
    private boolean validationProcess(TelewebJSON jsonParams, TelewebJSON objRetParams) throws TelewebAppException {
        String strTransFlag = jsonParams.getString("DATA_FLAG");            //화면에서 전송된 플래그 설정

        //공통 항목에 대한데이터 체크
        //신규/수정 상태에 따른 다른 체크가 필요할 경우 추가 조건 설정
        if (strTransFlag.equals(TwbCmmnConst.TRANS_INS)) {
            //동일 ID가 존재하는 체크
            if (twbUserBizServiceImpl.UserDpncChk(jsonParams)) {
                objRetParams.setHeader("ERROR_FLAG", true);
                objRetParams.setHeader("ERROR_MSG", "이미 등록된 아이디입니다.");
                return false;
            }
        } else if (strTransFlag.equals(TwbCmmnConst.TRANS_UPD)) {
            //수정시 필수체크 정의
        }

        //초기값 설정
        objRetParams.setHeader("ERROR_FLAG", false);

        return true;
    }

    /**
     * 
     * 사용자관리 - -등록-사용자ID중복체크
     * 
     * @Method Name : UserDpncChk
     * @date : 2023. 6. 16.
     * @author : NJY
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams 체크할 사용자 정보
     * @return
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON UserDpncChk(TelewebJSON mjsonParams) throws TelewebAppException {

        //      String strTransFlag = mjsonParams.getString("DATA_FLAG");            //화면에서 전송된 플래그 설정

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);             // 반환 파라메터 생성

        //공통 항목에 대한데이터 체크
        //신규/수정 상태에 따른 다른 체크가 필요할 경우 추가 조건 설정
        //        if(strTransFlag.equals(TwbCmmnConst.TRANS_INS)) {
        //동일 ID가 존재하는 체크
        if (twbUserBizServiceImpl.UserDpncChk(mjsonParams)) {
            objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "이미 등록된 아이디입니다.");
            return objRetParams;
        } else {

            //초기값 설정
            objRetParams.setHeader("ERROR_FLAG", false);
        }

        //        }
        //        else if(strTransFlag.equals(TwbCmmnConst.TRANS_UPD)) {
        //            //수정시 필수체크 정의
        //        }

        return objRetParams;

    }

    /**
     * 
     * 사용자관리 - 비밀번호 초기화
     * 
     * @Method Name : updatePasswordReset
     * @date : 2023. 6. 19.
     * @author : NJY
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams 비밀번호 초기화할 사용자 정보
     * @return objRetParams
     * @throws TelewebAppException
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON updatePasswordReset(TelewebJSON mjsonParams) throws TelewebAppException {
        ServletRequestAttributes mobjRequest = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest objRequest = mobjRequest.getRequest();
        HttpSession mobjSession = objRequest.getSession();

        mjsonParams.setString("RGTR_ID", mjsonParams.getString("USER_ID"));
        //회원정보를 검색한 후 비밀번호 업데이트에 필요한 체크를 수행한다.
        log.debug("updatePasswordReset mjsonParams jy ===============================" + mjsonParams);
        TelewebJSON jsonUserInfo = twbUserBizServiceImpl.selectRtnForPSWD(mjsonParams);

        //        String keyVal = (String) mobjSession.getAttribute("ENCRYPT_KEY");

        /* 
         *      기존의 로직대로 화면에서 암호화를 해서 보내면 Invalid AES key length: 64 bytes
         *      확인 필요
         *        
         *       String keyVal = mjsonParams.getString("ENCRYPT_KEY");
         *       String EncVal = mjsonParams.getString("NEW_PWD").replace(" ", "+");
         *       log.debug("updatePasswordReset keyVal jy ==============================="+EncVal);
         *       String pwd;
         *       try {
         *           pwd = AES256Cipher.decryptString(EncVal, keyVal);
         *       }
         *       catch(InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
         *           throw new TelewebAppException(e.getLocalizedMessage(), e);
         *       }
         *       log.debug("updatePasswordReset pwd jy ==============================="+pwd);
         */
        mobjSession.setAttribute("ENCRYPT_KEY", "");

        String strEncryptKey = jsonUserInfo.getString("PSWD_ENCPT_CD");
        String strDecryptKey = twbUserBizServiceImpl.keyDecString(strEncryptKey);
        String pwd = mjsonParams.getString("RESET_LGN_ID");

        //초기 비번 설정
        if (mjsonParams.getString("RESET_PWD_TXT").isEmpty()) {
            pwd = pwd + "1!";
        } else {
            pwd = mjsonParams.getString("RESET_PWD_TXT");
        }

        String shaPwd;
        try {
            shaPwd = Hash.encryptSHA256(pwd, StandardCharsets.UTF_8);
        } catch (NoSuchAlgorithmException e) {
            throw new TelewebAppException(e.getLocalizedMessage(), e);
        }

        //비번 sha2==> aes256
        String lastPwd = twbUserBizServiceImpl.pwdEncString(shaPwd, strDecryptKey);

        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //DB Access 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        jsonParams.setString("PSWD", lastPwd);
        jsonParams.setString("PSWD_STTS", "NORMAL");
        jsonParams.setString("TASK_SE_CD", mjsonParams.getString("TASK_SE_CD"));
        log.debug("updatePasswordReset ===============================", jsonParams);
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "updatePasswordReset",
            jsonParams);

        //비밀번호 변경 로그를 기록한다.
        twbUserLogBizServiceImpl.insertRtnChangePwdInfo(jsonParams, objRequest.getRemoteAddr());

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 설정사용자관리 사용자 비밀번호잠금을 초기화한다
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON updatePasswordUnLock(TelewebJSON mjsonParams) throws TelewebAppException {
        HttpServletRequest mobjRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession mobjSession = mobjRequest.getSession();

        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //DB Access 파라메터 생성
        TelewebJSON jsonParams = new TelewebJSON(mjsonParams);
        jsonParams.setDataObject(mjsonParams.getDataObject(TwbCmmnConst.G_DATA));

        String MDFR_ID = mjsonParams.getString("USER_ID");
        //        String TASK_SE_CD = mjsonParams.getString("TASK_SE_CD");

        jsonParams.setString("PSWD_FAIL_CNT", "0");
        jsonParams.setString("PSWD_STTS", "NORMAL");
        jsonParams.setString("MDFR_ID", MDFR_ID);
        //        jsonParams.setString("TASK_SE_CD", TASK_SE_CD);
        jsonParams.setString("ETC_INFO_01", "계정 잠금 해제");

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "updatePasswordUnLock",
            jsonParams);

        //비밀번호 변경 로그를 기록한다.
        twbUserLogBizServiceImpl.insertRtnChangePwdInfo(mjsonParams, mobjRequest.getRemoteAddr());

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 설정사용자관리 사용자정보를 조회한다
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnUserBaseInfo(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "selectRtnUserBaseInfo", mjsonParams);
    }

    /**
     * 설정사용자관리 사용자목록을 조회한다
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectTwbBas01(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "SELECT_TWB_BAS01", mjsonParams);
    }

    /**
     * 설정사용자관리 비밀번호를 조회한다
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectTwbBas01pWD(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "SELECT_PLT_USER_PWD", mjsonParams);
    }

    /**
     * 설정사용자관리 사용자를 등록한다
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON insertRtn(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "INSERT_TWB_BAS01", mjsonParams);
    }

    /**
     * 관리자가 회원을 활성화 할 때, 사용자 개인 채팅 허용 수를 저장
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON mergeUserChatAgent(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.insert("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "mergeUserChatAgent", mjsonParams);
    }

    /**
     * 설정사용자관리 사용자를 수정한다
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON updateRtnUser(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "UPDATE_TWB_BAS01_USER", mjsonParams);
    }

    /**
     * 설정사용자관리 사용자를 수정한다
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON updateRtn(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "UPDATE_TWB_BAS01", mjsonParams);
    }

    /**
     * 설정사용자관리-팝업-페이지목록(사용안함)
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnUserPage(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "selectUserListPage", mjsonParams);
    }

    /**
     * 설정사용자관리 팝업화면 목록을 조회한다
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnUserPage_new(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "selectRtnUserPage_new", mjsonParams);
    }

    /**
     * 사용자 정보 목록 조회 팝업
     * 
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON selectRtnUserInfoPop(TelewebJSON mjsonParams) throws TelewebAppException {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "selectRtnUserInfoPop", mjsonParams);
    }

    /**
     * 사용자 삭제
     * 사용자 삭제시 상담이력등의 문제가 발생할 수 있음
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON deleteUser(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "deleteUserTB", mjsonParams);
        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "deleteChatSetTB", mjsonParams);
        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "deleteUserAuthTB", mjsonParams);
        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "deleteUserAttrTB", mjsonParams);

        return objRetParams;
    }

    /**
     * 사용자 ID 수정
     * 사용자 수정시 상담이력등의 문제가 발생할 수 있음
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON updateUserId(TelewebJSON mjsonParams) throws TelewebAppException {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "updateUserTB", mjsonParams);
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "updateChatSetTB", mjsonParams);
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "updateUserAuthTB", mjsonParams);
        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "updateUserAttrTB", mjsonParams);

        return objRetParams;
    }

    /**
     * 사용자 프로필 수정
     *
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateProfile(TelewebJSON jsonParams) throws TelewebAppException {
        return mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "UPDATE_PLT_USER_USER", jsonParams);
    }

    /**
     * 강제 비밀번호 변경
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = {Exception.class, SQLException.class}, readOnly = false)
    public TelewebJSON forceUpdatePassword(TelewebJSON mjsonParams) throws TelewebAppException {
        JSONArray jsonObj = mjsonParams.getDataObject(TwbCmmnConst.G_DATA);
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        for (int i = 0; i < jsonObj.size(); i++) {
            JSONObject objData = jsonObj.getJSONObject(i);

            @SuppressWarnings("rawtypes") Iterator it = objData.keys();
            String RESET_USER_ID = "";
            String RESET_LGN_ID = "";
            while (it.hasNext()) {
                String strKey = (String) it.next();
                String strValue = objData.getString(strKey);

                if (strKey.indexOf("TARGET_USER_ID") > -1 && StringUtils.isNotEmpty(strValue)) {
                    RESET_USER_ID = strValue;
                }

                if (strKey.indexOf("TARGET_LGN_ID") > -1 && StringUtils.isNotEmpty(strValue)) {
                    RESET_LGN_ID = strValue;
                }
            }

            mjsonParams.setString("RESET_USER_ID", RESET_USER_ID);
            mjsonParams.setString("RESET_LGN_ID", RESET_LGN_ID);

            objRetParams = this.updatePasswordReset(mjsonParams);
        }

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 고객사 상담사 허용 계정 수 체크
     */
    @Override
    @Transactional(readOnly = true)
    public boolean chkUserAcntCnt(TelewebJSON mjsonParams, TelewebJSON objRetParams) throws TelewebAppException {

        //        TelewebJSON objRetParam = new TelewebJSON(mjsonParams);

        String strTransFlag = mjsonParams.getString("DATA_FLAG");
        String AUTHRT_GROUP_ID = mjsonParams.getString("AUTHRT_GROUP_ID");
        String USER_STTS_CD = mjsonParams.getString("USER_STTS_CD");
        String CHK_AUTHRT_CHG = mjsonParams.getString("CHK_AUTHRT_CHG");

        boolean flag = true;
        /**
         * 1. 사용자의 권한 그룹이 상담사가 아닐 때
         * 2. 재직 상태가 아닐 때
         * 3. 사용자정보를 수정, 권한그룹을 수정하지 않았을 때
         * 허용 계정 수 체크하지 않음
         */
        if (!"6".equals(AUTHRT_GROUP_ID) || !"WORK".equals(USER_STTS_CD) || (strTransFlag.equals(TwbCmmnConst.TRANS_UPD) && "N".equals(
            CHK_AUTHRT_CHG))) {
            flag = true;
            return flag;
        } else {
            objRetParams = mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "chkUserAcntCnt",
                mjsonParams);

            int left_user_cnt = Integer.parseInt(objRetParams.getString("LEFT_USER_CNT"));

            if (left_user_cnt <= 0) {
                flag = false;
                return flag;
            }
        }
        return flag;
    }
    
    /**
     * 전체 스키마별 고객사 목록
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON schemaCustcoList(TelewebJSON mjsonParams) throws TelewebAppException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "schemaCustcoList", mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 고객사별 상담원 목록
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON custcoCuslList(TelewebJSON mjsonParams) throws TelewebAppException {
    	//반환 파라메터 생성
    	TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
    	objRetParams = mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "custcoCuslList", mjsonParams);
    	
    	//최종결과값 반환
    	return objRetParams;
    }
}
