package kr.co.hkcloud.palette3.core.chat.redis.app;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.core.chat.stomp.app.TalkStompInoutService;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service("talkRedisChatSubscriberService")
public class TalkRedisChatSubscriberServiceImpl implements TalkRedisChatSubscriberService {

    private final ObjectMapper objectMapper;
    private final TalkStompInoutService stompInoutService;
    private final SimpMessageSendingOperations messagingTemplate;
    
    //채팅 사용 여부 - 여기서는 쿠버네티스에서 palette3-chat pod인지를 체크하기 위함. 
    //palette3-chat pod가 아닐 시 redis 메시지 구독 처리 안함.
    @Value("${chat.enabled}")
    private boolean chatEnabled;
    
    String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";

    /**
     * Redis 메시지 구독 처리 - Redis에서 메시지가 발행(publish)되면 대기하고 있던 Redis Subscriber가 해당 메시지를 받아 처리한다. (sub)
     * 
     * @param publishedMessage
     */
    public void onSubMessage(String publishedMessage) throws TelewebAppException {
        //////////////////////////////////////////////////////////////////////////////////////////////
        //이슈 : redis에 발행된 메시지를 쿠버네티스의 모든 api pod에서 구독 처리함 - palette3-api, palette3-phone, palette3-sse 등
        //처리 : palette3-chat이 아닌 타 api pod에서 구독 처리 안하도록 처리.
        //       palette3-chat 에서만 chat.enabled 설정이 true 임.
        if(!chatEnabled) {
            return;
        }
        //////////////////////////////////////////////////////////////////////////////////////////////
        
        String logPrefix = logDevider + ".onSubMessage" + "___";
        int logNum = 1;
        log.info(logPrefix + (logNum++) + " ::: Redis 메시지 구독(Sub) start");
        log.info(logPrefix + (logNum++) + " ::: publishedMessage ::: " + publishedMessage);

        //StompChatMessage 객채로 맵핑
        ChatMessage chatMessage;
        try {
            chatMessage = objectMapper.readValue(publishedMessage, ChatMessage.class);
            logPrefix += StringUtils.defaultString(chatMessage.getUserId()) + "___" + StringUtils.defaultString(chatMessage.getUserKey()) + "___";
            log.info(logPrefix + (logNum++) + " ::: chatMessage ::: " + chatMessage.toString());
        } catch (JsonProcessingException e) {
            log.error(e.getLocalizedMessage());
            throw new TelewebAppException(e.getLocalizedMessage());
        }

        log.info(logPrefix + (logNum++) + " ::: chatMessage.getChatType() ::: " + chatMessage.getChatType());
        log.info(logPrefix + (logNum++) + " ::: chatMessage.getChatEvent() ::: " + chatMessage.getChatEvent());
        log.info(logPrefix + (logNum++) + " ::: chatMessage.getMessageEvent() ::: " + chatMessage.getMessageEvent());
        switch (chatMessage.getChatType()) {
            //대기
            case READY: {
                switch (chatMessage.getChatEvent()) {
                    //채팅ON
                    case AGENT_READY: {
                        switch (chatMessage.getMessageEvent()) {
                            //입장
                            case SYSTEM: {
                                //READY:AGENT_READY:SYSTEM - 상담사대기완료
                                log.info(logPrefix + (logNum++) + " ::: READY:AGENT_READY:SYSTEM - 상담사대기완료 ::: " + "/queue/ready/" + chatMessage.getUserId());
                                messagingTemplate.convertAndSend("/queue/ready/" + chatMessage.getUserId(), chatMessage);
                                break;
                            }
                            default: {
                                //READY:AGENT_READY:default
                                log.info(logPrefix + (logNum++) + " ::: READY:AGENT_READY:default ::: " + String.format("/queue/ready/%s", chatMessage.getUserId()));
                                messagingTemplate.convertAndSend(String.format("/queue/ready/%s", chatMessage.getUserId()), chatMessage);
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
                                //READY:READY_ALRAM:SYSTEM - 대기중 배정알람 완료
                                log.info(logPrefix + (logNum++) + " ::: READY:READY_ALRAM:SYSTEM - 대기중 배정알람 완료 ::: " + String.format("/queue/ready/%s", chatMessage.getUserId()));
                                messagingTemplate.convertAndSend(String.format("/queue/ready/%s", chatMessage.getUserId()), chatMessage);
                                break;
                            }
                            default: {
                                //READY:READY_ALRAM:default
                                log.info(logPrefix + (logNum++) + " ::: READY:READY_ALRAM:default ::: " + String.format("/queue/ready/%s", chatMessage.getUserId()));
                                messagingTemplate.convertAndSend(String.format("/queue/ready/%s", chatMessage.getUserId()), chatMessage);
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
                                //READY:TRANS_READY_ALRAM:SYSTEM - 상담중 전달배정알람 완료
                                log.info(logPrefix + (logNum++) + " ::: READY:TRANS_READY_ALRAM:SYSTEM - 상담중 전달배정알람 완료 ::: " + String.format("/queue/ready/%s", chatMessage.getUserId()));
                                messagingTemplate.convertAndSend(String.format("/queue/ready/%s", chatMessage.getUserId()), chatMessage);
                                break;
                            }
                            default: {
                                //READY:TRANS_READY_ALRAM:defult - 상담중 전달배정알람 완료
                                log.info(logPrefix + (logNum++) + " ::: READY:TRANS_READY_ALRAM:defult ::: " + String.format("/queue/ready/%s", chatMessage.getUserId()));
                                messagingTemplate.convertAndSend(String.format("/queue/ready/%s", chatMessage.getUserId()), chatMessage);
                                break;
                            }
                        }
                        break;
                    }
                    //고객세션만료
                    case EXPIRED_SESSION_CUST: {
                        switch (chatMessage.getMessageEvent()) {
                            //시스템
                            case SYSTEM: {
                                //READY:EXPIRED_SESSION_CUST:SYSTEM - 대기중 고객세션만료 완료
                                log.info(logPrefix + (logNum++) + " ::: READY:EXPIRED_SESSION_CUST:SYSTEM - 대기중 고객세션만료 완료 ::: " + String.format("/queue/ready/%s", chatMessage.getUserId()));
                                messagingTemplate.convertAndSend(String.format("/queue/ready/%s", chatMessage.getUserId()), chatMessage);
                                break;
                            }
                            default: {
                                //READY:EXPIRED_SESSION_CUST:default
                                log.info(logPrefix + (logNum++) + " ::: READY:EXPIRED_SESSION_CUST:default ::: " + String.format("/queue/ready/%s", chatMessage.getUserId()));
                                messagingTemplate.convertAndSend(String.format("/queue/ready/%s", chatMessage.getUserId()), chatMessage);
                                break;
                            }
                        }
                        break;
                    }
                    //연결분리
                    case DISCONNECT: {
                        //READY:DISCONNECT:SYSTEM - 상담사대기 연결분리 완료
                        log.info(logPrefix + (logNum++) + " ::: READY:DISCONNECT:SYSTEM - 상담사대기 연결분리 완료 ::: " + String.format("/queue/ready/%s", chatMessage.getUserId()));
                        messagingTemplate.convertAndSend(String.format("/queue/ready/%s", chatMessage.getUserId()), chatMessage);
                        break;
                    }
                    default: {
                        //READY:default
                        log.info(logPrefix + (logNum++) + " ::: READY:default ::: " + String.format("/queue/ready/%s", chatMessage.getUserId()));
                        messagingTemplate.convertAndSend(String.format("/queue/ready/%s", chatMessage.getUserId()), chatMessage);
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
                                //INOUT:IN_OUT:ENTER - 상담중 완료
                                log.info(logPrefix + (logNum++) + " ::: INOUT:IN_OUT:ENTER - 상담중 완료 ::: " + String.format("/topic/inout/%s", chatMessage.getUserKey()));
                                messagingTemplate.convertAndSend(String.format("/topic/inout/%s", chatMessage.getUserKey()), chatMessage);
                                break;
                            }
                            default: {
                                //INOUT:IN_OUT:default
                                log.info(logPrefix + (logNum++) + " ::: INOUT:IN_OUT:default ::: " + String.format("/topic/inout/%s", chatMessage.getUserKey()));
                                messagingTemplate.convertAndSend(String.format("/topic/inout/%s", chatMessage.getUserKey()), chatMessage);
                                break;
                            }
                        }
                        break;
                    }
                    //고객세션만료
                    case EXPIRED_SESSION_CUST: {
                        //INOUT:EXPIRED_SESSION_CUST
                        log.info(logPrefix + (logNum++) + " ::: INOUT:EXPIRED_SESSION_CUST - 고객세션만료 ::: " + String.format("/topic/inout/%s", chatMessage.getUserKey()));
                        messagingTemplate.convertAndSend(String.format("/topic/inout/%s", chatMessage.getUserKey()), chatMessage);
                        break;
                    }
                    //상담사고객세션만료
                    case EXPIRED_SESSION_AGENT: {
                        //INOUT:EXPIRED_SESSION_AGENT
                        log.info(logPrefix + (logNum++) + " ::: INOUT:EXPIRED_SESSION_AGENT - 상담사고객세션만료 ::: " + String.format("/topic/inout/%s", chatMessage.getUserKey()));

                        TelewebJSON inJson = new TelewebJSON();
                        inJson.setString("USER_ID", chatMessage.getUserId());
                        inJson.setString("TALK_USER_KEY", chatMessage.getUserKey());
                        inJson.setString("TALK_GB", "10");

                        //상담스택병합 및 상담시작(중) 삭제 처리
                        stompInoutService.processTalkStackIng(inJson);

                        messagingTemplate.convertAndSend(String.format("/topic/inout/%s", chatMessage.getUserKey()), chatMessage);
                        break;
                    }
                    //상담사고객세션만료
                    case NO_CUSTOMER_RESPONSE: {
                        //INOUT:NO_CUSTOMER_RESPONSE
                        log.info(logPrefix + (logNum++) + " ::: INOUT:NO_CUSTOMER_RESPONSE - 상담사고객세션만료 ::: " + String.format("/topic/inout/%s", chatMessage.getUserKey()));
                        messagingTemplate.convertAndSend(String.format("/topic/inout/%s", chatMessage.getUserKey()), chatMessage);
                        break;
                    }
                    //연결분리
                    case DISCONNECT: {
                        //INOUT:DISCONNECT
                        log.info(logPrefix + (logNum++) + " ::: INOUT:DISCONNECT - 연결분리 ::: " + String.format("/topic/inout/%s", chatMessage.getUserKey()));
                        messagingTemplate.convertAndSend(String.format("/topic/inout/%s", chatMessage.getUserKey()), chatMessage);
                        break;
                    }
                    default: {
                        //INOUT:default
                        log.info(logPrefix + (logNum++) + " ::: INOUT:default ::: ");
                        break;
                    }
                }
                break;
            }
            case CONSULT: {
                switch (chatMessage.getChatEvent()) {
                    //상담
                    case IN_OUT: {
                        switch (chatMessage.getMessageEvent()) {
                            //입장
                            case ENTER: {
                                //CONSULT:IN_OUT:ENTER - 상담중 완료
                                
                                //입장알림여부
                                String consultAlramYn = HcTeletalkDbEnvironment.getInstance().getString(chatMessage.getCustcoId(),
                                    "CONSULT_ALRAM_YN");
                                log.info(logPrefix + (logNum++) + " ::: CONSULT:IN_OUT:ENTER - consultAlramYn ::: " + consultAlramYn);
                                if (consultAlramYn != null && "Y".equals(consultAlramYn)) {
                                    log.info(logPrefix + (logNum++) + " ::: CONSULT:IN_OUT:ENTER - 상담중 완료 ::: " + String.format("/topic/inout/%s", chatMessage.getUserKey()));
                                    messagingTemplate.convertAndSend(String.format("/topic/inout/%s", chatMessage.getUserKey()),
                                        chatMessage);
                                }
                                break;
                            }
                            default: {
                                //CONSULT:IN_OUT:default
                                log.info(logPrefix + (logNum++) + " ::: CONSULT:IN_OUT:default ::: " + String.format("/topic/inout/%s", chatMessage.getUserKey()));
                                messagingTemplate.convertAndSend(String.format("/topic/inout/%s", chatMessage.getUserKey()), chatMessage);
                                break;
                            }
                        }
                        //                        
                        break;
                    }
                    // 3자 상담
                    case DISCONNECT: {
                        //CONSULT:DISCONNECT
                        
                        // 입장알림여부 
                        String consultAlramYn = HcTeletalkDbEnvironment.getInstance().getString(chatMessage.getCustcoId(),
                            "CONSULT_ALRAM_YN");
                        if (consultAlramYn != null && "Y".equals(consultAlramYn)) {
                            log.info(logPrefix + (logNum++) + " ::: CONSULT:DISCONNECT ::: " + String.format("/topic/inout/%s", chatMessage.getUserKey()));
                            messagingTemplate.convertAndSend(String.format("/topic/inout/%s", chatMessage.getUserKey()), chatMessage);
                        }
                        break;
                    }
                    default: {
                        //CONSULT:default
                        log.info(logPrefix + (logNum++) + " ::: CONSULT:default ::: " + String.format("/topic/inout/%s", chatMessage.getUserKey()));
                        messagingTemplate.convertAndSend(String.format("/topic/inout/%s", chatMessage.getUserKey()), chatMessage);
                        break;
                    }

                }

            }
            default: {
                log.info(logPrefix + (logNum++) + " ::: default ::: ");
                break;
            }
        }
    }
}
