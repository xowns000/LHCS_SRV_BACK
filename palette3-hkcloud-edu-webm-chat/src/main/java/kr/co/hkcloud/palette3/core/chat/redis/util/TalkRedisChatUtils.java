package kr.co.hkcloud.palette3.core.chat.redis.util;


import org.springframework.stereotype.Component;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.properties.chat.ChatProperties;
import kr.co.hkcloud.palette3.core.chat.msg.app.TalkMsgDataProcessService;
import kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgDataDAO;
import kr.co.hkcloud.palette3.core.chat.redis.dao.TalkRedisChatInoutRepository;
import kr.co.hkcloud.palette3.core.chat.redis.dao.TalkRedisChatReadyRepository;
import kr.co.hkcloud.palette3.core.chat.stomp.app.TalkStompSendToAgentService;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.ChatEvent;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.ChatType;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.MessageEvent;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebUtilException;
import kr.co.hkcloud.palette3.file.domain.FileResponse.FilePropertiesResponse;
import kr.co.hkcloud.palette3.file.enumer.RepositoryPathTypeCd;
import kr.co.hkcloud.palette3.file.enumer.RepositoryTaskTypeCd;
import kr.co.hkcloud.palette3.file.util.FileRulePropertiesUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Component
public class TalkRedisChatUtils
{
    private final FileRulePropertiesUtils      fileRulePropertiesUtils;
    private final ChatProperties               chatProperties;
    private final TalkMsgDataProcessService    msgDataProcess;
    private final TalkStompSendToAgentService  stompSendToAgentService;
    private final TalkRedisChatReadyRepository chatReadyRepository;
    private final TalkRedisChatInoutRepository chatInoutRepository;
    private final TalkMsgDataDAO               msgDataDao;


    /**
     * 상담사에게 메시지 전달
     * 
     * @param  calledApi
     * @param  userKey
     * @param  telewebJSON
     * @param  objParams
     * @return                      Boolean
     * @throws TelewebUtilException
     */
    public Boolean isSendSocketToAgent(String calledApi, String userKey, TelewebJSON telewebJSON, TelewebJSON objParams) throws TelewebUtilException
    {
        String custcoId = objParams.getString("CUSTCO_ID");

        log.info("===>" + userKey + ":///////////////////////////////////상담사에게 메시지 전달//////////////////////////////////////////////");
        log.info("===>" + userKey + ":///////////////////////////////////////////////////////////////////////////////////////////////////////");
        log.info("===>" + userKey + ":calledApi : " + calledApi);
        log.info("===>" + userKey + ":userKey : " + userKey);
        log.info("===>" + userKey + ":telewebJSON : " + telewebJSON);
        log.info("===>" + userKey + ":objParams : " + objParams);
        log.info("===>" + userKey + ":///////////////////////////////////////////////////////////////////////////////////////////////////////");
        
        boolean isAgentReadyToContact = isAgentReadyToContact(userKey);
        log.info("===>" + userKey + ":isSendSocketToAgent userKey : " + isAgentReadyToContact);
        //상담사가 상담중 요청중인 지 체크
        if(isAgentReadyToContact) {
            boolean existContact = false;   // 상담테이블에 데이타가 잇는냐..

            // message 만 상담사에게 전달한다.
            // message 만 상담을 저장한다, .
            if("/message".equals(calledApi)) {
                TelewebJSON objSelParams = msgDataDao.selectTalkContactId(objParams);

                log.info("===>" + userKey + ":isSendSocketToAgent /message objSelParams.getSize() : {}", objSelParams.getSize());

                if(objSelParams != null && objSelParams.getSize() > 0) // 상담중(contact)n
                {
                    log.info("===>" + userKey + ":isSendSocketToAgent /message 상담중 - 수신 메세지 저장 : {}", objSelParams );

                    objParams.setString("CHT_CUTT_ID", objSelParams.getString("CHT_CUTT_ID"));
                    objParams.setString("USER_ID", objSelParams.getString("CUSL_ID"));
                    objParams.setString("CUSL_ID", objSelParams.getString("CUSL_ID"));
                    objParams.setString("RCPTN_DSPTCH_CD", "RCV");

                    // 수신 메세지 저장
                    log.info("수신메시지 저장 전 objParams ===> " + objParams);
                    msgDataDao.insertCntRcvMsg(objParams);

                    telewebJSON.setString("CALL_TYP_CD", objSelParams.getString("CALL_TYP_CD"));
                    telewebJSON.setString("TALK_SERIAL_NUMBER", objParams.getString("TALK_SERIAL_NUMBER")); // 메시지 READ 여부 업데이트를 위해 SERIAL 값을 넘김.
                    telewebJSON.setString("TALK_CONTACT_ID", objSelParams.getString("TALK_CONTACT_ID"));    // tobe -vue        
                    telewebJSON.setString("ORG_CONT_ID", objParams.getString("ORG_CONT_ID"));               // tobe -vue       
                    telewebJSON.setString("SYS_MSG", objParams.getString("SYS_MSG"));                       // tobe -vue
                    telewebJSON.setString("CUSTCO_ID", custcoId);

                    // [파일o] 상담사에게 메시지 전달: 채팅-이미지(고객)
                    final RepositoryTaskTypeCd taskTypeCd = RepositoryTaskTypeCd.chat;    //채팅
                    final RepositoryPathTypeCd pathTypeCd = RepositoryPathTypeCd.images;  //이미지(고객)
                    final FilePropertiesResponse fileProperties = fileRulePropertiesUtils.getProperties(taskTypeCd, pathTypeCd);

                    // 파일 BLOB 처리이면
                    switch(fileProperties.getTrgtTypeCd())
                    {
                        case DB:
                        {
                            telewebJSON.setString("ORG_FILE_ID", objParams.getString("ORG_FILE_ID"));           // BLOB 파일 키
                            break;
                        }
                        default:
                        {
                            break;
                        }
                    }

                    existContact = true;
                }
                else {      //처음 들어온 채팅
                    log.info("===>" + userKey + ":isSendSocketToAgent /message ready 테이블에서 상담원 id만 조회 : {}", objSelParams );
                    
                    // ready 테이블에서 상담원 id만 조회
                    objSelParams = msgDataDao.selectTalkUserReady(objParams);
                    
                    existContact = false;
                }

                if(objSelParams != null && objSelParams.getSize() > 0) // 상담중(contact)n ,ready 상태에서도 메시지 전송 
                {
                    if(objSelParams.getString("USER_ID").equals("") || objSelParams.getString("USER_ID") == null) {
                        objSelParams.setString("USER_ID", objSelParams.getString("CUSL_ID"));
                    }
                    //INOUT:IN_OUT:TALK [PUB] - 상담중 고객이 보낸 메시지 
                    log.info("===>" + userKey + ":isSendSocketToAgent INOUT:IN_OUT:TALK [PUB] - 상담중 고객이 보낸 메시지" + objSelParams);
                    stompSendToAgentService.sendToAgent(ChatMessage.builder().chatType(ChatType.INOUT).chatEvent(ChatEvent.IN_OUT).messageEvent(MessageEvent.TALK).userId(objSelParams.getString("USER_ID"))
                        .userKey(userKey).custcoId(objSelParams.getString("CUSTCO_ID")).build(), telewebJSON);
                }

            }
            return existContact;
        }
        //상담사가 상담중 요청중인 지 체크 , db 체크 , 2018.10.08 SJH   /////////////////////////////////////// 
        else {
            log.info("===>" + userKey + "************ isAgentReadyToContact websocket Killed userKey={}", userKey);
            // message 만 상담을 저장한다, .
            if("/message".equals(calledApi)) {
                // DB에 상담중인 데이타가 있는지 체크 한다. (상담원 무응답 1분까지 체크함) 
                TelewebJSON objAgent = msgDataProcess.selectAgentNoResponseTalkContactId(objParams);
                if(objAgent.getSize() > 0) // 상담중이고 DB 조회해서 무응답 대기가 있는 경우 ...
                {
                    TelewebJSON objSelParams = msgDataDao.selectTalkContactId(objParams);
                    objParams.setString("CHT_CUTT_ID", objSelParams.getString("CHT_CUTT_ID"));
                    objParams.setString("USER_ID", objSelParams.getString("CUSL_ID"));
                    objParams.setString("CUSL_ID", objSelParams.getString("CUSL_ID"));
                    objParams.setString("RCPTN_DSPTCH_CD", "RCV");
                    // 수신 메세지 저장
                    objParams.setString("QSTN_TYPE_ID", objSelParams.getString("QSTN_TYPE_ID"));

                    // 환경 변수값으로 response timeout 설정값을 가지고 온다. 제한 시간을 초과 한경우 신규 배정로직으로 전환 , 더이상 기다리지 않는다. 
                    // Properties env 사용시 1로 디폴트
                    int agentTime = chatProperties.getAgentNonResponseTimeout();
                    if(objAgent.getString("AGENT_NONRESPONSE_TIME") != null && !"".equals(objAgent.getString("AGENT_NONRESPONSE_TIME")) && Integer.parseInt(objAgent.getString("AGENT_NONRESPONSE_TIME")) >= agentTime) {
                        return false;   // 1분(default) 이상인 경우 신규 배정처리 
                    }
                    msgDataDao.insertCntRcvMsg(objParams);

                    return true;  // 아니면 메시지만 저장 하고 return 
                }
            }
        }
        //상담사가 상담중 요청중인 지 체크 , db 체크 ///////////////////////////////////////

        return false;
    }


    /**
     * 고객이 지속적으로 메시지를 보내는 상황에서 상담사가 상담대기에서 상담중으로 이관 중인 지 체크
     * 
     * @param  userKey
     * @return
     */
    public Boolean isAgentReadyToContact(String userKey) throws TelewebUtilException
    {
        if(chatInoutRepository.hasKey(userKey)) {
            return true;
        }
        else {
            //고객이 상담 진행 요청중인 경우
            //1초마다 최대 10번까지 체크
            // 불필요 로직 서주희 주석 
//            for(int i=0; i<10; i++)
//            {
//                //ing=true, inout=false
//                if(chatEscalatingRepository.hasKey(userKey))
//                {
//                    log.debug("[{}] CHECK INOUT ::: ing={},inout={}", i, chatEscalatingRepository.hasKey(userKey), chatInoutRepository.hasKey(userKey));
//                    Thread.sleep(1000);
//                }
//                else
//                {
//                    log.debug("[{}] CHECK INOUT ::: ing={},inout={}", i, chatEscalatingRepository.hasKey(userKey), chatInoutRepository.hasKey(userKey));
//                    break;
//                }
//            }

            if(chatInoutRepository.hasKey(userKey)) { return true; }
//            else if (TalkWebsocketChatInoutRepository.getInstance().getConsultConnections().containsKey(userKey))
//            {
//                return true;
//            }
        }
        return false;
    }


    /**
     * user_key 가 대기중 상태인지 한다.
     * 
     * @param  userKey
     * @return
     */
    public Boolean isUserReadyStates(TelewebJSON objParams) throws TelewebUtilException
    {
        // ready 테이블에서 상담원 id만 조회
        TelewebJSON objSelParams = msgDataDao.selectTalkUserReady(objParams);
        if(objSelParams != null && objSelParams.getSize() > 0) // ready 상태 
        { return true; }

        return false;
    }


    /**
     * 현재 연결 된 세션의 웹소켓 inout 연결상태체크
     * 
     * @param  userKey
     * @param  sendJson
     * @return
     */
    public boolean isStillAliveInOutSocket(String userKey) throws TelewebUtilException
    {
        if(chatInoutRepository.hasKey(userKey)) { return true; }
        log.trace("*************isStillAliveInOutSocket killed Websession = {} tried session", userKey);
        return false;
    }


    /**
     * 현재 연결 된 세션의 웹소켓 ready 연결상태체크
     * 
     * @param  agentID
     * @param  sendJson
     * @return
     */
    public boolean isStillAliveReadySocket(String agentID) throws TelewebUtilException
    {
        if(chatReadyRepository.hasKey(agentID)) { return true; }
        log.trace("*************isStillAliveReadySocket killed Websession = {} tried session", agentID);
        return false;
    }


    /**
     * user_key가 상담 대기가 없고 문의유형 선택중인지 체크한다
     * 
     * @param  userKey
     * @return
     */
    public Boolean isUserCuttStates(TelewebJSON objParams) throws TelewebUtilException
    {
        TelewebJSON objSelParams = msgDataDao.selectTalkUserCutt(objParams);
        if(objSelParams != null && objSelParams.getSize() > 0) // CUTT 상태 
        { return true; }

        return false;
    }


    /**
     * user_key가 상담 대기가 없고 문의유형 선택중인 상담을 종료한다
     * 
     * @param  userKey
     * @return
     */
    public Boolean updadateExpiredSessionCutt(TelewebJSON objParams) throws TelewebUtilException
    {
        // ready 테이블에서 상담원 id만 조회
        TelewebJSON objSelParams = msgDataDao.updadateExpiredSessionCutt(objParams);

        return false;
    }
}
