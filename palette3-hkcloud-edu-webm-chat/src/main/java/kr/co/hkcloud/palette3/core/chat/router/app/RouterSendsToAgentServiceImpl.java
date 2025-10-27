package kr.co.hkcloud.palette3.core.chat.router.app;


import java.sql.SQLException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.chat.redis.app.TalkRedisChatPublisherService;
import kr.co.hkcloud.palette3.core.chat.redis.dao.TalkRedisChatInoutRepository;
import kr.co.hkcloud.palette3.core.chat.router.dao.RoutingToAgentDAO;
import kr.co.hkcloud.palette3.core.chat.router.dao.RoutingToAgentReadyDAO;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.ChatEvent;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.ChatType;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatMessage.MessageEvent;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatStompVO;
import kr.co.hkcloud.palette3.core.util.PaletteFilterUtils;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;


@Slf4j
@RequiredArgsConstructor
@Service("routerSendsToAgentService")
public class RouterSendsToAgentServiceImpl implements RouterSendsToAgentService
{
    private final TalkRedisChatPublisherService redisChatPublisher;
    private final TalkRedisChatInoutRepository  redisChatInoutRepository;
    private final RoutingToAgentDAO             routingToAgentDAO;
    private final RoutingToAgentReadyDAO        routingToAgentReadyDAO;
    private final PaletteFilterUtils            paletteFilterUtils;


    /**
     * 상담사에게 고객배분건을 배정하고 알람을 보낸다.
     * 
     * @param  routeJson
     * @param  inJson
     * @throws TelewebAppException
     */
    public void readyAlram(TelewebJSON routeJson, TelewebJSON inJson) throws TelewebAppException
    {
        log.trace("readyAlram :::");

        TelewebJSON outJson = new TelewebJSON();
        String userId = ((JSONObject) (routeJson.getDataObject().get(0))).getString("USER_ID");
        ((JSONObject) (inJson.getDataObject().get(0))).put("USER_ID", userId);

        // 조회 즉시 ready 하므로 , 여기서 제외 처리함. ( 싱크 문제로 변경함 )
        //outJson = updateTalkUserReady(inJson);

        // 상담 대기 건수 조회 (11, 12, 13)
        ((JSONObject) (inJson.getDataObject().get(0))).put("USER_ID", userId);
        outJson = routingToAgentReadyDAO.selectTalkUserReadyAgent(inJson);
        String cnt = ((JSONObject) (outJson.getDataObject().get(0))).getString("CNT");

        outJson = routingToAgentDAO.selectTalkUserReadyInfo(inJson);  //배분상세조회

        TelewebJSON responseJson = new TelewebJSON();
        responseJson.setHeader("called_api", "/readyAlram");
        responseJson.setHeader("ready_count", cnt);

        log.info("###### [RouterSendsToAgentServiceImpl.java][readyAlram] cnt=" + cnt);
        log.info("###### [RouterSendsToAgentServiceImpl.java][readyAlram] outJson=" + outJson);

        if(outJson.getHeaderInt("TOT_COUNT") > 0) {
            for(int k = 0; k < outJson.getHeaderInt("TOT_COUNT"); k++) {
                responseJson.setString("CHT_USER_KEY", k, outJson.getString("CHT_USER_KEY", k));
                responseJson.setString("CUTT_RDY_REG_DT", k, outJson.getString("CUTT_RDY_REG_DT", k));
                responseJson.setString("CUTT_TTL", k, outJson.getString("CUTT_TTL", k));
                responseJson.setString("ALTMNT_STTS_CD", k, outJson.getString("ALTMNT_STTS_CD", k));
                responseJson.setString("CUST_ID", k, outJson.getString("CUST_ID", k));
                responseJson.setString("CUST_NM", k, outJson.getString("CUST_NM", k));
                responseJson.setString("CHN_TYPE_CD", k, outJson.getString("CHN_TYPE_CD", k));

                responseJson.setString("SNDR_KEY", k, outJson.getString("SNDR_KEY", k));

                String talkUserKey = outJson.getString("CHT_USER_KEY", k);
                //INOUT:SET:REDIS - 대기중 sjh
                log.trace("<<< INOUT:SET:REDIS - 대기중");
                redisChatInoutRepository.setStompVO(ChatStompVO.builder().userKey(talkUserKey).agentId(userId).build());
            }
        }

        String message = String.format("");
        responseJson.setHeader("code", 0);
        responseJson.setHeader("ERROR_FLAG", false);
        responseJson.setHeader("ERROR_MSG", message);

        //READY:READY_ALRAM:SYSTEM [PUB] - 대기중 배정알람
        log.trace(">>> READY:READY_ALRAM:SYSTEM [PUB] - 대기중 배정알람");
        redisChatPublisher.sendPubMessage(ChatMessage.builder().chatType(ChatType.READY).chatEvent(ChatEvent.READY_ALRAM).messageEvent(MessageEvent.SYSTEM).userId(userId)
            .telewebJsonString(paletteFilterUtils.filter(responseJson.toString())).build());
    }


    /**
     * 상담원 배분 처리 ( 전달건 ) => 대기중인 상담원이 있는 경우만 처리되야 한다
     * 
     * @param maxAgentChat
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public void transReadyAlram(String chkUserId) throws TelewebAppException
    {
        log.trace("transReadyAlram :::");

        TelewebJSON inJson = new TelewebJSON();
        TelewebJSON outJson = new TelewebJSON();

        ((JSONObject) (inJson.getDataObject().get(0))).put("USER_ID", chkUserId);

        //전달 요청건이 있는지 조회한다.(count)
        outJson = routingToAgentDAO.selectTalkUserReadyAgentNot11(inJson); //12,13
        String chkCnt = ((JSONObject) (outJson.getDataObject().get(0))).getString("CNT");
        if(Integer.valueOf(chkCnt) == 0) {
            log.trace("전달 요청건 count={}", chkCnt);
            return;
        }

        outJson = routingToAgentDAO.selectTalkUserReadyInfo(inJson);

        TelewebJSON respJson = new TelewebJSON();
        respJson.setHeader("called_api", "/readyAlram");
        respJson.setHeader("ready_count", chkCnt);

        if(outJson.getHeaderInt("TOT_COUNT") > 0) {
            for(int i = 0; i < outJson.getHeaderInt("TOT_COUNT"); i++) {
                respJson.setString("CHT_USER_KEY", i, outJson.getString("CHT_USER_KEY", i));
                respJson.setString("CUTT_RDY_REG_DT", i, outJson.getString("CUTT_RDY_REG_DT", i));
                respJson.setString("CUTT_TTL", i, outJson.getString("CUTT_TTL", i));
                respJson.setString("ALTMNT_STTS_CD", i, outJson.getString("ALTMNT_STTS_CD", i));
                respJson.setString("CUST_ID", i, outJson.getString("CUST_ID", i));
                respJson.setString("EXEC_RST_CD", i, outJson.getString("PRCS_RSLT_CD", i));  // 콜백 ( 전달시 값 ) SJH

                respJson.setString("CUST_NM", i, "");
                respJson.setString("CUST_ID", i, outJson.getString("CUST_ID", i));
            }

            respJson.setHeader("code", 0);
            respJson.setHeader("ERROR_FLAG", false);
            respJson.setHeader("ERROR_MSG", "");

            //READY:TRANS_READY_ALRAM:SYSTEM [PUB] - 상담중 전달배정알람
            log.trace(">>> READY:TRANS_READY_ALRAM:SYSTEM [PUB] - 상담중 전달배정알람");
            redisChatPublisher.sendPubMessage(ChatMessage.builder().chatType(ChatType.READY).chatEvent(ChatEvent.TRANS_READY_ALRAM).messageEvent(MessageEvent.SYSTEM).userId(chkUserId)
                .telewebJsonString(paletteFilterUtils.filter(respJson.toString())).build());
        }
    }
}
