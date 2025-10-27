package kr.co.hkcloud.palette3.config.event;


import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 팔래트 Application 의 준비가 완료되었을 때 발생하는 이벤트 리스너
 * 
 * @author Orange
 *
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class PaletteApplicationReadyEventListener
{
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent(ApplicationReadyEvent event)
    {
        log.info("=================================");
        log.info("Palette Application ready: {}", event);
        log.info("=================================");
    }
}
