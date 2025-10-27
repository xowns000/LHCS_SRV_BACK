package kr.co.hkcloud.palette3.common.twb.app;


import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.security.properties.PaletteSecurityProperties;
import kr.co.hkcloud.palette3.core.security.crypto.AES256Cipher;
import kr.co.hkcloud.palette3.core.security.crypto.Base64;
import kr.co.hkcloud.palette3.core.security.crypto.Hash;
import kr.co.hkcloud.palette3.core.util.PaletteFilterUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.setting.system.app.SettingSystemAuthorityByAgentManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("twbUserBizService")
public class TwbUserBizServiceImpl implements TwbUserBizService
{
    private final TwbComDAO                                  mobjDao;
    private final PaletteSecurityProperties                  paletteSecurityProperties;
    private final PaletteFilterUtils                         paletteCmmnFilterUtils;
    private final SettingSystemAuthorityByAgentManageService settingSystemAuthorityByAgentManageService;


    /**
     * 사용자 정보를 검색한다
     * 
     * @param  jsonParams          검색할 조회조건
     * @param  objDAO              TelewebComDAO 객체
     * @return                     검색결과
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtn(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "selectRtnUserPage_new", jsonParams);
    }


   
    /**
	 * 
	 * LGN_ID값만 이용하여 사용자 정보를 검색한다
	 * @Method Name  	: selectRtnForPSWD
	 * @date   			: 2023. 6. 19.
	 * @author   		: NJY
	 * @version     	: 1.0
	 * ----------------------------------------
	 * @param  jsonParams	비밀번호 초기화할 사용자 정보
	 * @return objRetParams
	 * @throws TelewebAppException
	 */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnForID(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON jsonParam01 = new TelewebJSON(jsonParams);
        jsonParam01.setString("LGN_ID", jsonParams.getString("LGN_ID"));
        jsonParam01.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));

        return mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "SELECT_PLT_USER", jsonParam01);
    }
    
    /**
	 * 
	 * 사용자관리 - 비밀번호 초기화 - 로그인ID로 초기화할 사용자 조회
	 * @Method Name  	: selectRtnForPSWD
	 * @date   			: 2023. 6. 19.
	 * @author   		: NJY
	 * @version     	: 1.0
	 * ----------------------------------------
	 * @param  mjsonParams	비밀번호 초기화할 사용자 정보
	 * @return objRetParams
	 * @throws TelewebAppException
	 */
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnForPSWD(TelewebJSON jsonParams) throws TelewebAppException
    {
    	TelewebJSON jsonParam01 = new TelewebJSON(jsonParams);
    	jsonParam01.setString("LGN_ID", jsonParams.getString("RESET_LGN_ID"));
    	jsonParam01.setString("USER_ID", jsonParams.getString("RESET_USER_ID"));
    	jsonParam01.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
    	
    	return mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "SELECT_PLT_USER", jsonParam01);
    }
    

    /**
     * 사용자 ID를 이용하여 등록여부를 판단한다.
     * 
     * @param  jsonParams          사용자정보
     * @param  objDAO              TelewebComDAO 객체
     * @return                     true:등록상태, false:미등록상태
     * @throws TelewebAppException
     */
    @Transactional(readOnly = true)
    public boolean isUserReg(TelewebJSON jsonParams) throws TelewebAppException
    {
        try {
            TelewebJSON jsonParam01 = new TelewebJSON(jsonParams);
            jsonParam01.setString("USER_ID", jsonParams.getString("USER_ID"));
            jsonParam01.setString("CUSTCO_ID", jsonParams.getString("CUSTCO_ID"));
            TelewebJSON jsonUserData = mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "SELECT_PLT_USER", jsonParam01);
            if(jsonUserData.getHeaderInt("COUNT") == 0) {
                return false;
            }
            else {
                return true;
            }
        }
        catch(Exception ex) {
            return false;
        }
    }
    
    /**
	 * 
	 * 사용자관리 - 사용자 등록 - 사용자 ID 중복 체크
	 * @Method Name  	: UserDpncChk
	 * @date   			: 2023. 7. 26.
	 * @author   		: NJY
	 * @version     	: 1.1
	 * ----------------------------------------
	 * @param  jsonParams	사용자 정보
	 * @return 수정결과
	 * @throws TelewebAppException
	 */
    @Transactional(readOnly = true)
    public boolean UserDpncChk(TelewebJSON jsonParams) throws TelewebAppException
    {
    	try {
    		TelewebJSON jsonParam01 = new TelewebJSON(jsonParams);
    		jsonParam01.setString("LGN_ID", jsonParams.getString("LGN_ID"));
    		
    		TelewebJSON jsonUserData = mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "SELECT_PLT_USER_DPNC", jsonParam01);
    		
    		TelewebJSON jsonCertUserData = mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "SELECT_PLT_CERT_USER_DPNC", jsonParam01);
    		if(jsonUserData.getHeaderInt("COUNT") == 0 && jsonCertUserData.getHeaderInt("COUNT") == 0) {
    			return false;
    		}
    		else {
    			return true;
    		}
    	}
    	catch(Exception ex) {
    		return false;
    	}
    }


    /**
	 * 
	 * 사용자관리 - 사용자 등록
	 * @Method Name  	: insertRtn
	 * @date   			: 2023. 6. 16.
	 * @author   		: NJY
	 * @version     	: 1.0
	 * ----------------------------------------
	 * @param  jsonParams	사용자 정보
	 * @return 등록결과
	 * @throws TelewebAppException
	 */
    @Transactional(readOnly = false)
    public TelewebJSON insertRtn(TelewebJSON jsonParams) throws TelewebAppException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        //서블릿 컨텍스트에서 세션 추출
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest objRequest = requestAttributes.getRequest();
        HttpSession mobjSession = objRequest.getSession();
        
       
        //초기 데이터 생성시 기본파라메터 정의
        if(!jsonParams.containsKey("USE_YN")) {
            jsonParams.setString("USE_YN", "N");
        }

        /* 관리화면에서 관리자가 등록 시 인증여부를 Y로 설정한다. */
        if(!jsonParams.containsKey("USER_CERT_YN")) {
            jsonParams.setString("USER_CERT_YN", "Y");
        }

        /* 현재 로그인 상태를 N으로 설정한다. */
        if(!jsonParams.containsKey("PRST_LGN_YN")) {
            jsonParams.setString("PRST_LGN_YN", "N");
        }
        /* 현재 비밀번호 상태를 NORMAL으로 설정한다. */
        if(!jsonParams.containsKey("PSWD_STTS")) {
        	jsonParams.setString("PSWD_STTS", "NORMAL");
        }
        
		/* 상단메시지 설정값이 없다면 미사용으로 설정  */
        if(!jsonParams.containsKey("TOP_MSG")) {
        	jsonParams.setString("TOP_MSG", "상단메시지 미사용");
        }
        jsonParams.setString("PSWD_FAIL_CNT", "0");
        
        String DATA_FLAG = jsonParams.getString("DATA_FLAG");
        //패스워드를 복호화.
        if(DATA_FLAG == "SIGN_UP") {
	        String EncVal = jsonParams.getString("PSWD").replace(" ", "+");
	        //String keyVal = (String) mobjSession.getAttribute("ENCRYPT_KEY");
	        String keyVal = jsonParams.getString("ENCRYPT_KEY");
	        String pwd;
	        try {
	            pwd = AES256Cipher.decryptString(EncVal, keyVal);
	        }
	        catch(InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
	            throw new TelewebAppException(e.getLocalizedMessage());
	        }
	        mobjSession.setAttribute("ENCRYPT_KEY", "");
        }else {
        	String pwd = jsonParams.getString("LGN_ID");
	        //초기 비번 설정
	        if(jsonParams.containsKey("NO_ENC_PWD")) {
	
	            String shaPwd = pwd;
	            //비번 EncryptKey 값 추출
	            String strEncryptKey = createEncryptKey();
	            //비번 EncryptKey 암호화 생성 
	            String lastEncryptKey = keyEncString(strEncryptKey);
	            //비번 sha2==> aes256
	            String lastPwd = pwdEncString(shaPwd, strEncryptKey);
	            //jsonParams에 값 설정
	            jsonParams.setString("PSWD", lastPwd);
	            jsonParams.setString("ENCRYPT_KEY", lastEncryptKey);
	        }
	        else {
	        		
	            pwd = pwd + "1!";
	            String shaPwd;
	            try {
	                shaPwd = Hash.encryptSHA256(pwd, StandardCharsets.UTF_8);
	            }
	            catch(NoSuchAlgorithmException e) {
	                throw new TelewebAppException(e.getLocalizedMessage());
	            }
	            //비번 EncryptKey 값 추출
	            String strEncryptKey = createEncryptKey();
	            //비번 EncryptKey 암호화 생성 
	            String lastEncryptKey = keyEncString(strEncryptKey);
	            //비번 sha2==> aes256
	            String lastPwd = pwdEncString(shaPwd, strEncryptKey);
	            //jsonParams에 값 설정
	            jsonParams.setString("PSWD", lastPwd);
	            jsonParams.setString("ENCRYPT_KEY", lastEncryptKey);
	        }
        }
        //쿼리 문장 수정해야 함

//      jsonParams.setString("REGR_DEPT_CD", jsonParams.getString("REGR_DEPT_CD")); //20190510 ojw changed: 해당 파라메터가 초기화되는 증상이 생겨 주석처리

        /* 20190510 ojw: 조직 개편으로 인해 안쓰이는 컬럼이지만 REGR_ID 등 문제를 방지하기 위해 dummy 값 설정 */
//        jsonParams.setString("DEPT_CD", "x");

		/*
		 * jsonParams.setString("REGR_ID", jsonParams.getString("PROC_ID"));
		 * jsonParams.setString("USER_NM",
		 * paletteCmmnFilterUtils.filter3(jsonParams.getString("USER_NM")));
		 * jsonParams.setBlankToNull(0);
		 */

		/*
		 * if(jsonParams.containsKey("SIGN_UP")) { jsonParams.setString("USER_NNM",
		 * jsonParams.getString("USER_NM")); }
		 */
        
//        log.debug("jsonParams =============================" + jsonParams);
        
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "INSERT_PLT_USER", jsonParams);
        
        /* 관리자가 사용자를 저장 할 때 사용자의 부서까지 함께 저장  */
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "INSERT_PLT_USER_OGNZ", jsonParams);

        /* 관리자가 사용자를 저장 할 때 사용자의 권한그룹까지 함께 저장  */
        objRetParams = settingSystemAuthorityByAgentManageService.insertAtrtGroupIdByUser(jsonParams);
        
        
        
        
        
        

        // 관리자가 회원을 활성화 할 때, 사용자 개인 채팅 허용 수를 저장
//        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "mergeUserChatAgent", jsonParams);

 /*
  *       // 관리자가 사용자를 등록할 때 선택된 권한을 저장한다.
  *      // 회원가입이 아닐 때 수행한다.
  *       if(!jsonParams.containsKey("SIGN_UP")) {
  *           jsonParams.setString("ATRT_GROUP_ID", jsonParams.getString("ATRT_GROUP_CD"));
  *           objRetParams = settingSystemAuthorityByAgentManageService.insertAtrtGroupIdByUser(jsonParams);
  *       }
  **/
        return objRetParams;
    }

    
    /**
     * 사용자 관리 - 등록 - 등록 사용자 CUSTCO 스키마에 INSERT
     */    
    @Transactional(readOnly = false)
    public TelewebJSON insertCertUser(TelewebJSON jsonParams) throws TelewebAppException
    {
    	TelewebJSON objRetParams = new TelewebJSON(jsonParams);
    	
    	TelewebJSON jsonCertParam = new TelewebJSON(jsonParams);
    	
    	jsonCertParam.setString("CUSTCO_ID",jsonParams.getString("CUSTCO_ID"));
    	
    	TelewebJSON objRetCertCustco = mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "SELECT_ASP_CUST_KEY", jsonCertParam);
    	
    	if(!"".equals(objRetCertCustco.getString("ASP_CUST_KEY"))){  		
		
			jsonCertParam.setString("ASP_CUST_KEY",objRetCertCustco.getString("ASP_CUST_KEY"));
    		
    		objRetCertCustco = 
    				mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "SELECT_CERT_CUSTCO_ID", jsonCertParam);
    		
    		jsonParams.setString("CERT_CUSTCO_ID",objRetCertCustco.getString("CERT_CUSTCO_ID"));
    		
    	}else {
    		
    		objRetParams.setHeader("ERROR_FLAG", true);
            objRetParams.setHeader("ERROR_MSG", "등록되지 않은 고객사 입니다.");
            
    	};
    	
    	if(jsonParams.containsKey("CERT_CUSTCO_ID")) {
            jsonParams.setString("RGTR_ID","2");
    		objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "INSERT_PLT_CERT_USER", jsonParams);
    		objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "INSERT_PLT_CERT_CUSTCO_USER", jsonParams);
    		log.debug("insert cert_custco_user");
    	}else {
    		objRetParams.setHeader("ERROR_FLAG", true);
    	}
    	
    	return objRetParams;
    }
    
    /**
	 * 
	 * 사용자관리 - 사용자 수정
	 * @Method Name  	: updateRtn
	 * @date   			: 2023. 6. 16.
	 * @author   		: NJY
	 * @version     	: 1.0
	 * ----------------------------------------
	 * @param  jsonParams	수정할 사용자 정보
	 * @return 수정결과
	 * @throws TelewebAppException
	 */
    @Transactional(readOnly = false)
    public TelewebJSON updateRtn(TelewebJSON jsonParams) throws TelewebAppException
    {

        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);

        jsonParams.setBlankToNull(0);
        jsonParams.setString("REG_USER_ID", paletteCmmnFilterUtils.filter3(jsonParams.getString("REG_USER_ID")));

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "UPDATE_PLT_USER_USER", jsonParams);

        /* 사용자 조직 정보 업데이트 */
        if(jsonParams.getString("DEPT_CHG_YN").equals("Y")) {
        	//소속 부서 변경 시 기존 부서 남겨놓고 USE_YN = 'N'처리
        	//새로운 소속 부서로 INSERT처리
        	jsonParams.setString("GEN_USER_ID",jsonParams.getString("REG_USER_ID"));
        	objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "UPDATE_PLT_USER_OGNZ", jsonParams);
        	objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "INSERT_PLT_USER_OGNZ", jsonParams);
        } else {
        	//부서변경 없을 시 따로 처리하지 않음
        }
//        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "UPDATE_PLT_USER_OGNZ", jsonParams);

//        // 관리자가 회원을 활성화 할 때, 사용자 개인 채팅 허용 수를 저장
//        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "mergeUserChatAgent", jsonParams);

        // 관리자가 사용자를 수정할 때 선택된 권한을 저장한다.
//        jsonParams.setString("ATRT_GROUP_ID", jsonParams.getString("ATRT_GROUP_NM"));
//        log.debug("jsonParams ====================" + jsonParams);
        
        
        objRetParams = settingSystemAuthorityByAgentManageService.updateAtrtGroupIdByUser(jsonParams);
        
        /**
         * 사용자 상태가 재직이 아닐 떄 PLT_PHN_IP_EXT에서 사용자 내선번호를 삭제
         */

        /* 내선번호가 있는지 체크 하여 수정, 삭제  */
        TelewebJSON getExtNoParam =  new TelewebJSON();

        getExtNoParam = mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "selectExtNoByUserId", jsonParams);

        /* 내선번호 정보가 존재 할 때*/
        if(getExtNoParam.getSize() > 0){
            /* 업데이트한 사용자 상태 코드가 WORK 일 때 */
            if("WORK".equals(jsonParams.getString("USER_STTS_CD"))){
                /* 업데이트한 사용자의 부서 ID가 다를 때 */
                if(!getExtNoParam.getString("DEPT_ID").equals(jsonParams.getString("DEPT_ID"))){
                    mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "updateDeptIdInExt", jsonParams);
                }
            }else{
                /* 업데이트한 사용자 상태 코드가 WORK 아닐 때, 내선번호 정보 삭제 */
                mobjDao.delete("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "deleteExtNo", jsonParams);
            }
        }
        return objRetParams;
    }


    /**
     * 비밀번호만 수정한다.
     * 
     * @param  jsonParams          수정할 비밀번호정보
     * @param  objDAO              TelewebComDAO 객체
     * @return                     수정결과
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnPwd(TelewebJSON jsonParams) throws TelewebAppException
    {
    	
    	TelewebJSON objRtnParam = new TelewebJSON(jsonParams); 
        //비번 EncryptKey 값 추출
        String keyVal = jsonParams.getString("SESSION_ENCRYPT_KEY");
        String credentials = jsonParams.getString("PASSWORD_NEW");
        String encryptKey = jsonParams.getString("ENCRYPT_KEY");
        String decCredentials = null;
        String decryptKey = null;

        decCredentials = pwdDecString(credentials, keyVal);
        decryptKey = keyDecString(encryptKey);

        //비번 sha2==> aes256
        String lastPwd = pwdEncString(decCredentials, decryptKey);
//        log.debug(decCredentials);
//        log.debug(decryptKey);
        

        //jsonParams에 값 설정

        jsonParams.setString("PSWD", lastPwd);
        
        
        
        //비밀번호 변경
//        log.debug("updateRtnPwdupdateRtnPwdupdateRtnPwdupdateRtnPwdupdateRtnPwdupdateRtnPwd"+jsonParams);
        
        return objRtnParam = mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "UPDATE_PWD_STATUS", jsonParams); 
    }


    /**
     * 로그인 정보를 업데이트 한다. (독립적 트랜젝션으로 정의)
     * 
     * @param  jsonParams
     * @param  strUserID   사용자ID
     * @param  strAccessIP 접속ID
     * @return             수정결과
     * @author             MPC R&D Team
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnLoginInfo(TelewebJSON jsonParams, String strUserID, String strAccessIP) throws TelewebAppException
    {
        TelewebJSON jsonParam01 = new TelewebJSON(jsonParams);
        jsonParam01.setString("USER_ID", strUserID);
        jsonParam01.setString("CNNCT_IP", strAccessIP);
        jsonParam01.setString("PSNT_LOGIN_YN", "Y");
        jsonParam01.setString("PWD_FAIL_FREQ", "0");
        jsonParam01.setString("PROC_ID", strUserID);

        return mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "UPDATE_PLT_USER", jsonParam01);
    }


    /**
     * 로그아웃 정보를 업데이트 한다. (독립적 트랜젝션으로 정의)
     * 
     * @param  jsonParams
     * @param  strUserID   사용자ID
     * @param  strAccessIP 접속ID
     * @return             수정건수
     * @author             MPC R&D Team
     */
    @Transactional(readOnly = false)
    public TelewebJSON updateRtnLogoutInfo(TelewebJSON jsonParams, String strUserID) throws TelewebAppException
    {
        TelewebJSON jsonParam01 = new TelewebJSON(jsonParams);
        jsonParam01.setString("USER_ID", strUserID);
        jsonParam01.setString("CONECT_IP", "");
        jsonParam01.setString("PSNT_LOGIN_YN", "N");
        jsonParam01.setString("PROC_ID", strUserID);

        return mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "UPDATE_PLT_USER", jsonParam01);
    }


    /**
     * 실패건수를 업데이트 한다. (독립적 트랜젝션으로 정의)
     * 
     * @param  jsonParams 비밀번호 입력 실패 횟수 정보
     * @return            수정결과
     * @author            MPC R&D Team
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW,
                   readOnly = false)
    public TelewebJSON updateRtnFailPwd(TelewebJSON jsonParams, int strUserID, int intFailCnt) throws TelewebAppException
    {
        TelewebJSON jsonParam01 = new TelewebJSON(jsonParams);
              
        jsonParam01.setInt("REG_USER_ID", strUserID); // 비밀번호 오류가 발생하고 있는 USER_ID 
        log.debug("strUserID>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+strUserID);
        jsonParam01.setInt("PSWD_FAIL_CNT", intFailCnt + 1);
        
        /* UPDATE_PLT_USER_USER 등록자 아이디  */
        jsonParam01.setInt("USER_ID", 2); // SYSTEM 계정 USER_ID

        //5회 틀렸을 경우 잠김상태
        if((intFailCnt + 1) == 5) {
            jsonParam01.setString("PSWD_STTS", "LOCK");
        }
//        log.debug("jsonParams ==111============" + jsonParams);
        return mobjDao.update("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "UPDATE_PLT_USER_USER", jsonParam01);
    }


    public byte[] procEncDecBytes(TelewebJSON mjsonParams) throws TelewebAppException
    {
        //필수객체정의
        byte[] strRetValue = new byte[]{};             //반환파라메터생성
        //최종본 암호키  2019.01.21
        String encryptKey = "newDemoCustomTEL";
        try {
            if(mjsonParams.getString("CASE").equals("ENC")) {
                strRetValue = AES256Cipher.encrypt(mjsonParams.getString("ORG_PWD").getBytes(), encryptKey);
            }
            else if(mjsonParams.getString("CASE").equals("DEC")) {
                strRetValue = AES256Cipher.decrypt(Base64.decode(mjsonParams.getString("ENC_PWD")), encryptKey);
            }
        }
        catch(Exception e) {
            throw new TelewebAppException(e);
        }

        //최종결과값 반환
        return strRetValue;
    }


    public String createEncryptKey() throws TelewebAppException
    {
        //난수 생성으로 16자리 영문과 숫자 조합 문자열 키 정보 생성
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < 16; i++) {
            if(random.nextBoolean()) {
                sb.append((char) ((random.nextInt(26)) + 97));
            }
            else {
                sb.append((random.nextInt(10)));
            }
        }
        //실제 키 정보를 생성하여 16자리로 자르기
        String strEncryptKey;
        try {
            strEncryptKey = Base64.encode(AES256Cipher.generateKey(sb.toString() + sb.toString())).toString();
        }
        catch(Exception e) {
            throw new TelewebAppException(e);
        }
        strEncryptKey = strEncryptKey.substring(0, 32);

        return strEncryptKey;
    }


    public String keyEncString(String orgPwd) throws TelewebAppException
    {
        try {
            String encryptKey = paletteSecurityProperties.getPwdUserTerm().getEncKeyword(); //credentials??
            //String encryptKey =String.valueOf(redisTemplate.opsForValue().get("palette:sha256enc:" + userId));
            
            
            String strRetValue = AES256Cipher.encryptString(orgPwd, encryptKey);
            //String strRetValue = AES256Cipher.encryptString(orgPwd, keyVal);
            //return strRetValue;
            return strRetValue;
        }
        catch(Exception e) {
            throw new TelewebAppException(e);
        }
    }


    public String keyDecString(String encStr) throws TelewebAppException
    {
        try {
            String encryptKey = paletteSecurityProperties.getPwdUserTerm().getEncKeyword();
//            log.info("encryptKey"+encryptKey);
//            log.info("encStr"+encStr);
            String strRetValue = AES256Cipher.decryptString(encStr, encryptKey);
            return strRetValue;
        }
        catch(Exception e) {
            throw new TelewebAppException(e);
        }
    }


    public String pwdEncString(String orgPwd, String encryptKey) throws TelewebAppException
    {
        try {
            String strRetValue = AES256Cipher.encryptString(orgPwd, encryptKey);
            return strRetValue;
        }
        catch(Exception e) {
            throw new TelewebAppException(e);
        }
    }


    public String pwdDecString(String encStr, String encryptKey) throws TelewebAppException
    {
        try {
            String strRetValue = AES256Cipher.decryptString(encStr, encryptKey);
            return strRetValue;
        }
        catch(Exception e) {
            throw new TelewebAppException(e);
        }
    }
    
    /**
     * 패스워드 초기화
     * @param mjsonParams
     * @param pwd
     */
    public TelewebJSON initPswd(TelewebJSON mjsonParams) {
    	String pwd = mjsonParams.getString("LGN_ID");
    	pwd = pwd + "1!";
        String shaPwd;
        try {
            shaPwd = Hash.encryptSHA256(pwd, StandardCharsets.UTF_8);
        }
        catch(NoSuchAlgorithmException e) {
            throw new TelewebAppException(e.getLocalizedMessage());
        }
        //비번 EncryptKey 값 추출
        String strEncryptKey = createEncryptKey();
        //비번 EncryptKey 암호화 생성 
        String lastEncryptKey = keyEncString(strEncryptKey);
        //비번 sha2==> aes256
        String lastPwd = pwdEncString(shaPwd, strEncryptKey);
        //mjsonParams에 값 설정
        mjsonParams.setString("PSWD", lastPwd);
        mjsonParams.setString("ENCRYPT_KEY", lastEncryptKey);
        return mjsonParams;
    }

    
    /**
     * 채팅 사용자 추가
     * @param mjsonParams
     * @param pwd
     */
    public TelewebJSON insertChatUser(TelewebJSON mjsonParams) {
        TelewebJSON objParams = new TelewebJSON(mjsonParams);
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
    	//고객사 채팅 사용여부 체크
        objParams = mobjDao.select("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "SELECT_CUSTCO_CHAT_YN", mjsonParams);
		if(objParams.getString("CHAT_YN").equals("Y")) {
			objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "INSERT_CHAT_USER", mjsonParams);
			objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.setting.agent.dao.SettingAgentManageMapper", "INSERT_CHAT_USER_CUSTCO", mjsonParams);
		}
    	
        return mjsonParams;
    }

}
