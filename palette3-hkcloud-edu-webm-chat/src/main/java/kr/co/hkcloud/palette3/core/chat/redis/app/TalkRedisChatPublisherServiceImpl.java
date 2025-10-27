package kr.co.hkcloud.palette3.core.chat.redis.app;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.config.properties.chat.ChatProperties;
import kr.co.hkcloud.palette3.core.chat.redis.dao.TalkRedisChatReadyRepository;
import kr.co.hkcloud.palette3.core.chat.router.app.TalkDataProcessService;
import kr.co.hkcloud.palette3.core.chat.stomp.app.TalkStompInoutService;
import kr.co.hkcloud.palette3.core.chat.stomp.app.TalkStompReadyService;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatStompVO;
import kr.co.hkcloud.palette3.core.util.PaletteFilterUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author Orange
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("talkStompRedisPublisher")
public class TalkRedisChatPublisherServiceImpl implements TalkRedisChatPublisherService {

    private final ChatProperties chatProperties;
    private final ChannelTopic channelTopic;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PaletteFilterUtils paletteFilterUtils;
    private final TalkStompInoutService stompInoutService;
    private final TalkStompReadyService stompReadyService;
    private final TalkRedisChatReadyRepository redisChatReadyRepository;
    private final TalkDataProcessService dataProcess;
    String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";

    /**
     * destination정보에서 userKey 추출
     * 
     * @param destination
     * @return
     */
    public String getUserKey(@NotEmpty String destination) throws TelewebAppException {
        int lastIndex = destination.lastIndexOf('/');
        if (lastIndex != -1) {
            return destination.substring(lastIndex + 1);
        }
        return "";
    }

    /**
     * Redis 채널에 메시지 발송 - 채팅방에 메시지 발송 (pub)
     * 
     * @param chatMessage
     */
    public void sendPubMessage(@NotNull ChatMessage chatMessage) throws TelewebAppException {
        String logPrefix = logDevider + ".sendPubMessage" + "___" + StringUtils.defaultString(chatMessage.getUserKey()) + "___";
        int logNum = 1;
        log.info(logPrefix + (logNum++) + " ::: 채팅방에 메시지 발송 (pub) start");
        log.info(logPrefix + (logNum++) + " ::: chatMessage.toString() ::: " + chatMessage.toString());
        log.info(logPrefix + (logNum++) + " ::: chatMessage.getChatType() ::: " + chatMessage.getChatType());
        log.info(logPrefix + (logNum++) + " ::: chatMessage.getChatEvent() ::: " + chatMessage.getChatEvent());
        log.info(logPrefix + (logNum++) + " ::: chatMessage.getMessageEvent() ::: " + chatMessage.getMessageEvent());

        switch (chatMessage.getChatType()) {
            //대기
            case READY: {
                switch (chatMessage.getChatEvent()) {
                    //상담대기
                    case AGENT_READY: {
                        switch (chatMessage.getMessageEvent()) {
                            //시스템
                            case SYSTEM: {
                                //READY:AGENT_READY:SYSTEM - 상담대기완료
                                log.info(logPrefix + (logNum++) + " ::: READY:AGENT_READY:SYSTEM - 상담대기완료");

                                TelewebJSON inJson = new TelewebJSON();
                                inJson.setString("USER_ID", chatMessage.getUserId());
                                inJson.setString("CUSTCO_ID", chatMessage.getCustcoId());
                                inJson.setString("TALK_START_DT", DateFormatUtils.format((new Date()), "yyyyMMddHHmmss"));
                                inJson.setString("USER_STATUS_CD", chatProperties.getChatOn().name());

                                //상담대기 처리 , 상담대기는 DB 값 업데이트 시 마다 변경 됨. READY 웹소켓은 늘 구독 상태임. SJH 20200417
                                //                                stompReadyService.insertReady(inJson);
                                //redis 저장 - tenantId:custcoId:palette:messenger:ready-enter-info
                                redisChatReadyRepository.setStompVO(ChatStompVO.builder().agentId(chatMessage.getUserId()).sessionId(
                                    chatMessage.getSessionId()).build());

                                chatMessage.setMessage(String.format(" %s 님, 채팅 ON 상태가 되었습니다.", chatMessage.getUserNickname()));

                                TelewebJSON responseJson = new TelewebJSON();
                                responseJson.setHeader("code", 0);
                                responseJson.setHeader("ERROR_FLAG", false);
                                responseJson.setHeader("ERROR_MSG", chatMessage.getMessage());
                                responseJson.setHeader("from_who", "fromweb");
                                responseJson.setHeader("called_api", "/agentReady");
                                chatMessage.setTelewebJsonString(paletteFilterUtils.filter(responseJson.toString()));

                                redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
                                break;
                            }
                            default: {
                                log.info(logPrefix + (logNum++) + " ::: READY:AGENT_READY:default");
                                redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
                                break;
                            }
                        }
                        break;
                    }
                    //배정알람
                    case READY_ALRAM: {
                        switch (chatMessage.getMessageEvent()) {
                            //시스템
                            case SYSTEM: {
                                //READY:READY_ALRAM:SYSTEM
                                log.info(logPrefix + (logNum++) + " ::: READY:READY_ALRAM:SYSTEM");
                                redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
                                break;
                            }
                            default: {
                                //READY:READY_ALRAM:default
                                log.info(logPrefix + (logNum++) + " ::: READY:READY_ALRAM:default");
                                redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
                                break;
                            }
                        }
                        break;
                    }
                    //전달배정알람
                    case TRANS_READY_ALRAM: {
                        switch (chatMessage.getMessageEvent()) {
                            //시스템
                            case SYSTEM: {
                                //READY:TRANS_READY_ALRAM:SYSTEM
                                log.info(logPrefix + (logNum++) + " ::: READY:TRANS_READY_ALRAM:SYSTEM");
                                redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
                                break;
                            }
                            default: {
                                //READY:TRANS_READY_ALRAM:default
                                log.info(logPrefix + (logNum++) + " ::: READY:TRANS_READY_ALRAM:default");
                                redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
                                break;
                            }
                        }
                        break;
                    }
                    //고객세션만료
                    case EXPIRED_SESSION_CUST: {
                        //READY:EXPIRED_SESSION_CUST
                        log.info(logPrefix + (logNum++) + " ::: READY:EXPIRED_SESSION_CUST");

                        //INOUT:REMOVE:REDIS - 대기중 세션 제거
                        TelewebJSON inJson = new TelewebJSON();
                        inJson.setString("TALK_USER_KEY", chatMessage.getUserKey());

                        //대기중 삭제 처리
                        stompInoutService.processTalkStackReady(inJson);

                        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
                        break;

                        //                        switch(chatMessage.getMessageEvent())
                        //                        {
                        //                            //시스템
                        //                            case SYSTEM:
                        //                            {
                        //                                log.trace("SYSTEM :::");
                        //                                
                        //                                redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
                        //                                break;
                        //                            }
                        //                            default:
                        //                            {
                        //                                log.trace("MessageEvent default ::: {}", chatMessage.getMessageEvent());
                        //                                
                        //                                redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
                        //                                break;
                        //                            }
                        //                        }
                        //                        break;
                    }
                    //연결분리
                    case DISCONNECT: {
                        switch (chatMessage.getMessageEvent()) {
                            //시스템
                            case SYSTEM: {
                                //READY:DISCONNECT:SYSTEM
                                log.info(logPrefix + (logNum++) + " ::: READY:DISCONNECT:SYSTEM");

                                TelewebJSON inJson = new TelewebJSON();
                                inJson.setString("USER_ID", chatMessage.getUserId());
                                inJson.setString("CUSTCO_ID", chatMessage.getCustcoId());
                                inJson.setString("USER_STATUS_CD", chatProperties.getChatOff().name());

                                //상담대기 삭제 처리
                                stompReadyService.deleteReady(inJson);

                                //READY:REMOVE:REDIS [SUB] - 상담대기 삭제
                                log.info(logPrefix + (logNum++) + " ::: READY:REMOVE:REDIS [SUB] - 상담대기 삭제");
                                redisChatReadyRepository.removeUserId(Integer.parseInt(chatMessage.getUserId()));

                                redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
                                break;
                            }
                            default: {
                                //READY:DISCONNECT:default
                                log.info(logPrefix + (logNum++) + " ::: READY:DISCONNECT:default");

                                redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
                                break;
                            }
                        }
                        break;
                    }
                    default: {
                        //READY:default
                        log.info(logPrefix + (logNum++) + " ::: READY:default");

                        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
                        break;
                    }
                }
                break;
            }
            //상담중
            case INOUT: {
                switch (chatMessage.getChatEvent()) {
                    //상담
                    case IN_OUT: {
                        switch (chatMessage.getMessageEvent()) {
                            //입장
                            case ENTER: {
                                //INOUT:IN_OUT:ENTER
                                log.info(logPrefix + (logNum++) + " ::: INOUT:IN_OUT:ENTER");

                                TelewebJSON inJson = new TelewebJSON();
                                inJson.setString("USER_ID", chatMessage.getUserId());
                                inJson.setString("CUSTCO_ID", chatMessage.getCustcoId());
                                inJson.setString("TALK_USER_KEY", chatMessage.getUserKey());
                                inJson.setString("TALK_GB", "10");

                                //재로그인하고 상담중인 건 다시 상담하게 될 때 자동인사가 2번 나갈 수 있음
                                //상담이력에서 자동인사여부 체크 by liy
                                TelewebJSON autoGreetingJson = new TelewebJSON();
                                autoGreetingJson.setString("TALK_CONTACT_ID", chatMessage.getContactId());
                                autoGreetingJson.setString("CHT_CUTT_ID", chatMessage.getContactId());
                                autoGreetingJson.setString("TALK_USER_KEY", chatMessage.getUserKey());
                                autoGreetingJson.setString("SNDR_KEY", chatMessage.getSenderKey());
                                autoGreetingJson.setString("CUSTCO_ID", chatMessage.getCustcoId());

                                Boolean isSendAutoGreeting = false;

                                TelewebJSON retAutoGreetingJson = dataProcess.selectTalkContactAutoGreeting(autoGreetingJson);
                                if (retAutoGreetingJson != null && retAutoGreetingJson.getSize() > 0 && retAutoGreetingJson != null && "Y"
                                    .equals(retAutoGreetingJson.getString("AUTO_GREETING_YN"))) {
                                    isSendAutoGreeting = true;
                                }

                                // 네트웍 단절 발생 하는 경우, 폴링 어싱크로 연결 시도 하기때문에 종료이후에도 REDIS 삽입 됨
                                //, 상태가 (12) 상담건만 입장 처리를 하도록 처리 변경 ( sjh)
                                if (retAutoGreetingJson != null && retAutoGreetingJson.getSize() > 0 && "12".equals(retAutoGreetingJson
                                    .getString("TALK_STAT_CD"))) {

                                    //상담시작(중) 삽입과 마지막상담 병합 처리 
                                    stompInoutService.processTalkIngLast(inJson);
                                }

                                TelewebJSON responseJson = new TelewebJSON();

                                //                                //상담사자동인사 사용여부
                                //                                if (!isSendAutoGreeting && "Y".equals(HcTeletalkDbEnvironment.getInstance().getString("AUTO_GREETING_YN")))
                                //                                {
                                //                                    StringBuffer sbGreetingMsg = new StringBuffer();
                                //                                    String strSystemMsgId = "";
                                //                                    
                                //                                    // 고객문의유형 사용할 때
                                //                                    if ("Y".equals(HcTeletalkDbEnvironment.getInstance().getString("INQRY_TYPE_YN")))
                                //                                    {
                                //                                        String s1 = filterUtils.filter2(HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId("28"));
                                //                                        sbGreetingMsg.append(s1);
                                //                                        strSystemMsgId = "28";
                                //                                    }
                                //                                    else
                                //                                    {
                                //                                        String s2 = filterUtils.filter2(HcTeletalkDbSystemMessage.getInstance().getStringBySystemMsgId("29"));
                                //                                        sbGreetingMsg.append(s2);
                                //                                        strSystemMsgId = "29";
                                //                                    }
                                //                                    //2019.04.30 모빌리티 자동인사메시지 사용자명에서 닉네임으로 변경
                                //                                    String greetingMsg = String.format(sbGreetingMsg.toString(), chatMessage.getUserNickname());
                                //
                                //                                    //고객에게 보낸다
                                //                                    {
                                //                                        TelewebJSON messageJson = new TelewebJSON();
                                //                                        messageJson.setString("TALK_CONTACT_ID", chatMessage.getContactId());
                                //                                        messageJson.setString("MSG_CN", greetingMsg);
                                //                                        messageJson.setString("SNDRCV_CD", "SND");
                                //                                        
                                //                                        transToKakaoService.sendCustNoResponseSystemMsg(chatMessage.getUserKey(), chatMessage.getSenderKey(), messageJson, strSystemMsgId);
                                //                                    }
                                //                                    
                                //                                    //상담사에게 보낸다
                                //                                    {
                                //                                        responseJson.setHeader("auto_greeting_message_yn", "Y");
                                //                                        responseJson.setHeader("code", 0);
                                //                                        responseJson.setHeader("ERROR_FLAG", false);
                                //                                        responseJson.setHeader("ERROR_MSG", greetingMsg);
                                //                                        responseJson.setString("user_key", chatMessage.getUserKey());
                                ////                                        sendToAgentLocal(responseJson);
                                //                                    }
                                //                                    
                                //                                    //상담이력에 자동인사여부 Y로 업데이트
                                //                                    {
                                //                                        dataProcess.updateTalkContactAutoGreetingY(autoGreetingJson);
                                //                                    }
                                //                                }
                                //                                else
                                //                                {
                                //                                    String message = String.format("%s 님, 고객님과 채팅이 시작 되었습니다.", chatMessage.getUserName());
                                //                                    responseJson.setHeader("code", 0);
                                //                                    responseJson.setHeader("ERROR_FLAG", false);
                                //                                    responseJson.setHeader("ERROR_MSG", message);
                                ////                                    sendToAgentLocal(responseJson);
                                //                                }

                                String message = String.format("%s 님, 고객님과 채팅이 시작 되었습니다.", chatMessage.getUserName());
                                responseJson.setHeader("code", 0);
                                responseJson.setHeader("ERROR_FLAG", false);
                                responseJson.setHeader("ERROR_MSG", message);
                                //                                  sendToAgentLocal(responseJson);

                                responseJson.setHeader("from_who", "fromweb");
                                responseJson.setHeader("called_api", "/inOut");
                                chatMessage.setTelewebJsonString(paletteFilterUtils.filter(responseJson.toString()));

                                break;
                            }
                            default: {
                                //INOUT:IN_OUT:default
                                log.info(logPrefix + (logNum++) + " ::: INOUT:IN_OUT:default");

                                break;
                            }
                        }
                        log.info(logPrefix + (logNum++) + " ::: channelTopic.getTopic() ::: " + channelTopic.getTopic());
                        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);

                        break;
                    }
                    //연결분리
                    case DISCONNECT:
                    case EXPIRED_SESSION_CUST: {// 종료종
                        //INOUT:EXPIRED_SESSION_CUST
                        log.info(logPrefix + (logNum++) + " ::: INOUT:EXPIRED_SESSION_CUST");

                        TelewebJSON inJson = new TelewebJSON();
                        inJson.setString("USER_ID", chatMessage.getUserId());
                        inJson.setString("CUSTCO_ID", chatMessage.getCustcoId());
                        inJson.setString("TALK_USER_KEY", chatMessage.getUserKey());
                        inJson.setString("TALK_GB", "10");

                        //상담스택병합 및 상담시작(중) 삭제 처리
                        stompInoutService.processTalkStackIng(inJson);

                        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);

                        break;
                    }
                    default: {
                        //INOUT:default
                        log.info(logPrefix + (logNum++) + " ::: INOUT:default");

                        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
                        break;
                    }
                }
                break;
            }
            //3자
            case CONSULT: {
                switch (chatMessage.getChatEvent()) {
                    //상담
                    case IN_OUT: {
                        switch (chatMessage.getMessageEvent()) {
                            //입장
                            case ENTER: {
                                //CONSULT:IN_OUT:ENTER
                                log.info(logPrefix + (logNum++) + " ::: CONSULT:IN_OUT:ENTER");

                                // 입장알림여부 
                                String consultAlramYn = HcTeletalkDbEnvironment.getInstance().getString(chatMessage.getCustcoId(),
                                    "CONSULT_ALRAM_YN");

                                if (consultAlramYn != null && "Y".equals(consultAlramYn)) {
                                    TelewebJSON responseJson = new TelewebJSON();
                                    String message = String.format("상담사 %s 님 컨설팅을 시작하였습니다.", chatMessage.getUserName());

                                    chatMessage.setChatType(ChatMessage.ChatType.INOUT);                // 입장메시지 전송 
                                    chatMessage.setMessageEvent(ChatMessage.MessageEvent.TALK);         // 입장메시지 전송

                                    responseJson.setHeader("code", 0);
                                    responseJson.setHeader("ERROR_FLAG", false);
                                    responseJson.setHeader("from_who", "fromweb");
                                    responseJson.setHeader("called_api", "/consult");
                                    responseJson.setHeader("system_message_yn", "Y");
                                    responseJson.setString("message", message);
                                    responseJson.setString("consult_message", message);
                                    responseJson.setString("SNDRCV_CD", "TRAN");
                                    responseJson.setString("USER_ID", chatMessage.getUserId());
                                    responseJson.setString("USER_NAME", chatMessage.getUserName());
                                    responseJson.setString("user_key", chatMessage.getUserKey());

                                    responseJson.setString("TALK_CONTACT_ID", chatMessage.getContactId());
                                    responseJson.setString("TALK_USER_KEY", chatMessage.getUserKey());

                                    chatMessage.setTelewebJsonString(paletteFilterUtils.filter(responseJson.toString()));
                                }
                                //CONSULT:IN_OUT:ENTER [PUB] - 상담중 완료
                                log.info(logPrefix + (logNum++) + " :::  CONSULT:IN_OUT:ENTER [PUB] - 상담중 완료");
                                redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
                                break;
                            }
                            default: {
                                //CONSULT:IN_OUT:default
                                log.info(logPrefix + (logNum++) + " :::  CONSULT:IN_OUT:default");
                                redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
                                break;
                            }
                        }
                        break;
                    }
                    // 3자 상담
                    case DISCONNECT: {
                        //CONSULT:DISCONNECT
                        log.info(logPrefix + (logNum++) + " :::  CONSULT:DISCONNECT");

                        // 입장알림여부 
                        String consultAlramYn = HcTeletalkDbEnvironment.getInstance().getString(chatMessage.getCustcoId(),
                            "CONSULT_ALRAM_YN");

                        if (consultAlramYn != null && "Y".equals(consultAlramYn)) {
                            TelewebJSON responseJson = new TelewebJSON();
                            String message = String.format("상담사 %s 님 컨설팅을 종료하였습니다.", chatMessage.getUserName());

                            chatMessage.setChatType(ChatMessage.ChatType.INOUT);                // 입장메시지 전송 
                            chatMessage.setMessageEvent(ChatMessage.MessageEvent.TALK);         // 입장메시지 전송

                            responseJson.setHeader("code", 0);
                            responseJson.setHeader("ERROR_FLAG", false);
                            responseJson.setHeader("from_who", "fromweb");
                            responseJson.setHeader("called_api", "/consult");
                            responseJson.setHeader("system_message_yn", "Y");
                            responseJson.setString("message", message);
                            responseJson.setString("consult_message", message);
                            responseJson.setString("SNDRCV_CD", "TRAN");
                            responseJson.setString("USER_ID", chatMessage.getUserId());
                            responseJson.setString("USER_NAME", chatMessage.getUserName());
                            responseJson.setString("user_key", chatMessage.getUserKey());
                            responseJson.setString("TALK_CONTACT_ID", chatMessage.getContactId());
                            responseJson.setString("TALK_USER_KEY", chatMessage.getUserKey());

                            chatMessage.setTelewebJsonString(paletteFilterUtils.filter(responseJson.toString()));
                        }

                        //CONSULT:IN_OUT:DISCONNECT [PUB] - 상담중 완료
                        log.info(logPrefix + (logNum++) + " :::  CONSULT:IN_OUT:DISCONNECT [PUB] - 상담중 완료");
                        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
                        break;
                    }
                    default: {
                        //CONSULT:default
                        log.info(logPrefix + (logNum++) + " :::  CONSULT:default");
                        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
                        break;
                    }
                }
                break;
            }
            default: {
                //default
                log.info(logPrefix + (logNum++) + " :::  default");

                break;
            }
        }
    }
}
