package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.linebzc.app;


import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.linebzc.domain.LinebzcOnExpiredSessionEvent;
import kr.co.hkcloud.palette3.core.chat.msg.app.TalkExpiredSessionService;
import kr.co.hkcloud.palette3.core.chat.redis.util.TalkRedisChatUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


/**
 * 세션만료 수신 인터페이스 구현체
 * 
 * @author User
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("hkcdvLinebzcReceiveExpiredSession")
public class HkcdvLinebzcReceiveExpiredSessionImpl implements HkcdvLinebzcReceiveExpiredSession
{
    private static final String CALLED_API = "/expired_session";

    private final TalkRedisChatUtils        redisChatUtils;
    private final TalkExpiredSessionService talkExpiredSessionService;


    /**
     * 세션만료 이벤트 수신
     * 
     * @praram            TtalkOnExpiredSessionEvent expiredSessionEvent
     * @throws  Exception
     * @version           5.0
     */
    @EventListener
    public void onExpiredSession(final LinebzcOnExpiredSessionEvent expiredSessionEvent) throws TelewebAppException
    {
        JSONObject expiredSessionJson = expiredSessionEvent.getExpiredSessionJson();
        log.info("onExpiredSession - {}", expiredSessionJson.toString());

        String userKey = expiredSessionJson.getString("user_key");
        String senderKey = expiredSessionJson.getString("asp_sndrKey");
        String custcoId = expiredSessionJson.getString("custco_id");

        TelewebJSON objParams = new TelewebJSON();
        objParams.setString("TALK_USER_KEY", userKey);
        objParams.setString("SNDR_KEY", senderKey);
        objParams.setString("CUSTCO_ID", custcoId);

        //해당건이 이관중인 지 체크한다.
        //boolean isReadyToContact = redisChatUtils.isAgentReadyToContact(userKey);
        boolean isUserReadyStates = redisChatUtils.isUserReadyStates(objParams);	// db 값만 체크 (sjh )

        log.info("isReadyToContact={}", isUserReadyStates);

        if(isUserReadyStates)		// db 값 체크로만 변경 (redis 는 ready 상태에서도 열림) , sjh 20200414
        {
            //세션만료 UPDATE 처리
            talkExpiredSessionService.updadateExpiredSessionReady(objParams);

            //고객의 접수포기, 대기포기, 전달대기포기 건을 세션만료
            talkExpiredSessionService.processExpiredSessionReady(objParams, CALLED_API, expiredSessionJson);
        }

        //상담중인 건을 세션만료
        talkExpiredSessionService.processExpiredSessionContract(objParams, CALLED_API, expiredSessionJson);
    }

}
