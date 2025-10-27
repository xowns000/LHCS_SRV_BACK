package kr.co.hkcloud.palette3.config.stomp.listeners;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TalkStompSessionUnSubscribeEventListener implements ApplicationListener<SessionUnsubscribeEvent>
{
    @Override
    public void onApplicationEvent(SessionUnsubscribeEvent sessionUnSubscribeEvent)
    {
        log.trace("TalkStompSessionUnSubscribeEventListener.onApplicationEvent ###");
        
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionUnSubscribeEvent.getMessage());
        log.trace(headerAccessor.toString());
    }
}
