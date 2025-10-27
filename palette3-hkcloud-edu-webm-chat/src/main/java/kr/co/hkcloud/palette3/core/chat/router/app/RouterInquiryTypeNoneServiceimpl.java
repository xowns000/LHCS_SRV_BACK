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
import kr.co.hkcloud.palette3.core.chat.transfer.app.TransToKakaoService;
import kr.co.hkcloud.palette3.core.chat.transfer.domain.TransToEndTalkEvent;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("routerInquiryTypeNoneService")
public class RouterInquiryTypeNoneServiceimpl implements RouterInquiryTypeNoneService
{
    private final InnbCreatCmmnService      innbCreatCmmnService;
    private final TransToKakaoService       transToKakaoService;
    private final RoutingToAgentDAO         routingToAgentDAO;
    private final ApplicationEventPublisher eventPublisher;
    private String className = "kr.co.hkcloud.palette3.core.chat.router.app.RouterInquiryTypeNoneServiceimpl";
    String logDevider = "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-";


    /**
     * 고객 문의유형 선택 안하는 고객 리스트
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectInqryTypeNoContact() throws TelewebAppException
    {
        return routingToAgentDAO.selectInqryTypeNoContact();
    }


    /**
     * 고객 문의유형 선택안함 처리
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public void processInqryTypeNoContact(JSONObject talkInfoMsg) throws TelewebAppException
    {
        String logPrefix = logDevider + ".processInqryTypeNoContact___" + talkInfoMsg.getString("CHT_USER_KEY") + "___" + talkInfoMsg.getString("CHN_CLSF_CD") + "___";
        int logNum = 1;
        log.info(logPrefix + (logNum++) + " ::: 고객 문의유형 선택안함 처리 start");
        log.info(logPrefix + (logNum++) + " ::: " + talkInfoMsg.toString());
        
        String talkUserKey = talkInfoMsg.getString("CHT_USER_KEY");
        String sndrKey = talkInfoMsg.getString("SNDR_KEY");
        String custcoId = talkInfoMsg.getString("CUSTCO_ID");
        String talkSystemMsg = talkInfoMsg.getString("MSG_CN");
//        String strSystemMsgId = talkInfoMsg.getString("SYS_MSG_ID");
        String strSystemMsgId = "9";
        String callTypCd = talkInfoMsg.getString("CHN_CLSF_CD");

        TelewebJSON msgJson = new TelewebJSON();
        msgJson.setDataObject("DATA", talkInfoMsg);

        //고객에게 문의유형 선택을 안해서 상담사에게 배정한다는 메시지 발송 
        //transToKakaoService.sendSystemMsg(talkUserKey, sndrKey, talkSystemMsg);
        log.info(logPrefix + (logNum++) + " ::: 고객에게 문의유형 선택을 안해서 상담사에게 배정한다는 메시지 발송 ::: ");
        log.info(logPrefix + (logNum++) + " ::: transToKakaoService.sendSystemMsg(talkUserKey, sndrKey, talkSystemMsg, strSystemMsgId, callTypCd, custcoId) ::: "
            + "\ntalkUserKey ::: " + talkUserKey
            + "\nsndrKey ::: " + sndrKey
            + "\ntalkSystemMsg ::: " + talkSystemMsg
            + "\nstrSystemMsgId ::: " + strSystemMsgId
            + "\ncallTypCd ::: " + callTypCd
            + "\ncustcoId ::: " + custcoId
            );
        transToKakaoService
            .sendSystemMsg(talkUserKey, sndrKey, talkSystemMsg, strSystemMsgId, callTypCd, custcoId);

        //시간 업데이트
        log.info(logPrefix + (logNum++) + " ::: 시간 업데이트 ::: ");
        routingToAgentDAO.updateMsgTimeByTalkReady(msgJson);

        if(talkInfoMsg.getInt("MSG_TIME") >= talkInfoMsg.getInt("MAX_TIME")) {
        	if(talkInfoMsg.getInt("NOQSTN_TIME") == talkInfoMsg.getInt("MAX_TIME")) {
	            boolean result = routingToAgentDAO.inqryTypeNoContactByClose(talkInfoMsg);
	
	            // 정상삭제 된 경우 session 만료 이벤트 발생 (sjh)
	            log.info(logPrefix + (logNum++) + " ::: 정상삭제 된 경우 session 만료 이벤트 발생 ::: result ::: " + result);
	            if(result) {
	                // kakao Session End
	                JSONObject obj = new JSONObject();
	                obj.put("user_key", talkUserKey);
	                obj.put("CHT_USER_KEY", talkUserKey);
	                obj.put("sndrKey", sndrKey);
	                obj.put("SNDR_KEY", sndrKey);
	                obj.put("CHT_CUTT_DTL_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID")));
	                log.info(logPrefix + (logNum++) + " ::: CHT_CUTT_DTL_ID ::: " + obj.get("CHT_CUTT_DTL_ID"));
	//                // endtalk ( 별도 쓰레드 처리 )
	//                new Thread(()->{
	//                	try {
	//                		transToKakaoService.transToKakao("endtalk", obj, callTypCd);
	//                	} catch(Exception e) {
	//                		e.printStackTrace();
	//                	}
	//                }).start();
	
	                // 스프링 이벤트로 처리 (transToKakaoService.transToEndTalkEvent)
	                log.info(logPrefix + (logNum++) + " ::: 스프링 이벤트로 처리 (transToKakaoService.transToEndTalkEvent) ::: " + obj);
	                eventPublisher.publishEvent(TransToEndTalkEvent.builder().active("endtalk").writeData(obj)
	                    .callTypCd(callTypCd).build());
	            }
        	}
        }
    }

}
