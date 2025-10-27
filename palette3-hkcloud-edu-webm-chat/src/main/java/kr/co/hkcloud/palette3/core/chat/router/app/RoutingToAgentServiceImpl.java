package kr.co.hkcloud.palette3.core.chat.router.app;


import java.sql.SQLException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.environment.HcTeletalkDbEnvironment;
import kr.co.hkcloud.palette3.config.properties.chat.ChatProperties;
import kr.co.hkcloud.palette3.core.chat.router.dao.RoutingToAgentDAO;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 
 * @author Orange
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("routingToAgentService")
public class RoutingToAgentServiceImpl implements RoutingToAgentService
{
    private final ChatProperties            chatProperties;
    private final RoutingToAgentDAO         routingToAgentDAO;
    private final RouterSendsToAgentService routerSendsToAgent;


    /**
     * 상담원 배분 처리 => 대기중인 상담원이 있는 경우만 처리되야 한다
     * 
     * @param maxAgentChat
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON processRoutingToAgent(JSONObject talkInfoMsg) throws TelewebAppException
    {
        int maxAgentChat = chatProperties.getMaxAgentChat();
        TelewebJSON inJson = new TelewebJSON();
        TelewebJSON routeJson = null;

        //고객문의유형 사용여부
        String inqryTypeYn = HcTeletalkDbEnvironment.getInstance().getString(talkInfoMsg.getString("CUSTCO_ID"), "INQRY_TYPE_YN");

        String talkUserKey = talkInfoMsg.getString("CHT_USER_KEY");
        String talkIqryCd = talkInfoMsg.getString("QSTN_TYPE_ID");
        String talkSerialNumber = talkInfoMsg.getString("CHT_RDY_ID");
        String sndrKey = talkInfoMsg.getString("SNDR_KEY");
        String custcoId = talkInfoMsg.getString("CUSTCO_ID");
        String talkDistDt = talkInfoMsg.getString("ALTMNT_RDY_REG_DT");

        inJson.setInt("MAX_AGENT_CHAT", maxAgentChat);
        inJson.setString("CHT_USER_KEY", talkUserKey);
        inJson.setString("QSTN_TYPE_CD", talkIqryCd);
        inJson.setString("QSTN_TYPE_ID", talkIqryCd);
        inJson.setString("CHT_RDY_ID", talkSerialNumber);
        inJson.setString("SNDR_KEY", sndrKey);
        inJson.setString("CUSTCO_ID", custcoId);
        inJson.setString("ALTMNT_RDY_REG_DT", talkDistDt);

        // 1.직전상담사 체크 유무에따라 직전상담 배분 먼저 수행 
        String strBeforeAgentUseYn = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "BEFORE_AGENT_USE_YN");
        if(strBeforeAgentUseYn != null && "Y".equals(strBeforeAgentUseYn)) {
            String strBeforeAgentPeriod = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "BEFORE_AGENT_PERIOD");
            inJson.setString("BEFORE_AGENT_PERIOD", strBeforeAgentPeriod);
            routeJson = routingToAgentDAO.readyTalkRouteToBeforeAgent(inJson);
        }

        // 2.직전상담원이 부재이거나 직전상담사 배정로직을 사용하지 않는 경우  일반 배정 로직 수행 (일반/전문)
        if(routeJson == null || routeJson.getDataObject().size() <= 0) {
            // 고객문의유형을 사용할 때
            if("Y".equals(inqryTypeYn)) {
                // 이미 등록되어 있는 지 체크 SPEC_CNSL_ROUTE
                // * SPEC_CNSL_ROUTE
                // - NORMAL
                //   전문상담사가 모두 상담중이면 다른 상담사에게도 배분한다
                // - ONLY_SPEC
                //   전문상담사가 모두 상담중이어도 대기한다
                String specCnslRoute = HcTeletalkDbEnvironment.getInstance().getString(custcoId, "SPEC_CNSL_ROUTE");
                if(specCnslRoute == null || "".equals(specCnslRoute)) {
                    log.debug("specCnslRoute :: {}", specCnslRoute);
                    specCnslRoute = "NORMAL"; //NORMAL로 적용
                }

                if("NORMAL".equals(specCnslRoute)) {
                    //상담사 조회
                    routeJson = routingToAgentDAO.readyTalkRouteToAgent(inJson);      // select -> ready 로 변경( 배정상담원 즉시 ready 상태 변경 ) 
                }
                else if("ONLY_SPEC".equals(specCnslRoute)) {
                    //전문상담사 조회 쿼리
                    routeJson = routingToAgentDAO.readyTalkRouteToSpecAgent(inJson);      // select -> ready 로 변경( 배정상담원 즉시 ready 상태 변경 ) 
                }
            }
            // 고객문의유형을 사용하지 않을 때
            else {
                //상담사 조회
                routeJson = routingToAgentDAO.readyTalkRouteToAgent(inJson);              // select -> ready 로 변경( 배정상담원 즉시 ready 상태 변경 ) 
            }
        }
        
        routingToAgentDAO.updateRoutingCutt(inJson);

        // 배정 알림 
        JSONArray userIdLoop = routeJson.getDataObject();
        int len = userIdLoop.size();

        log.trace("###### [RoutingToAgentServiceImpl.java][processRoutingToAgent] userIdLoop=" + userIdLoop);
        log.trace("###### [RoutingToAgentServiceImpl.java][processRoutingToAgent] routeJson=" + routeJson);
        log.trace("###### [RoutingToAgentServiceImpl.java][processRoutingToAgent] inJson=" + inJson);

        if(len != 0) {
            routerSendsToAgent.readyAlram(routeJson, inJson);
        }
        return routeJson;
    }


    /**
     * 지정 상담원 배분 처리 => 대기중인 상담원이 있는 경우만 처리되야 한다
     * 
     * @param maxAgentChat
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public TelewebJSON processRoutingToDesignatedAgent(JSONObject talkInfoMsg) throws TelewebAppException
    {
        // Properties env에서 사용시 3으로 디폴트
        int maxAgentChat = chatProperties.getMaxAgentChat();
        TelewebJSON inJson = new TelewebJSON();
        TelewebJSON routeJson = new TelewebJSON();

        //고객문의유형 사용여부
        String talkUserKey = talkInfoMsg.getString("TALK_USER_KEY");
        String talkIqryCd = talkInfoMsg.getString("TALK_INQRY_CD");
        String talkSerialNumber = talkInfoMsg.getString("TALK_SERIAL_NUMBER");
        String sndrKey = talkInfoMsg.getString("SNDR_KEY");
        String custcoId = talkInfoMsg.getString("CUSTCO_ID");
        String talkDistDt = talkInfoMsg.getString("TALK_DIST_DT");
        String designatedUserId = talkInfoMsg.getString("DESIGNATED_USER_ID");

        inJson.setInt("MAX_AGENT_CHAT", maxAgentChat);
        inJson.setString("TALK_USER_KEY", talkUserKey);
        inJson.setString("TALK_INQRY_CD", talkIqryCd);
        inJson.setString("TALK_SERIAL_NUMBER", talkSerialNumber);
        inJson.setString("SNDR_KEY", sndrKey);
        inJson.setString("CUSTCO_ID", custcoId);
        inJson.setString("TALK_DIST_DT", talkDistDt);
        inJson.setString("DESIGNATED_USER_ID", designatedUserId);

        routeJson = routingToAgentDAO.readyTalkRouteToDesignatedAgent(inJson);
        
        routingToAgentDAO.updateRoutingCutt(inJson);

        JSONArray userIdLoop = routeJson.getDataObject();
        if(userIdLoop.size() != 0) {
            //배정 알림처리
            routerSendsToAgent.readyAlram(routeJson, inJson);
        }
        return routeJson;
    }
}
