package kr.co.hkcloud.palette3.core.chat.msg.app;


import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.chat.setting.util.ChatSettingBannedWordUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("talkMsgDataProcessService")
public class TalkMsgDataProcessServiceImpl implements TalkMsgDataProcessService
{
    private final TwbComDAO mobjDao;
    private final ChatSettingBannedWordUtils chatSettingBannedWordUtils;

//    /**
//     * 세션만료 처리
//     * @param TelewebJSON objParams
//     * @param String      calledApi
//     * @param JSONObject  rcvJson
//     */
//    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = { Exception.class, SQLException.class }, readOnly = false)
//    public void processExpiredSession(TelewebJSON objParams, String calledApi, JSONObject rcvJson) throws TelewebAppException
//    {
//        log.trace("processExpiredSession :::");
//        
//        //고객접수, 대기 건을 만료 시킨다 09,10,11,13 조회
//        TelewebJSON objSelReadyParams = msgDataDao.selectTalkUserReady(objParams);
//        if (objSelReadyParams != null && objSelReadyParams.getHeaderInt("COUNT") > 0)
//        {
//            String consultAutoEndToCustomer = HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId("18"); // 시스템메시지 DB로 변경
//            rcvJson.put("consultAutoEndToCustomer", consultAutoEndToCustomer);
//
//            TelewebJSON inJson = new TelewebJSON();
//
//            //상담이력 ID를 가져온다.
//            String strBizCase = "TALK";
//            String strContactId = comBiz.getSeqNo(inJson, strBizCase);
//            inJson.setString("TALK_CONTACT_ID", strContactId);
//            inJson.setString("TALK_USER_KEY", objSelReadyParams.getString("TALK_USER_KEY"));
//            inJson.setString("SNDR_KEY", objSelReadyParams.getString("SNDR_KEY"));
//            inJson.setString("USER_ID", objSelReadyParams.getString("USER_ID"));
//            inJson.setString("TALK_SERIAL_NUMBER", "9999999999999999999");
//            inJson.setString("TALK_API_CD", calledApi);
//            inJson.setString("TYPE", "system");
//            inJson.setString("CONTENT", consultAutoEndToCustomer);
//            inJson.setString("PROC_ID", "SYSTEM");
//
//            //상담대기 상세에 세션만료 INSERT
//            mobjDao.insert("com.hcteletalk.teletalk.core.router.dao.TalkRouteMapper", "insertTalkUserReadyDetail", inJson);
//
//            //상담이력으로 이동하여 상태를 09,10,11,13 -> 90,91,92,93 변경 저장 (고객의 접수포기, 대기포기, 전달대기포기)
//            //고객대기에서 상담이력으로 데이터 이관한다
//            mobjDao.insert("kr.co.hkcloud.palette.core.chat.msg.dao.TalkMsgMapper", "INSERT_TALK_USER_READY_TO_CONTACT", inJson);
//
//            //고객대기에서 대기이력테이블에 데이터 저장
//            mobjDao.insert("kr.co.hkcloud.palette.chat.main.dao.ChatMainMapper", "insertRtnTalkReadyHist", inJson);
//
//            //고객대기에서 해당 데이터 삭제
//            mobjDao.delete("kr.co.hkcloud.palette.chat.main.dao.ChatMainMapper", "deleteRtnTalkReadyInfo", inJson);
//
//            //상담이력상세에 저장
//            mobjDao.insert("kr.co.hkcloud.palette.chat.main.dao.ChatMainMapper", "insertRtnTalkHistDetailInfo", inJson);
//
//            //고객대기상세에서 해당 데이터 삭제
//            mobjDao.delete("kr.co.hkcloud.palette.chat.main.dao.ChatMainMapper", "deleteRtnTalkReadyDetailInfo", inJson);
//
//            TelewebJSON telewebJSON = new TelewebJSON();
//            telewebJSON.setHeader("called_api", calledApi);
//            telewebJSON.setHeader("code", 0);
//            telewebJSON.setHeader("ERROR_FLAG", false);
//            telewebJSON.setHeader("ERROR_MSG", "");
//            JSONArray jsonArray = new JSONArray();
//            jsonArray.add(0, rcvJson);
//            telewebJSON.setDataObject(jsonArray);
//
//            //READY:EXPIRED_SESSION_CUST:SYSTEM [PUB] - 대기중 고객세션만료
//            log.trace(">>> READY:EXPIRED_SESSION_CUST:SYSTEM [PUB] - 대기중 고객세션만료");
//            stompSendToAgentService.sendToReadyAgent(ChatMessage.builder()
//                                                                .chatType(ChatType.READY)
//                                                                .chatEvent(ChatEvent.EXPIRED_SESSION_CUST)
//                                                                .messageEvent(MessageEvent.SYSTEM)
//                                                                .userId(objSelReadyParams.getString("USER_ID"))
//                                                                .userKey(objSelReadyParams.getString("TALK_USER_KEY"))
//                                                                .build()
//                                                    , telewebJSON);
//        }
//
//        //상담중인 건을 만료 시킨다
//        TelewebJSON objSelParams = msgDataDao.selectTalkContactId(objParams); //12 조회
//        if (objSelParams != null && objSelParams.getHeaderInt("COUNT") > 0)
//        {
//            objParams.setString("TALK_CONTACT_ID", objSelParams.getString("TALK_CONTACT_ID"));
//            objParams.setString("SNDR_KEY", objSelParams.getString("SNDR_KEY"));
//            objParams.setString("USER_ID", objSelParams.getString("USER_ID"));
//
//            // 카카오에서 채팅방 종료에 대해서 아래의 파라미터는 보내주지 않는다.
//            objParams.setString("TALK_SERIAL_NUMBER", "9999999999999999999");
//            objParams.setString("SNDRCV_CD", "SND");
//            objParams.setString("TYPE", calledApi.replace("/", ""));
//            objParams.setString("CONTENT", HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId("20180403040958193MSG34896"));
//            objParams.setString("PROC_ID", "SYSTEM");
//
//            msgDataDao.insertCntRcvMsg(objParams);
//
//            //해당 상담중 건의 커넥션이 존재하나?
//            //INOUT:CHECK - 세션만료
//            String userKey = rcvJson.getString("user_key");
//            boolean blnInout = chatInoutRepository.hasKey(userKey);
//            if (blnInout)
//            {
//                TelewebJSON telewebJSON = new TelewebJSON();
//                telewebJSON.setHeader("called_api", calledApi);
//                telewebJSON.setHeader("code", 0);
//                telewebJSON.setHeader("ERROR_FLAG", false);
//                telewebJSON.setHeader("ERROR_MSG", "");
//                JSONArray jsonArray = new JSONArray();
//                jsonArray.add(0, rcvJson);
//                telewebJSON.setDataObject(jsonArray);
//                telewebJSON.setString("TALK_SERIAL_NUMBER", objParams.getString("TALK_SERIAL_NUMBER")); // 메시지 READ 여부 업데이트를 위해 SERIAL 값을 넘김.
//                
//                //INOUT:EXPIRED_SESSION_CUST:QUIT [PUB] - 상담중 고객세션만료
//                log.trace(">>> INOUT:EXPIRED_SESSION_CUST:QUIT [PUB] - 상담중 고객세션만료");
//                stompSendToAgentService.sendToAgent(ChatMessage.builder()
//                                                               .chatType(ChatType.INOUT)
//                                                               .chatEvent(ChatEvent.EXPIRED_SESSION_CUST)
//                                                               .messageEvent(MessageEvent.SYSTEM)
//                                                               .userId(objSelParams.getString("USER_ID"))
//                                                               .userKey(userKey)
//                                                               .build()
//                                                    ,  telewebJSON);
//            }
//            else
//            {
//                TelewebJSON inJson = new TelewebJSON();
//                inJson.setString("TALK_CONTACT_ID", objSelParams.getString("TALK_CONTACT_ID"));
//                msgDataDao.updateTalkContactPost(inJson); //20
//            }
//        }
//    }


    /**
     * 발신메세지 저장
     * 
     * @param  TelewebJSON
     * @return             TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional
    public TelewebJSON insertSndMsg(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        log.info("123123SDSADASD" + mjsonParams);
        //금칙어 변환
        String msg = chatSettingBannedWordUtils.parseContent_3(mjsonParams.getString("CONTENT"), mjsonParams.getString("CUSTCO_ID"));
        mjsonParams.setString("CHG_CONTENT",msg);
        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "INSERT_PLT_CHT_CUTT_DTL", mjsonParams);

        return objRetParams;
    }


    /**
     * 상담사가 메시지를 보냈을 경우 고객 무응답 메시지 시간 0으로 초기화
     * 
     * @Transactional             Auto Commit
     * @param         TelewebJSON
     * @return                    TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional
    public TelewebJSON updateCustNoRespIs0(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "UPDATE_TWB_TALK_CONTACT_CUSTNORESP_INIT", mjsonParams);

        return objRetParams;
    }


    /**
     * 상담이력의 상담중 건을 상담사 비정상 종료건 상태로 업데이트
     * 
     * @param  TelewebJSON
     * @return             TelewebJSON 형식의 처리 결과 데이터
     * @author             SJH , 2018.10.10
     */
    @Transactional
    public TelewebJSON updateTalkContactStatusCd(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.update("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "UPDATE_TALK_CONTACT_STATUS_CD", mjsonParams);

        return objRetParams;
    }


    /**
     * 상담원이 무응답인 메시지를 1분내의 메시지 조회 한다.
     * 
     * @param  TelewebJSON
     * @return             TelewebJSON 형식의 처리 결과 데이터
     */
    @Transactional(readOnly = true)
    public TelewebJSON selectAgentNoResponseTalkContactId(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.select("kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgMapper", "SELECT_TALK_USER_TALKING", mjsonParams);

        return objRetParams;
    }

}
