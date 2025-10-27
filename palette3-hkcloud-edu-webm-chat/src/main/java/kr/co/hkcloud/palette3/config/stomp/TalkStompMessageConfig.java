package kr.co.hkcloud.palette3.config.stomp;


import kr.co.hkcloud.palette3.config.stomp.interceptors.TalkStompChannelInterceptor;
import kr.co.hkcloud.palette3.config.stomp.interceptors.TalkStompHandshakeInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;


/**
 * 
 * @author Orange
 *
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class TalkStompMessageConfig implements WebSocketMessageBrokerConfigurer
{
    private final TalkStompChannelInterceptor talkStompChannelInterceptor;
    private final TalkStompHandshakeInterceptor talkStompHandshakeInterceptor;


    /**
     * stomp websocket의 연결 endpoint는 /ws-stomp
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry)
    {
        registry.addEndpoint("/secured/ws-stomp").addInterceptors(talkStompHandshakeInterceptor).setAllowedOrigins("*").withSockJS()

        // .setStreamBytesLimit(512 * 1024)
        // .setHttpMessageCacheSize(1000)
        // .setDisconnectDelay(30 * 1000)
        ;
        //registry.setErrorHandler(errorHandler);
    }


    /**
     * pub/sub 메시징을 구현
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry)
    {
        //간단한 메시지 브로커를 사용하도록 설정하고 하나 이상의 접두사를 구성하여
        //브로커를 대상으로하는 목적지를 필터링하십시오 (예 : 접두사 "/ topic").
        registry.enableSimpleBroker("/topic", "/queue");     //sub(구독)

        //응용 프로그램 어노테이션이있는 메소드를 대상으로하는 대상을 필터링하도록
        //하나 이상의 접 두부를 구성하십시오.
        //예를 들어, "/ app"접두어가 붙은 대상은 주석이 달린 방법으로 처리 될 수 있지만
        //다른 대상은 메시지 브로커 (예 : "/ topic", "/ queue")를 대상으로 할 수 있습니다.
        //메시지가 처리 될 때 조회 경로를 형성하기 위해 대상에서 일치하는 접두사가 제거됩니다.
        //이는 주석에 대상 접두사를 포함해서는 안됨을 의미합니다.
        //후행 슬래시가없는 접두어에는 자동으로 추가됩니다.
        registry.setApplicationDestinationPrefixes("/app");  //pub(발행)
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration)
    {
        registration.interceptors(talkStompChannelInterceptor);
    }

}
