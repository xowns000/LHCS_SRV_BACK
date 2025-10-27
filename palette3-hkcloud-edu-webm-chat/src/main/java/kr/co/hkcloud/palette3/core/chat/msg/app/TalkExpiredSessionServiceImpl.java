package kr.co.hkcloud.palette3.core.chat.msg.app;


import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbSystemMessage;
import kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgDataDAO;
import kr.co.hkcloud.palette3.core.chat.redis.dao.TalkRedisChatInoutRepository;
import kr.co.hkcloud.palette3.core.chat.stomp.app.TalkStompSendToAgentService;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.ChatEvent;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.ChatType;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.MessageEvent;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("talkExpiredSessionService")
public class TalkExpiredSessionServiceImpl implements TalkExpiredSessionService
{
    private final InnbCreatCmmnService         innbCreatCmmnService;
    private final TwbComDAO                    mobjDao;
    private final TalkRedisChatInoutRepository chatInoutRepository;
    private final TalkStompSendToAgentService  stompSendToAgentService;
    private final TalkMsgDataDAO               msgDataDao;


    /**
     * 세션만료 UPDATE 처리
     * 
     * @param TelewebJSON objParams
     * @param JSONObject  rcvJson
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    @Override
    public void updadateExpiredSessionReady(TelewebJSON objParams) throws TelewebAppException
    {
        log.trace("updadateExpiredSessionReady :::");

        //상태를 09,10,11,13 -> 90,91,92,93 변경 저장 (고객의 접수포기, 대기포기, 전달대기포기)
        mobjDao
            .update("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkExpiredSessionMapper", "UPDATE_EXPIRED_SESSION_READY", objParams);
    }


    /**
     * 고객의 접수포기, 대기포기, 전달대기포기 건을 세션만료
     * 
     * @param TelewebJSON objParams
     * @param String      calledApi
     * @param JSONObject  rcvJson
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    @Override
    public void processExpiredSessionReady(TelewebJSON objParams, String calledApi, JSONObject rcvJson) throws TelewebAppException
    {
        log.trace("processExpiredSessionReady :::");

        //고객 포기 세션 만료 건 조회
        TelewebJSON objSelReadyParams = mobjDao
            .select("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkExpiredSessionMapper", "SELECT_EXPIRED_SESSION_READY", objParams);
        if(objSelReadyParams != null && objSelReadyParams.getHeaderInt("COUNT") > 0) {
            String callTypCd = objSelReadyParams.getString("CHN_TYPE_CD");
            String consultAutoEndToCustomer = HcTeletalkDbSystemMessage.getInstance()
                .getStringBySystemMsgId(((JSONObject) (objParams.getDataObject().get(0)))
                    .getString("CUSTCO_ID"), "18"); // 시스템메시지 DB로 변경
            rcvJson.put("consultAutoEndToCustomer", consultAutoEndToCustomer);
            rcvJson.put("call_typ_cd", callTypCd);

            TelewebJSON inJson = new TelewebJSON();

            //상담이력 ID를 가져온다.
            String strBizCase = "TLK";
            inJson.setString("CHT_RDY_ID", objSelReadyParams.getString("CHT_RDY_ID"));
            inJson.setString("TALK_USER_KEY", objSelReadyParams.getString("CHT_USER_KEY"));
            inJson.setString("CHT_USER_KEY", objSelReadyParams.getString("CHT_USER_KEY"));
            inJson.setString("CUSTCO_ID", objSelReadyParams.getString("CUSTCO_ID"));
            inJson.setString("SNDR_KEY", objSelReadyParams.getString("SNDR_KEY"));
            inJson.setString("USER_ID", objSelReadyParams.getString("USER_ID"));
            inJson.setString("TALK_API_CD", calledApi);
            inJson.setString("TYPE", "system");
            inJson.setString("CONTENT", consultAutoEndToCustomer);

            //상담대기 상세에 세션만료 INSERT
            inJson.setString("CHT_USER_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_USER_HSTRY_ID")));
            mobjDao.insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "insertTalkUserReadyDetail", inJson);

            //상담이력으로 이동하여 상태를 90,91,92,93 변경 저장 (고객의 접수포기, 대기포기, 전달대기포기)
            //고객대기에서 상담이력으로 데이터 이관한다
//            inJson.setString("CHT_CUTT_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_ID")));
//            mobjDao.insert("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkExpiredSessionMapper", "INSERT_TALK_USER_READY_TO_CONTACT", inJson);
            //신규 고객포기건 생성 => 인입 시 건수 생성/해당 건수 포기건으로 update
            mobjDao.update("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkExpiredSessionMapper", "UPDATE_TALK_USER_READY_TO_CONTACT", inJson);

            //고객대기에서 대기이력테이블에 데이터 저장
            //mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertRtnTalkReadyHist", inJson);

            //고객대기에서 해당 데이터 삭제
            mobjDao.delete("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "deleteRtnTalkReadyInfo", inJson);

            //상담이력상세에 저장
            inJson.setString("CHT_CUTT_DTL_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID")));
            mobjDao.insert("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "insertRtnTalkHistDetailInfo", inJson);

            //고객대기상세에서 해당 데이터 삭제
            //mobjDao.delete("kr.co.hkcloud.palette3.chat.main.dao.ChatMainMapper", "deleteRtnTalkReadyDetailInfo", inJson);

            TelewebJSON telewebJSON = new TelewebJSON();
            telewebJSON.setHeader("called_api", calledApi);
            telewebJSON.setHeader("code", 0);
            telewebJSON.setHeader("ERROR_FLAG", false);
            telewebJSON.setHeader("ERROR_MSG", "");
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(0, rcvJson);
            telewebJSON.setDataObject(jsonArray);

            //READY:EXPIRED_SESSION_CUST:SYSTEM [PUB] - 대기중 고객세션만료
            log.trace(">>> READY:EXPIRED_SESSION_CUST:SYSTEM [PUB] - 대기중 고객세션만료");
            stompSendToAgentService.sendToReadyAgent(ChatMessage.builder().chatType(ChatType.INOUT)
                .chatEvent(ChatEvent.EXPIRED_SESSION_CUST).messageEvent(MessageEvent.SYSTEM)
                .userId(objSelReadyParams.getString("USER_ID")).userKey(objSelReadyParams.getString("CHT_USER_KEY"))
                .custcoId(objSelReadyParams.getString("CUSTCO_ID")).build(), telewebJSON);
        }
    }


    /**
     * 상담중인 건을 세션만료
     * 
     * @param TelewebJSON objParams
     * @param String      calledApi
     * @param JSONObject  rcvJson
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    @Override
    public void processExpiredSessionContract(TelewebJSON objParams, String calledApi, JSONObject rcvJson) throws TelewebAppException
    {
        //상담중인 건을 만료 시킨다
        TelewebJSON objSelParams = msgDataDao.selectTalkContactId(objParams); //12 조회
        if(objSelParams != null && objSelParams.getHeaderInt("COUNT") > 0) {
            String callTypCd = objSelParams.getString("CALL_TYP_CD");
            rcvJson.put("call_typ_cd", callTypCd);

            objParams.setString("CHT_CUTT_ID", objSelParams.getString("CHT_CUTT_ID"));
            objParams.setString("SNDR_KEY", objSelParams.getString("SNDR_KEY"));
//            objParams.setString("USER_ID", objSelParams.getString("USER_ID"));
            objParams.setString("USER_ID", "2");

            // 카카오에서 채팅방 종료에 대해서 아래의 파라미터는 보내주지 않는다.
            objParams.setString("CHT_CUTT_DTL_ID", objParams.getString("CHT_CUTT_DTL_ID"));
            objParams.setString("RCPTN_DSPTCH_CD", "SND");
            objParams.setString("RCPTN_SNDPTY_ID", "2");
            objParams.setString("MSG_TYPE_CD", calledApi.replace("/", ""));
            objParams.setString("TYPE", calledApi.replace("/", ""));
            objParams.setString("CONTENT", HcTeletalkDbSystemMessage.getInstance()
                .getStringBySystemMsgId(((JSONObject) (objParams.getDataObject().get(0)))
                    .getString("CUSTCO_ID"), "19"));
            objParams.setString("SYS_MSG_ID", "19");
            
            if(!"EMAIL".equals(callTypCd)) {
                if(!objSelParams.getString("SYS_MSG_ID").equals("2")) {
                    msgDataDao.insertCntRcvMsg(objParams);
                }
            }
            
            //해당 상담중 건의 커넥션이 존재하나?
            //INOUT:CHECK - 세션만료
            String userKey = rcvJson.getString("user_key");
            String strPostTalkStat = getPostTalkStat(objSelParams);

            TelewebJSON telewebJSON = new TelewebJSON();
            telewebJSON.setHeader("called_api", calledApi);
            telewebJSON.setHeader("code", 0);
            telewebJSON.setHeader("ERROR_FLAG", false);
            telewebJSON.setHeader("ERROR_MSG", "");
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(0, rcvJson);
            telewebJSON.setDataObject(jsonArray);
            telewebJSON.setString("TALK_SERIAL_NUMBER", objParams.getString("TALK_SERIAL_NUMBER")); // 메시지 READ 여부 업데이트를 위해 SERIAL 값을 넘김.
            telewebJSON.setString("CHT_CUTT_DTL_ID", objParams.getString("CHT_CUTT_DTL_ID")); // 메시지 READ 여부 업데이트를 위해 SERIAL 값을 넘김.
            telewebJSON.setString("TALK_CONTACT_ID", objSelParams.getString("TALK_CONTACT_ID")); // 메시지 READ 여부 업데이트를 위해 SERIAL 값을 넘김.
            telewebJSON.setString("CHT_CUTT_ID", objSelParams.getString("CHT_CUTT_ID")); // 메시지 READ 여부 업데이트를 위해 SERIAL 값을 넘김.
            telewebJSON.setString("TALK_STAT_CD", strPostTalkStat); // 20,21 후처리
            telewebJSON.setString("CHT_STTS_CD", strPostTalkStat); // 20,21 후처리

            // 서버에서 처리하도록 수정          	
            TelewebJSON inJson = new TelewebJSON();

            inJson.setString("CHT_CUTT_ID", objSelParams.getString("CHT_CUTT_ID"));
            inJson.setString("CUSTCO_ID", objSelParams.getString("CUSTCO_ID"));
            inJson.setString("TALK_STAT_CD", strPostTalkStat); // 상담결과가 저장되어잇다면 상담완료로 상태 변경함 ( SJH , TOBE MODEL ) 
            inJson.setString("CHT_STTS_CD", strPostTalkStat); // 상담결과가 저장되어잇다면 상담완료로 상태 변경함 ( SJH , TOBE MODEL ) 
            
            msgDataDao.updateTalkContactPost(inJson); //20 or 21

            boolean blnInout = chatInoutRepository.hasKey(userKey);
            if(blnInout) {
                //INOUT:EXPIRED_SESSION_CUST:QUIT [PUB] - 상담중 고객세션만료
                log.trace(">>> INOUT:EXPIRED_SESSION_CUST:QUIT [PUB] - 상담중 고객세션만료");
                stompSendToAgentService.sendToAgent(ChatMessage.builder().chatType(ChatType.INOUT)
                    .chatEvent(ChatEvent.EXPIRED_SESSION_CUST).messageEvent(MessageEvent.SYSTEM)
                    .userId(objSelParams.getString("USER_ID")).userKey(userKey)
                    .custcoId(objSelParams.getString("CUSTCO_ID")).build(), telewebJSON);
            }
        }

    }


    /**
     * 변경 될 상담상태값
     * 
     * @param TelewebJSON objParams
     * @param String      calledApi
     * @param JSONObject  rcvJson
     */
    @Transactional(propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    private String getPostTalkStat(TelewebJSON objSelParams)
    {

        String talkStatCd = "AFTPRCS";
        if(objSelParams.getString("PRCS_RSLT_CD") != null && !("")
            .equals(objSelParams.getString("PRCS_RSLT_CD")) && !("TRAN_WAIT").equals(objSelParams.getString("PRCS_RSLT_CD"))	 // 전달
            && !("CLBK_WAIT").equals(objSelParams.getString("PRCS_RSLT_CD"))	 // 콜백	 
        ) {
            talkStatCd = "CMPL";
        }

        return talkStatCd;
    }
}
