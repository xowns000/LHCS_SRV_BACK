package kr.co.hkcloud.palette3.common.twb.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("twbUserLogBizService")
public class TwbUserLogBizServiceImpl implements TwbUserLogBizService
{
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO            twbComDAO;


    /**
     * 사용자 접속로그 정보를 기록한다.(독립적 트랜잭션 처리)
     * 
     * @param  strUserID           사용자ID
     * @param  strCase             업무구분
     * @param  strAccessIP         접속ID
     * @return                     접속로그ID
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public String insertRtn(TelewebJSON jsonParams, String strUserID, String strCase, String strAccessIP, String strLoginID) throws TelewebAppException
    {
        String strLogID = innbCreatCmmnService.getSeqNo(jsonParams, "LOG");	//로그 시컨스정보 생성
        TelewebJSON jsonParam01 = new TelewebJSON(jsonParams);
        jsonParam01.setString("LOG_ID", strLogID);
        jsonParam01.setString("USER_ID", strUserID);
        jsonParam01.setString("BIZ_DIV", strCase);
        jsonParam01.setString("CNNCT_IP", strAccessIP);
        jsonParam01.setString("ETC_INFO01", strLoginID);
        jsonParam01.setString("PROC_ID", strUserID);
        jsonParam01.setString("WRTR_ID", strUserID);

        twbComDAO.insert("com.hcteletalk.teletalk.mng.bas.bas08.dao.Bas08Mapper", "INSERT_TWB_BAS08", jsonParam01);

        return strLogID;
    }


    /**
     * 비밀번호 변경 이력을 입력한다.
     * 
     * @param  jsonParams
     * @param  objDAO              TelewebComDAO 객체
     * @return                     등록결과
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public String insertRtnChangePwdInfo(TelewebJSON jsonParams, String strAccessIP) throws TelewebAppException
    {
       
        TelewebJSON jsonParam01 = new TelewebJSON(jsonParams);
        
        int GEN_PSWD_LOG_ID = innbCreatCmmnService.createSeqNo("PSWD_LOG_ID");  //로그 시컨스정보 생성
        String pswd_log_id = String.valueOf(GEN_PSWD_LOG_ID);
        
        jsonParam01.setInt("PSWD_LOG_ID", GEN_PSWD_LOG_ID);
        
        // 수정할 사용자ID
        jsonParam01.setString("USER_ID", jsonParams.getString("RESET_USER_ID"));
        // 수정하는 사용자의 ID
        jsonParam01.setString("RGTR_ID", jsonParams.getString("USER_ID"));
        
        if(jsonParams.getString("TASK_SE_CD").isEmpty()) {
        	jsonParam01.setString("TASK_SE_CD", "CHANGE");
        }else {
        	jsonParam01.setString("TASK_SE_CD", jsonParams.getString("TASK_SE_CD"));
        }
        
        jsonParam01.setString("CNTN_IP", strAccessIP);

        twbComDAO.insert("kr.co.hkcloud.palette3.common.log.dao.AccesLogCmmnMapper", "INSERT_PLT_USER_PSWD_LOG", jsonParam01);
        
        return pswd_log_id;
    }


    /**
     * 비즈로그 입력한다.
     * 
     * @param  jsonParams
     * @param  objDAO              TelewebComDAO 객체
     * @return                     등록결과
     * @throws TelewebAppException
     */
    @Transactional(readOnly = false)
    public String insertUserBizLog(TelewebJSON jsonParams, String strAccessIP) throws TelewebAppException
    {
        String strLogID = innbCreatCmmnService.getSeqNo(jsonParams, "LOG");	//로그 시컨스정보 생성
        TelewebJSON jsonParam01 = new TelewebJSON(jsonParams);
        jsonParam01.setString("LOG_ID", strLogID);
        jsonParam01.setString("USER_ID", jsonParams.getString("USER_ID"));
        jsonParam01.setString("PROC_ID", jsonParams.getString("PROC_ID"));
        jsonParam01.setString("BIZ_DIV", jsonParams.getString("BIZ_DIV"));
        jsonParam01.setString("ETC_INFO01", jsonParams.getString("URI"));
        jsonParam01.setString("ETC_INFO02", jsonParams.getString("MENU_ID"));
        jsonParam01.setString("WRTR_DEPT_CD", jsonParams.getString("WRTR_DEPT_CD"));
        jsonParam01.setString("WRTR_ID", jsonParams.getString("WRTR_ID"));
        jsonParam01.setString("ETC_INFO03", jsonParams.getString("ETC_INFO03"));

        jsonParam01.setString("CNNCT_IP", strAccessIP);

        twbComDAO.insert("kr.co.hkcloud.palette3.common.log.dao.AccesLogCmmnMapper", "INSERT_PLT_PWD_LOG", jsonParam01);
        return strLogID;
    }
}
