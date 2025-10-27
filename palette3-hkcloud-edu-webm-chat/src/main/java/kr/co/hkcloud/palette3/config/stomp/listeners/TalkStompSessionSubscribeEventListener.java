package kr.co.hkcloud.palette3.config.stomp.listeners;


import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class TalkStompSessionSubscribeEventListener implements ApplicationListener<SessionSubscribeEvent>
{
    @Override
    public void onApplicationEvent(SessionSubscribeEvent sessionSubscribeEvent)
    {
        log.info("TalkStompSessionSubscribeEventListener.onApplicationEvent ###");

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionSubscribeEvent.getMessage());

        //if(log.isInfoEnabled()) {
        StringBuffer sb = new StringBuffer("\n");
        sb.append("\theaderAccessor(sessionConnectEvent.getMessage())\t\t:").append(headerAccessor.toString()).append("\n");
        log.debug("\n-------------------\nSTOMP TalkStompSessionSubscribeEventListener onApplicationEvent INFORMATION\n-------------------\n{}\n", sb.toString());
        //}
    }
}
