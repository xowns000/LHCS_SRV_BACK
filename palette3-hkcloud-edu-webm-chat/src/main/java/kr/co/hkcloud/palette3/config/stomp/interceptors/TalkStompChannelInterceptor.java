package kr.co.hkcloud.palette3.config.stomp.interceptors;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.multitenancy.TenantContext;
import kr.co.hkcloud.palette3.config.properties.chat.ChatProperties;
import kr.co.hkcloud.palette3.config.security.TeletalkAuthority;
import kr.co.hkcloud.palette3.config.stomp.listeners.TalkWebSocketConstants;
import kr.co.hkcloud.palette3.config.stomp.provider.TalkStompJwtTokenProvider;
import kr.co.hkcloud.palette3.core.chat.redis.app.TalkRedisChatPublisherServiceImpl;
import kr.co.hkcloud.palette3.core.chat.redis.dao.TalkRedisChatInoutRepository;
import kr.co.hkcloud.palette3.core.chat.redis.dao.TalkRedisChatReadyRepository;
import kr.co.hkcloud.palette3.core.chat.stomp.app.TalkStompReadyService;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.ChatEvent;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.ChatType;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatStompVO;
import kr.co.hkcloud.palette3.core.util.PaletteAntPathUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class TalkStompChannelInterceptor implements ChannelInterceptor {

    private final ChatProperties chatProperties;
    private final TalkStompJwtTokenProvider stompJwtTokenProvider;
    private final TalkStompReadyService stompReadyService;
    private final TalkRedisChatReadyRepository redisChatReadyRepository;
    private final TalkRedisChatInoutRepository redisChatInoutRepository;
    private final TalkRedisChatPublisherServiceImpl stompRedisPublisher;
    String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";

    //@Autowired
    //private RedisTemplate<String, Object> redisTemplate;

    /**
     * 상담사가 고객에게 전송하는 메세지 - frontend(vue)의 stompUtil.sendMessage에서 this.stompClient.send("/app/inout/chat/message") 로 발송한 메세지를 TalkStompChatMessageController.onInoutMessage 호출전에 인터셉트함
     * 메시지가 실제로 채널로 전송되기 전에 호출됩니다. 필요한 경우 메시지를 수정할 수 있습니다. 이 메서드는 null을 반환하면 실제 전송 호출이 발생하지 않습니다.
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        String logPrefix = logDevider + ".preSend" + "___";
        int logNum = 1;
        log.info(logPrefix + (logNum++) + " ::: start ::: ");
        //StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (log.isInfoEnabled()) {
            StringBuffer sb = new StringBuffer("\n");
            sb.append("\tChannel\t\t:").append(channel.toString()).append("\n");
            sb.append("\tAccessor\t:").append(accessor.toString()).append("\n");
            sb.append("\tSessionId\t:").append(accessor.getSessionId()).append("\n");
            sb.append("\tMessageType\t:").append(accessor.getMessageType()).append("\n");
            sb.append("\tCommand\t\t:").append(accessor.getCommand()).append("\n");
            sb.append("\tDestination\t:").append(accessor.getDestination()).append("\n");
            sb.append("\tMessage\t\t:").append(accessor.getMessage()).append("\n");
            sb.append("\tChatType\t:").append(accessor.getNativeHeader("chatType")).append("\n");
            log.info("\n-------------------\nSTOMP TalkStompChannelInterceptor preSend INFORMATION\n-------------------\n{}\n", sb
                .toString());
        }

        //        int userStrIndex = 0;
        //        int userStrLastIndex = 0;
        //        userStrIndex = accessor.toString().indexOf("userId=[");
        //        userStrLastIndex = accessor.toString().indexOf("],", userStrIndex);
        //        String tmpUserId = "";
        //        String cuslId = "";
        //        tmpUserId = accessor.toString().substring(userStrIndex, userStrLastIndex);
        //        String[] aryUserId = tmpUserId.replaceAll("[", "").split("");
        //        log.info("tmpUserId"+tmpUserId);
        //        if(aryUserId.length > 0) {
        //        	cuslId = aryUserId[1];
        //        } else {
        //        	cuslId = "2";	//시스템
        //        }
        //
        //        int tokenStrIndex = 0;
        //        int tokenStrLastIndex = 0;
        //        tokenStrIndex = accessor.toString().indexOf("[bearer ");
        //        tokenStrLastIndex = accessor.toString().indexOf("],", tokenStrIndex);
        //        String tmpToken = "";
        //        String token = "";
        //        token = accessor.toString().substring(tokenStrIndex+8, tokenStrLastIndex);
        //        log.info("123$#@@#"+token);
        //        String[] aryToken = tmpToken.split("");
        //        log.info("tmpToken"+tmpToken);
        //        if(aryToken.length > 0) {
        //            token = aryToken[1];
        //        } else {
        //        	token = "";	//토큰 오류
        //        }
        //
        //        int custcoStrIndex = 0;
        //        int custcoStrLastIndex = 0;
        //        custcoStrIndex = accessor.toString().indexOf("custcoId=[");
        //        custcoStrLastIndex = accessor.toString().indexOf("],", userStrIndex);
        //        String tmpcustcoId = "";
        //        String custcoId = "";
        //        tmpcustcoId = accessor.toString().substring(custcoStrIndex, custcoStrLastIndex);
        //        String[] arycustcoId = tmpUserId.replaceAll("[", "").split("");
        //        log.info("tmpUserId"+tmpUserId);
        //        if(arycustcoId.length > 0) {
        //        	custcoId = arycustcoId[1];
        //        } else {
        //        	custcoId = "2";	//시스템
        //        }

        //HttpSession session = (HttpSession) accessor.getSessionAttributes().get(TalkWebSocketConstants.TELETALK_SESSION_OBJ);
        String custcoId = String.valueOf(accessor.getSessionAttributes().get(TalkWebSocketConstants.TELETALK_CUSTCO_ID));

        if (accessor.getNativeHeader("CERT-CUSTCO-TENANT-ID") != null && !accessor.getNativeHeader("CERT-CUSTCO-TENANT-ID").equals("")
            && !accessor.getNativeHeader("CERT-CUSTCO-TENANT-ID").equals("[]")) {
            String tenantCodeBlk = String.valueOf(accessor.getNativeHeader("CERT-CUSTCO-TENANT-ID"));
            String tenantCode = tenantCodeBlk.substring(1, tenantCodeBlk.length() - 1);
            TenantContext.setCurrentTenant(tenantCode);
            log.info(logPrefix + (logNum++) + " ::: tenantCode TenantContext.setCurrentTenant(tenantCode) ::: " + tenantCode);
        }

        if (accessor.getNativeHeader("custcoId") != null && !accessor.getNativeHeader("custcoId").equals("") && !accessor.getNativeHeader(
            "custcoId").equals("[]")) {
            String custcoBlk = String.valueOf(accessor.getNativeHeader("custcoId"));
            String custco = custcoBlk.substring(1, custcoBlk.length() - 1);
            TenantContext.setCurrentCustco(custco);
            log.info(logPrefix + (logNum++) + " ::: custcoId TenantContext.setCurrentCustco(custco) ::: " + custco);
        }
        
        log.info(logPrefix + (logNum++) + " ::: TenantContext ::: " + TenantContext.getCurrentCustco() + "___" + TenantContext.getCurrentTenant());
        

        if (custcoId == null || "".equals(custcoId)) {
            try {
                stompRedisPublisher.sendPubMessage(ChatMessage.builder().chatType(ChatMessage.ChatType.INOUT).chatEvent(
                    ChatMessage.ChatEvent.IN_OUT).messageEvent(ChatMessage.MessageEvent.SESSIONOUT).custcoId(custcoId).build());
            } catch (Exception e) {
                log.error(e.getLocalizedMessage(), e);
            }
            return message;
        }

        log.info(logPrefix + (logNum++) + " ::: accessor.getCommand() ::: " + accessor.getCommand());
        switch (accessor.getCommand()) {
            //연결요청
            case CONNECT: {
                //STOMP:CONNECT - STOMP 연결
                log.info(logPrefix + (logNum++) + " ::: CONNECT ::: STOMP:CONNECT - STOMP 연결");
                String jwtToken = accessor.getFirstNativeHeader("token");
                //                if(jwtToken.equals("") || jwtToken.length() == 0) {
                //                	jwtToken = token;
                //                }

                //Header의 jwt token 검증
                stompJwtTokenProvider.validateToken(jwtToken);

                //accessor.setUser(new User(String.valueOf(accessor.getSessionAttributes().get(TalkWebSocketConstants.TELETALK_ASP_USER_ID))));

                String user = String.valueOf(accessor.getSessionAttributes().get(TalkWebSocketConstants.TELETALK_ASP_USER_ID));
                //String user = accessor.getNativeHeader("userId").get(0);
                //String user = cuslId;
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_" + TeletalkAuthority.CODES.MEMBER));
                //authorities.add(new SimpleSessionId(String.valueOf(accessor.getSessionAttributes().get(TalkWebSocketConstants.TELETALK_SESSION_ATTR))));
                Authentication auth = new UsernamePasswordAuthenticationToken(user, user, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
                accessor.setUser(auth);

                break;
            }
            //구독
            case SUBSCRIBE: {
                //STOMP:SUBSCRIBE - STOMP 구독
                log.info(logPrefix + (logNum++) + " ::: CONNECT ::: STOMP:SUBSCRIBE - STOMP 구독");

                String sessionId = accessor.getSessionId();

                //대기
                if (PaletteAntPathUtils.matches("/queue/ready/**", accessor.getDestination())) {

                    log.trace("preSend SUBSCRIBE - READY ###");

                    try {

                        String userId = accessor.getNativeHeader("userId").get(0);    //Optional.ofNullable((Principal) accessor.getUser()).map(Principal::getName).orElse("UnknownUser");
                        //                    	if(userId.equals("") || userId.length() == 0) {
                        //                    		userId = cuslId;
                        //                        }
                        String userName = accessor.getNativeHeader("userName").get(0);
                        String userNickname = accessor.getNativeHeader("userNickname").get(0);

                        //READY:AGENT_READY:SYSTEM [PUB] - 상담사대기
                        log.info(logPrefix + (logNum++) + " ::: READY:AGENT_READY:SYSTEM [PUB] - 상담사대기");

                        stompRedisPublisher.sendPubMessage(ChatMessage.builder().chatType(ChatMessage.ChatType.READY).chatEvent(
                            ChatMessage.ChatEvent.AGENT_READY).messageEvent(ChatMessage.MessageEvent.SYSTEM).userId(userId).userName(
                                userName).userNickname(userNickname).sessionId(sessionId).custcoId(custcoId).build());

                    } catch (Exception e) {
                        log.error(e.getLocalizedMessage(), e);
                    }
                }
                //상담
                else if (PaletteAntPathUtils.matches("/topic/inout/**", accessor.getDestination())) {
                    log.info(logPrefix + (logNum++) + " ::: preSend SUBSCRIBE - INOUT ###");
                    try {
                        String userId = accessor.getNativeHeader("userId").get(0);    //Optional.ofNullable((Principal) accessor.getUser()).map(Principal::getName).orElse("UnknownUser");
                        //                        if(userId.equals("") || userId.length() == 0) {
                        //                    		userId = cuslId;
                        //                        }
                        String userName = accessor.getNativeHeader("userName").get(0);
                        String userNickname = accessor.getNativeHeader("userNickname").get(0);
                        String talkContactId = accessor.getNativeHeader("chtCuttId").get(0);
                        String sndrKey = accessor.getNativeHeader("sndrKey").get(0);
                        String chatType = accessor.getNativeHeader("chatType").get(0);

                        //header정보에서 구독 destination정보를 얻고, userKey를 추출한다.
                        String userKey = stompRedisPublisher.getUserKey(Optional.ofNullable(accessor.getDestination()).orElse(
                            "InvalidRoomId"));

                        //INOUT:PLUSCOUNT:REDIS - 채팅방의 인원수를 +1한다. 
                        //                        redisChatInoutRepository.plusCount(userKey);

                        //INOUT:IN_OUT:ENTER [PUB] - 상담사입장 (구독)
                        log.info(logPrefix + (logNum++) + " ::: INOUT:IN_OUT:ENTER [PUB] - 상담사입장 (구독)");

                        // 제 3자 채팅 
                        if (chatType != null && "CONSULT".equals(chatType)) {
                            stompRedisPublisher.sendPubMessage(ChatMessage.builder().chatType(ChatMessage.ChatType.CONSULT).chatEvent(
                                ChatMessage.ChatEvent.IN_OUT).messageEvent(ChatMessage.MessageEvent.ENTER).userKey(userKey).userId(userId)
                                .userName(userName).userNickname(userNickname).contactId(talkContactId).senderKey(sndrKey).sessionId(
                                    sessionId).custcoId(custcoId).build());
                        } else {
                            stompRedisPublisher.sendPubMessage(ChatMessage.builder().chatType(ChatMessage.ChatType.INOUT).chatEvent(
                                ChatMessage.ChatEvent.IN_OUT).messageEvent(ChatMessage.MessageEvent.ENTER).userKey(userKey).userId(userId)
                                .userName(userName).userNickname(userNickname).contactId(talkContactId).senderKey(sndrKey).sessionId(
                                    sessionId).custcoId(custcoId).build());
                        }

                        log.info(logPrefix + (logNum++) + " :::preSend SUBSCRIBED - INOUT : {}:{}", userKey, userId);
                    } catch (Exception e) {
                        log.error(e.getLocalizedMessage(), e);
                    }
                } else {
                    log.info(logPrefix + (logNum++) + "preSend SUBSCRIBE ### {}", accessor.getDestination());
                }
                break;
            }
            //연결분리
            case DISCONNECT: {
                //STOMP:DISCONNECT - STOMP 연결분리
                log.info(logPrefix + (logNum++) + " ::: STOMP:DISCONNECT - STOMP 연결분리");

                //                //연결이 종료된 클라이언트 sesssionId로 채팅방 id를 얻는다.
                //                String sessionId = accessor.getSessionId();
                try {

                    //Optional.ofNullable((Principal) accessor.getUser()).map(Principal::getName).orElse("UnknownUser");
                    String userId = (String) accessor.getSessionAttributes().get(TalkWebSocketConstants.TELETALK_ASP_USER_ID);
                    //                    if(userId.equals("") || userId.length() == 0) {
                    //                		userId = cuslId;
                    //                    }
                    List<String> chatTypeList = accessor.getNativeHeader("chatType");
                    if (!ObjectUtils.isEmpty(chatTypeList)) {
                        ChatType chatType = ChatType.valueOf(chatTypeList.get(0));
                        switch (chatType) {
                            //대기
                            case READY: {
                                List<String> chatEventList = accessor.getNativeHeader("chatEvent");
                                if (!ObjectUtils.isEmpty(chatEventList)) {
                                    ChatEvent chatEvent = ChatEvent.valueOf(chatEventList.get(0));
                                    switch (chatEvent) {
                                        case CLEAR_DISCONNECT: {
                                            log.info(logPrefix + (logNum++) + " ::: DISCONNECT:READY:CLEAR_DISCONNECT");
                                            if (redisChatReadyRepository.hasKey(userId)) {
                                                TelewebJSON inJson = new TelewebJSON();
                                                inJson.setString("USER_ID", userId);
                                                inJson.setString("USER_STATUS_CD", chatProperties.getChatOff().name());

                                                //상담대기 삭제 처리
                                                stompReadyService.deleteReady(inJson);

                                                //READY:REMOVE:REDIS [SUB] - 상담대기 삭제
                                                log.info(logPrefix + (logNum++) + " ::: READY:REMOVE:REDIS [SUB] - 상담대기 삭제");
                                                redisChatReadyRepository.removeUserId(Integer.parseInt(userId));

                                            } else {
                                                log.info(logPrefix + (logNum++) + " ::: CLEAR_DISCONNECT:READY:REMOVE:REDIS:HASKEY : {}", userId);
                                            }
                                            break;
                                        }
                                        case DISCONNECT: {
                                            log.info(logPrefix + (logNum++) + " ::: DISCONNECT:READY:DISCONNECT");
                                            if (redisChatReadyRepository.hasKey(userId)) {
                                                //READY:DISCONNECT:SYSTEM [PUB] - 상담사대기 연결분리
                                                log.info(logPrefix + (logNum++) + " ::: READY:DISCONNECT:SYSTEM [PUB] - 상담사대기 연결분리");
                                                stompRedisPublisher.sendPubMessage(ChatMessage.builder().chatType(
                                                    ChatMessage.ChatType.READY).chatEvent(ChatMessage.ChatEvent.DISCONNECT).messageEvent(
                                                        ChatMessage.MessageEvent.SYSTEM).userId(userId).custcoId(custcoId).build());
                                            } else {
                                                log.info(logPrefix + (logNum++) + " ::: DISCONNECT:READY:REMOVE:REDIS:HASKEY : {}", userId);
                                            }
                                            break;
                                        }
                                        default: {
                                            log.info(logPrefix + (logNum++) + " ::: READY ::: Undefined ChatEvent COMMAND : {} ", chatEvent);
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                            //상담중
                            case INOUT: {
                                List<String> chatEventList = accessor.getNativeHeader("chatEvent");
                                if (!ObjectUtils.isEmpty(chatEventList)) {
                                    ChatEvent chatEvent = ChatEvent.valueOf(chatEventList.get(0));
                                    switch (chatEvent) {
                                        case CLEAR_DISCONNECT: {
                                            log.info(logPrefix + (logNum++) + " ::: INOUT ::: CLEAR_DISCONNECT");
                                            List<String> userKeyList = accessor.getNativeHeader("userKey");
                                            String userKey = userKeyList.get(0);
                                            if (redisChatInoutRepository.hasKey(userKey)) {
                                                //INOUT:REMOVE:REDIS - 클리어 세션 제거
                                                log.info(logPrefix + (logNum++) + " ::: INOUT:REMOVE:REDIS - 클리어 세션 제거");
                                                redisChatInoutRepository.removeUserKey(userKey);
                                                log.info(logPrefix + (logNum++) + " ::: preSend CLEAR_DISCONNECTED - FORCE QUIT : {}, {}", userKey, userId);
                                            } else {
                                                log.info(logPrefix + (logNum++) + " ::: CLEAR_DISCONNECT:INOUT:REMOVE:REDIS:HASKEY : {}, {}", userKey, userId);
                                            }
                                            break;
                                        }
                                        case DISCONNECT: {
                                            log.info(logPrefix + (logNum++) + " ::: INOUT ::: DISCONNECT :::");
                                            List<String> userKeyList = accessor.getNativeHeader("userKey");
                                            String userKey = userKeyList.get(0);
                                            if (redisChatInoutRepository.hasKey(userKey)) {
                                                ChatStompVO stompVO = redisChatInoutRepository.getStompVO(userKey);
                                                if (!ObjectUtils.isEmpty(stompVO)) {
                                                    //INOUT:MINUSCOUNT:REDIS - 채팅방의 인원수를 -1한다.
                                                    //                                                    redisChatInoutRepository.minusCount(roomId);

                                                    //클라이언트 채팅방에 발행한다.(redis publish)
                                                    //INOUT:DISCONNECT:QUIT [PUB] - 상담중 연결분리
                                                    log.debug(">>> INOUT:DISCONNECT:QUIT [PUB] - 상담중 연결분리");
                                                    stompRedisPublisher.sendPubMessage(ChatMessage.builder().chatType(
                                                        ChatMessage.ChatType.INOUT).chatEvent(ChatMessage.ChatEvent.DISCONNECT)
                                                        .messageEvent(ChatMessage.MessageEvent.QUIT).userId(userId).userKey(userKey)
                                                        .custcoId(custcoId).build());

                                                    //퇴장한 클라이언트의 roomId 맵핑 정보를 삭제한다.
                                                    //INOUT:REMOVE:REDIS - 상담중 세션 제거
                                                    log.debug("INOUT:REMOVE:REDIS - 상담중 세션 제거");
                                                    redisChatInoutRepository.removeUserKey(userKey);

                                                    log.debug("preSend DISCONNECTED - QUIT : {}, {}", userKey, userId);
                                                }
                                            } else {
                                                log.info(logPrefix + (logNum++) + " ::: DISCONNECT:INOUT:REMOVE:REDIS:HASKEY : {}, {}", userKey, userId);
                                            }
                                            break;
                                        }
                                        default: {
                                            log.info(logPrefix + (logNum++) + " ::: INOUT ::: Undefined ChatEvent COMMAND : {} ", chatEvent);
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                            //3자 채팅
                            case CONSULT: {
                                log.info(logPrefix + (logNum++) + " ::: CONSULT");
                                List<String> userKeyList = accessor.getNativeHeader("userKey");
                                String userKey = userKeyList.get(0);
                                String userName = accessor.getNativeHeader("userName").get(0);
                                String userNickname = accessor.getNativeHeader("userNickname").get(0);

                                if (redisChatInoutRepository.hasKey(userKey)) {
                                    ChatStompVO stompVO = redisChatInoutRepository.getStompVO(userKey);
                                    if (!ObjectUtils.isEmpty(stompVO)) {
                                        //INOUT:MINUSCOUNT:REDIS - 채팅방의 인원수를 -1한다.
                                        //                                        redisChatInoutRepository.minusCount(roomId);

                                        //클라이언트 채팅방에 발행한다.(redis publish)
                                        //INOUT:DISCONNECT:QUIT [PUB] - 상담중 연결분리
                                        log.info(logPrefix + (logNum++) + " ::: INOUT:DISCONNECT:QUIT [PUB] - 상담중 연결분리");
                                        stompRedisPublisher.sendPubMessage(ChatMessage.builder().chatType(ChatMessage.ChatType.CONSULT)
                                            .chatEvent(ChatMessage.ChatEvent.DISCONNECT).messageEvent(ChatMessage.MessageEvent.QUIT).userId(
                                                userId).userKey(userKey).userName(userName).userNickname(userNickname).custcoId(custcoId)
                                            .build());
                                        log.debug("preSend 3자채팅 DISCONNECTED - QUIT : {}, {}", userKey, userId);
                                    }
                                }
                                break;
                            }
                            default: {
                                log.info(logPrefix + (logNum++) + "Undefined ChatType COMMAND : {} ", chatType);
                                break;
                            }
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getLocalizedMessage(), e);
                }
                break;
            }
            default: {
                //메시지 발송 => 테넌트 구분
                //            	String tenantCode = String.valueOf(accessor.getNativeHeader("CERT-CUSTCO-TENANT-ID")).replaceAll("[","").replaceAll("]","");
                //            	TenantContext.setCurrentTenant(tenantCode);
                log.info(logPrefix + (logNum++) + "Undefined STOMP COMMAND : {} ", accessor.getCommand());
                break;
            }
        }

        return message;

    }

    /**
     * 발생한 예외에 관계없이 송신 완료 후 호출되므로 적절한 자원 정리가 가능합니다. preSend가 성공적으로 완료되고 메시지를 반환 한 경우에만 호출됩니다. 즉, null을 반환하지 않습니다.
     */
    @Override
    public void afterSendCompletion(Message<?> message, MessageChannel channel, boolean sent, Exception ex) {
        log.debug("afterSendCompletion :::");
    }
}
