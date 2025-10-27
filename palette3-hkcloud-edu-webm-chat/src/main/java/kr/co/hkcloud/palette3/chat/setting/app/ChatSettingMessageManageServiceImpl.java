package kr.co.hkcloud.palette3.chat.setting.app;


import java.sql.SQLException;

import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbSystemMessage;
import kr.co.hkcloud.palette3.core.chat.router.app.RoutingToAgentManagerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("chatSettingMessageManageService")
public class ChatSettingMessageManageServiceImpl implements ChatSettingMessageManageService
{
    private final InnbCreatCmmnService innbCreatCmmnService;
    private final TwbComDAO            mobjDao;
    private final RoutingToAgentManagerService routingToAgentManagerService;
    //private final TwbTalkPropDAO talkPropDAO;


    /**
     * 고객무응답 전송 후 종료 값 조회
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectRtnCnslProp(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingMessageManageMapper", "selectRtnCnslProp", jsonParams);
    }


    /**
     * 고객접수 시스템 메시지 조회 3. 상담종료 메시지
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectRtnSystemMsgByMsgID(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingMessageManageMapper", "selectRtnSystemMsgByMsgID", jsonParams);
    }


    /**
     * 고객무응답 메시지 조회
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectRtnCustMsg(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingMessageManageMapper", "selectRtnCustMsg", jsonParams);
    }


    /**
     * 메시지 중복 조회
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectRtnCustMsgDuplicatedCnt(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingMessageManageMapper", "selectRtnCustMsgDuplicatedCnt", jsonParams);
    }


    /**
     * 메시지 신규등록
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON insertRtnCnslMsg(TelewebJSON jsonParams) throws TelewebAppException
    {      
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        String sysMsgId = jsonParams.getString("BF_MSG_HR");
        if(sysMsgId.equals("-1")) {
            //추가
            objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingMessageManageMapper", "insertRtnCnslMsg", jsonParams);
        }
        else {
            //수정
            objRetParams = mobjDao.update("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingMessageManageMapper", "updateRtnCnslMsg", jsonParams);
        }

        //시스템메시지 Redis에 설정
        TelewebJSON sysMsgJson = routingToAgentManagerService.selectSystemMessage();
        HcTeletalkDbSystemMessage.getInstance().setSystemMessage(sysMsgJson);

        return objRetParams;
    }


    /**
     * 메시지 삭제
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON deleteRtnCnslMsg(TelewebJSON jsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(jsonParams);
        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingMessageManageMapper", "deleteRtnCnslMsgByCustco", jsonParams);
        
        return objRetParams;
    }

    /**
     * 상담설정 정보 업데이트
     * 
     * @Override
     * @Transactional(readOnly = false) public TelewebJSON updateRtnCnslProp(TelewebJSON objRetParams, TelewebJSON jsonParams) throws TelewebAppException { JSONArray objArry = jsonParams.getDataObject(); String strEnvCd
     *                             = ""; //환경설정코드 String strEnvSetVal = ""; //환경설정값 String strEnvCdNm = ""; //환경설정값 설명 int objArrSize = objArry.size();
     * 
     *                             // 상담가능 허용 일괄 건수 , 여부 String contChatagentYn = null; String contChatagentCnt = null;
     * 
     *                             //배열 사이즈만큼 처리 if (!objArry.isEmpty()) { for (int i = 0; i < objArrSize; i++) { //환경설정코드, 환경설정값을 가져온다. strEnvCd = objArry.getJSONObject(i).getString("STNG_CD"); strEnvSetVal =
     *                             objArry.getJSONObject(i).getString("STNG_VL"); strEnvCdNm = objArry.getJSONObject(i).getString("STNG_CD_NM");
     * 
     *                             if (strEnvCd.equals("CONT_CHATAGENT_YN")) contChatagentYn = strEnvSetVal; if (strEnvCd.equals("CONT_CHATAGENT_CNT")) contChatagentCnt = strEnvSetVal;
     * 
     *                             //환경설정코드, 환경설정값을 세팅한다. jsonParams.setString("STNG_CD", strEnvCd); jsonParams.setString("STNG_VL", strEnvSetVal); jsonParams.setString("STNG_CD_NM", strEnvCdNm != null ? strEnvCdNm :
     *                             "");
     * 
     *                             objRetParams = talkPropDAO.updateRtnCnslProp(jsonParams); } }
     * 
     *                             // 상담가능허용수 일괄적용 if (contChatagentYn != null && contChatagentYn.equals("Y")) { jsonParams.setString("MAX_CHAT_AGENT", contChatagentCnt); objRetParams =
     *                             talkPropDAO.updateAllMaxAgent(jsonParams); }
     * 
     *                             return objRetParams; }
     */


    /**
     * 시스템 메시지 중복 체크
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON selectRtnCustcoMsgDuplicatedCnt(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.chat.setting.dao.ChatSettingSystemMessageManageMapper", "selectRtnCustcoMsgDuplicatedCnt", jsonParams);
    }

}
