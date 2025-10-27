package kr.co.hkcloud.palette3.config.stomp.listeners;


import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class TalkStompSessionConnectEventListener implements ApplicationListener<SessionConnectEvent>
{
    @Override
    public void onApplicationEvent(SessionConnectEvent sessionConnectEvent)
    {
        log.trace("TalkStompSessionConnectEventListener.onApplicationEvent ###");

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionConnectEvent.getMessage());

        //log.trace(headerAccessor.toString());

        //if(log.isInfoEnabled()) {
        StringBuffer sb = new StringBuffer("\n");
        sb.append("\theaderAccessor(sessionConnectEvent.getMessage())\t\t:").append(headerAccessor.toString()).append("\n");
        log.debug("\n-------------------\nSTOMP TalkStompSessionConnectEventListener onApplicationEvent INFORMATION\n-------------------\n{}\n", sb.toString());
        //}

    }
}
