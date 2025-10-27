package kr.co.hkcloud.palette3.core.chat.router.app;


import java.sql.SQLException;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.chat.router.dao.RoutingToAgentDAO;
import kr.co.hkcloud.palette3.core.chat.router.app.TalkDataProcessService;
import kr.co.hkcloud.palette3.core.chat.transfer.app.TransToKakaoService;
import kr.co.hkcloud.palette3.core.chat.transfer.domain.TransToEndTalkEvent;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("routerInformationMessageService")
public class RouterInformationMessageServiceImpl implements RouterInformationMessageService
{
    private final InnbCreatCmmnService      innbCreatCmmnService;
    private final TransToKakaoService       transToKakaoService;
    private final RoutingToAgentDAO         routingToAgentDAO;
    private final ApplicationEventPublisher eventPublisher;
    
    private TalkDataProcessService 		dataProcess;


    /**
     * 안내메시지 발송 메시지 조회
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectInfoMsg() throws TelewebAppException
    {
        return routingToAgentDAO.selectInfoMsg();
    }


    /**
     * 안내메세지 처리
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public void processInfoMsg(JSONObject talkInfoMsg) throws TelewebAppException
    {
        String talkUserKey = talkInfoMsg.getString("CHT_USER_KEY");
        String sndrKey = talkInfoMsg.getString("SNDR_KEY");
        String talkMsgTime = talkInfoMsg.getString("MSG_TIME");
        String callTypCd = talkInfoMsg.getString("CHN_CLSF_CD");

        TelewebJSON msgJson = new TelewebJSON();
        msgJson.setDataObject("DATA", talkInfoMsg);
        msgJson.setString("RCPTN_DSPTCH_CD", "SND");
        msgJson.setString("SYS_MSG_ID", "1");

        //안내메세지 발송
        transToKakaoService.sendSystemMsg(talkUserKey, sndrKey, msgJson, callTypCd);
        //dataProcess.insertTalkContactDetail(msgJson);

        //안내메세지 발송time update
        TelewebJSON setTimeJson = new TelewebJSON();
        setTimeJson.setString("TALK_USER_KEY", talkUserKey);
        setTimeJson.setString("CHT_USER_KEY", talkUserKey);
        setTimeJson.setString("MSG_TIME", talkMsgTime);
        routingToAgentDAO.updateTalkSetTime(msgJson);

        //2018.10.15 lsm 3번의 지연메시지 전송 후 상담종료
        if(talkInfoMsg.getInt("MSG_TIME") >= talkInfoMsg.getInt("MAX_TIME")) {
        	if(talkInfoMsg.getInt("ALTMNT_TIME") == talkInfoMsg.getInt("MAX_TIME")) {
	            routingToAgentDAO.delayByClose(talkInfoMsg);
	
	            JSONObject obj = new JSONObject();
	            obj.put("user_key", talkUserKey);
	            obj.put("sndrKey", sndrKey);
	            obj.put("CHT_CUTT_DTL_ID", innbCreatCmmnService.getSeqNo(setTimeJson, "MPC"));
	
	//            // endtalk ( 별도 쓰레드 처리 ) 
	//            new Thread(()->{
	//            	try {
	//            		transToKakaoService.transToKakao("endtalk", obj, callTypCd);
	//            	} catch(Exception e) {
	//            		e.printStackTrace();
	//            	}
	//            }).start();
	
	            // 스프링 이벤트로 처리 (transToKakaoService.transToEndTalkEvent)
	            eventPublisher.publishEvent(TransToEndTalkEvent.builder().active("endtalk").writeData(obj)
	                .callTypCd(callTypCd).build());
        	}
        }
    }
}
