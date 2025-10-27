package kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.kakaobzc.app;


import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.core.chat.messenger.hkcdv.kakaobzc.domain.KakaobzcOnExpiredSessionEvent;
import kr.co.hkcloud.palette3.core.chat.msg.app.TalkExpiredSessionService;
import kr.co.hkcloud.palette3.core.chat.redis.util.TalkRedisChatUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


/**
 * 세션만료 수신 서비스 인터페이스 구현체
 * 
 * @author Orange
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("hkcdvKakaobzcReceiveExpiredSession")
public class HkcdvKakaobzcReceiveExpiredSessionImpl implements HkcdvKakaobzcReceiveExpiredSession
{
    private static final String CALLED_API = "/expired_session";

    private final TalkRedisChatUtils        redisChatUtils;
    private final TalkExpiredSessionService talkExpiredSessionService;
    private final InnbCreatCmmnService         innbCreatCmmnService;
    private String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";

    /**
     * 세션만료 이벤트 수신
     * 
     * @praram            KakaobzcOnExpiredSessionEvent expiredSessionEvent
     * @throws  Exception
     * @version           5.0
     */
    @EventListener
    public void onExpiredSession(final KakaobzcOnExpiredSessionEvent expiredSessionEvent) throws TelewebAppException
    {
        String logPrefix = logDevider + ".onExpiredSession" + "___";
        int logNum = 1;
        JSONObject expiredSessionJson = expiredSessionEvent.getExpiredSessionJson();
        log.info(logPrefix + (logNum++) + " ::: onExpiredSession start - {}", expiredSessionJson.toString());

        String userKey = expiredSessionJson.getString("user_key");
        String senderKey = expiredSessionJson.getString("asp_sndrKey");
        String custcoId = expiredSessionJson.getString("custco_id");
        String chtCuttDtlId = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID"));

        TelewebJSON objParams = new TelewebJSON();
        objParams.setString("TALK_USER_KEY", userKey);
        objParams.setString("CHT_USER_KEY", userKey);
        objParams.setString("SNDR_KEY", senderKey);
        objParams.setString("CUSTCO_ID", custcoId);
        objParams.setString("CHN_CLSF_CD", expiredSessionJson.getString("call_typ_cd"));
        objParams.setString("CHT_CUTT_DTL_ID", chtCuttDtlId);
        
        expiredSessionJson.put("CUSTCO_ID", custcoId);
        expiredSessionJson.put("CHT_USER_KEY", userKey);
        expiredSessionJson.put("RCPTN_DSPTCH_CD", "RCV");
        expiredSessionJson.put("RCPTN_SNDPTY_ID", "2");
        expiredSessionJson.put("CHT_CUTT_DTL_ID", chtCuttDtlId);

        //해당건이 이관중인 지 체크한다.
//        boolean isReadyToContact = redisChatUtils.isAgentReadyToContact(userKey);	// redis check
        //이미 RDY 삭제된 경우 TalkBusyServiceImpl 의 스프링 이벤트로 처리 부분에서 상담 종료 / 상담 대기 삭제 / 상담 이력 업데이트가 먼저 실행 후 호출되면 isUserReadyStates=false 임.
        boolean isUserReadyStates = redisChatUtils.isUserReadyStates(objParams);	// db 값만 체크 (sjh )
        log.info(logPrefix + (logNum++) + " ::: isUserReadyStates - {}", isUserReadyStates);

        if(isUserReadyStates)		// db 값 체크로만 변경 (redis 는 ready 상태에서도 열림) , sjh 20200414
        {
            //세션만료 UPDATE 처리
            log.info(logPrefix + (logNum++) + " ::: 세션만료 UPDATE 처리 - ", objParams);
            talkExpiredSessionService.updadateExpiredSessionReady(objParams);

            //고객의 접수포기, 대기포기, 전달대기포기 건을 세션만료
            log.info(logPrefix + (logNum++) + " ::: 고객의 접수포기, 대기포기, 전달대기포기 건을 세션만료 - ", expiredSessionJson);
            talkExpiredSessionService.processExpiredSessionReady(objParams, CALLED_API, expiredSessionJson);
        } else {
        	//문의유형에서 메시지형 버튼을 클릭한 경우
        	//문의유형 처리에 의해 상담 대기테이블에서 삭제됨 - isUserReadyStates = false
        	//상담대기테이블이 아닌 상담테이블에서 상담 정보를 체크해야함
        	boolean isUserCuttStates = redisChatUtils.isUserCuttStates(objParams);
        	if(isUserCuttStates) {
        		redisChatUtils.updadateExpiredSessionCutt(objParams);
        	}
        }

        //상담중인 건을 세션만료
        log.info(logPrefix + (logNum++) + " ::: 상담중인 건을 세션만료 ");
        talkExpiredSessionService.processExpiredSessionContract(objParams, CALLED_API, expiredSessionJson);
    }

}
