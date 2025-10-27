package kr.co.hkcloud.palette3.config.event;

import kr.co.hkcloud.palette3.core.chat.router.app.RoutingToAgentManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 채팅 Application 의 준비가 완료되었을 때 발생하는 이벤트 리스너
 *
 * @author Orange
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ChatApplicationReadyEventListener
{
    private final RoutingToAgentManagerService routingToAgentManagerService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent(ApplicationReadyEvent event)
    {

        // TeletalkRouterWebListener 에서 TELETALK관련 레디스데이터 초기세팅.
    }
}
