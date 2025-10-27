package kr.co.hkcloud.palette3.core.chat.router.app;


import java.sql.SQLException;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbSystemMessage;
import kr.co.hkcloud.palette3.core.chat.msg.dao.TalkMsgDataDAO;
import kr.co.hkcloud.palette3.core.chat.redis.app.TalkRedisChatPublisherService;
import kr.co.hkcloud.palette3.core.chat.redis.dao.TalkRedisChatInoutRepository;
import kr.co.hkcloud.palette3.core.chat.router.dao.RoutingToAgentDAO;
import kr.co.hkcloud.palette3.core.chat.stomp.app.TalkStompSendToAgentService;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.ChatEvent;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.ChatType;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.MessageEvent;
import kr.co.hkcloud.palette3.core.chat.transfer.app.TransToKakaoService;
import kr.co.hkcloud.palette3.core.chat.transfer.domain.TransToEndTalkEvent;
import kr.co.hkcloud.palette3.core.util.PaletteFilterUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("routerNoCustomerResponseService")
public class RouterNoCustomerResponseServiceImpl implements RouterNoCustomerResponseService
{
    private final TalkDataProcessService              dataProcess;
    private final TransToKakaoService          transToKakaoService;
    private final TalkStompSendToAgentService  stompSendToAgentService;
    private final TalkRedisChatPublisherService       redisChatPublisher;
    private final TalkRedisChatInoutRepository chatInoutRepository;
    private final RoutingToAgentDAO            routingToAgentDAO;
    private final TalkMsgDataDAO               msgDataDao;
    private final PaletteFilterUtils           paletteFilterUtils;
    private final ApplicationEventPublisher    eventPublisher;
    private final InnbCreatCmmnService         innbCreatCmmnService;


    /**
     * 고객 무응답 메시지 조회
     * 
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectCustNoResponseMsg() throws TelewebAppException
    {
        return routingToAgentDAO.selectCustNoResponseMsg();
    }


    /**
     * 고객 무응답 메시지 처리
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public void processCustNoResponseMsg(JSONObject talkInfoMsg) throws TelewebAppException
    {
    	log.info("imhere@@@@@@@" + talkInfoMsg);
        String strTalkUserKey = talkInfoMsg.getString("CHT_USER_KEY");
        String strAspSenderKey = talkInfoMsg.getString("SNDR_KEY");
        String strCustcoId = talkInfoMsg.getString("CUSTCO_ID");
        String strUserId = talkInfoMsg.getString("USER_ID");
//        String strCustMsgTime = talkInfoMsg.getString("CUST_MSG_TIME");
        String callTypCd = talkInfoMsg.getString("CHN_CLSF_CD");
        String chtCuttDtlId = Integer.toString(innbCreatCmmnService.createSeqNo("CHT_CUTT_DTL_ID"));

        TelewebJSON msgJson = new TelewebJSON();
        msgJson.setDataObject("DATA", talkInfoMsg);
        msgJson.setString("TYPE", "system_noresponse");     // SJH 20181108 

        msgJson.setString("RCPTN_DSPTCH_CD", "SND");		//메시지 수발신코드(발신 고정)
        msgJson.setString("CHT_CUTT_DTL_ID", chtCuttDtlId);		//메시지 수발신코드(발신 고정)

        //고객에게 고객무응답메시지 발송 
        transToKakaoService.sendSystemMsg(strTalkUserKey, strAspSenderKey, msgJson, callTypCd);

        //dataProcess.insertTalkContactDetail(dtlJsonParam);    //고객 무응답 메시지 저장

        if(chatInoutRepository.hasKey(strTalkUserKey)) {
            TelewebJSON responseJson = new TelewebJSON();
            responseJson.setHeader("cust_noresponse_message_yn", "Y");
            responseJson.setHeader("code", 0);
            responseJson.setHeader("ERROR_FLAG", false);
            responseJson.setHeader("ERROR_MSG", talkInfoMsg.getString("MSG_CN"));
            responseJson.setString("user_key", strTalkUserKey);
            responseJson.setHeader("from_who", "fromweb");
            responseJson.setHeader("called_api", "/inOut");
            responseJson.setString("user_key", strTalkUserKey);
            responseJson.setString("CHT_CUTT_ID", msgJson.getString("CHT_CUTT_ID"));

            //INOUT:NO_CUSTOMER_RESPONSE:SYSTEM [PUB] - 상담중 고객무응답메시지
            log.trace(">>> INOUT:NO_CUSTOMER_RESPONSE:SYSTEM [PUB] - 상담중 고객무응답메시지");
            redisChatPublisher.sendPubMessage(ChatMessage.builder().chatType(ChatType.INOUT)
                .chatEvent(ChatEvent.NO_CUSTOMER_RESPONSE).messageEvent(MessageEvent.SYSTEM).userId(strUserId)
                .userKey(strTalkUserKey).telewebJsonString(paletteFilterUtils.filter(responseJson.toString()))
                .custcoId(strCustcoId).build());
            
        }

        //고객무응답메시지 발송 후 시간 업데이트
        routingToAgentDAO.updateCustNoResponeTime(msgJson);

        //TalkDataProcess.chatEndAfterLastCustNoResponse
        //마지막 고객무응답메시지 발송 후 종료
        //상담설정에서 고객무응답메시지 발송후 종료 Y로 설정되어 있을 때만 처리 talkInfoMsg
        String strCustChatEndYn = HcTeletalkDbEnvironment.getInstance().getString(strCustcoId, "CUST_NORESP_CHATEND");
        if("Y".equals(strCustChatEndYn)) {
            int strMaxTime = msgJson.getInt("MAX_TIME");
            int strMsgTime = msgJson.getInt("MSG_TIME");
            int strNorespTime = msgJson.getInt("NORESP_TIME");
            if(strMaxTime <= strMsgTime) {
            	//무응답종료시간 = 무응답 메시지 시간
            	//같지 않으면 무응답 시간 오류
            	if(strMaxTime == strNorespTime) {
	                //상담사에게 메시지 전송 (채팅중 상담사, 3자중인 상담사)
	                {
	                    TelewebJSON telewebJSON = new TelewebJSON();
	                    String userKey = msgJson.getString("CHT_USER_KEY");
	                    telewebJSON.setHeader("called_api", "/expired_noresponse");
	                    telewebJSON.setHeader("code", 0);
	                    telewebJSON.setHeader("ERROR_FLAG", false);
	                    telewebJSON.setHeader("ERROR_MSG", "");
	                    telewebJSON.setString("user_key", userKey);
	                    telewebJSON.setString("CHT_CUTT_DTL_ID", chtCuttDtlId); // 메시지 READ 여부 업데이트를 위해 SERIAL 값을 넘김.
	                    telewebJSON.setString("CHT_CUTT_ID", msgJson.getString("CHT_CUTT_ID"));
	
	                    //웹소켓이 정상적이지 않은 경우 무응답 종료 처리 ( 상태값만 업데이트 함 ), SJH 20181024
	                    //INOUT:EXPIRED_NORESPONSE:SYSTEM [PUB] - 상담중 고객무응답만료
	                    log.info(">>> INOUT:EXPIRED_NORESPONSE:SYSTEM [PUB] - 상담중 고객무응답만료");
	                    boolean result = stompSendToAgentService.sendToAgent(ChatMessage.builder().chatType(ChatType.INOUT)
	                        .chatEvent(ChatEvent.EXPIRED_NORESPONSE).messageEvent(MessageEvent.SYSTEM)
	                        .userId(msgJson.getString("USER_ID")).userKey(userKey).custcoId(strCustcoId)
	                        .build(), telewebJSON);
	                    if(!result) {
	                        msgDataDao.updateTalkContactPost(msgJson);
	                    }
	                    JSONObject obj = new JSONObject();
	                    obj.put("user_key", userKey);
	                    obj.put("CHT_USER_KEY", userKey);
	                    obj.put("sndrKey", strAspSenderKey);
	                    obj.put("SNDR_KEY", strAspSenderKey);
	                    obj.put("CHT_CUTT_DTL_ID", chtCuttDtlId);
	
	//                    // endtalk ( 별도 쓰레드 처리 ) 
	//                    new Thread(()->{
	//                    	try {
	//                    		transToKakaoService.transToKakao("endtalk", obj, callTypCd);
	//                    	} catch(Exception e) {
	//                    		e.printStackTrace();
	//                    	}
	//                    }).start();
	
	                    // 스프링 이벤트로 처리 (transToKakaoService.transToEndTalkEvent)
	                    eventPublisher.publishEvent(TransToEndTalkEvent.builder().active("endtalk").writeData(obj)
	                        .callTypCd(callTypCd).build());
	                }
	            }
            }
        }
        else if("N".equals(strCustChatEndYn)) {
            log.debug("CUST_NORESP_CHATEND ::: {}", strCustChatEndYn);
        }
        else {
            log.error("NO CUST_NORESP_CHATEND ::: {}", strCustChatEndYn);
        }
    }

}
