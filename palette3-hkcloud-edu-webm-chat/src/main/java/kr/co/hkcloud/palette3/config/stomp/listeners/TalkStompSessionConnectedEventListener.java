package kr.co.hkcloud.palette3.config.stomp.listeners;


import java.security.Principal;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.chat.router.dao.RoutingToAgentReadyDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Component
public class TalkStompSessionConnectedEventListener implements ApplicationListener<SessionConnectedEvent>
{
    private final InnbCreatCmmnService   innbCreatCmmnService;
    private final RoutingToAgentReadyDAO routingToAgentReadyDAO;
    private final TwbComDAO              mobjDao;


    @Override
    public void onApplicationEvent(SessionConnectedEvent sessionConnectedEvent)
    {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionConnectedEvent.getMessage());

        String userId = "";
        String simpsessionId = "";
        String headerAccessorStr = "";

        int strIndex = 0;
        int strLastIndex = 0;

        String tmpUserId = "";

        try {

            //userId = Optional.ofNullable((Principal) headerAccessor.getUser()).map(Principal::getName).orElse("UnknownUser");
            simpsessionId = headerAccessor.getSessionId();
            headerAccessorStr = headerAccessor.toString();

            strIndex = headerAccessorStr.indexOf("teletalk_asp_user_id=");
            strLastIndex = headerAccessorStr.indexOf("teletalk_httpSession.id");

            tmpUserId = headerAccessorStr.substring(strIndex, strLastIndex);

            String[] aryUserId = tmpUserId.replaceAll(",", "").replaceAll(" ", "").split("=");
            if(aryUserId.length > 0) {
                userId = aryUserId[1];
            } else {
            	userId = "2";
            }

            //if(log.isInfoEnabled()) {
            StringBuffer sb = new StringBuffer("\n");
            sb.append("\theaderAccessor(sessionConnectEvent.getMessage())\t\t:").append(headerAccessor.toString()).append("\n");
            sb.append("\tsimpsessionId)\t\t:").append(simpsessionId).append("\n");
            sb.append("\tuserId)\t\t:").append(userId).append("\n");
            log.debug("\n-------------------\nSTOMP TalkStompSessionConnectedEventListener onApplicationEvent INFORMATION\n-------------------\n{}\n", sb.toString());
            //}

            TelewebJSON jsonParams = new TelewebJSON();
            jsonParams.setString("USER_ID", userId);
            jsonParams.setString("USER_CHT_STAT", "CHT_WAIT");

            /*
             * // 웹소켓이 연결 된 경우 최종 상담원 상태로 판단하여 배정대기에 넣어줌 ( 웹소켓 단절등의 사유로 재연결시 필요함. ) sjh TelewebJSON objRetParams = mobjDao.select("kr.co.hkcloud.palette.main.dao.PaletteMainMapper", "selectTalkReadyCHATONUserId",
             * jsonParams); if (objRetParams.getInt("CNT") > 0) { String[] userStatusCds = objRetParams.getString("USER_CHT_STAT").split("_"); // CHATON_XXXX ( CHATON_ 뒤에 값이 대기유형 코드값임 )
             * jsonParams.setString("READY_TYPE", userStatusCds.length > 1 ? userStatusCds[1] : ""); routingToAgentReadyDAO.insertTalkReadyByUserId(jsonParams); }
             */

            // 이석상태 이력에도 넣어줄 수 있도록 로직 변경
            // 2020.10.08 umh
            TelewebJSON objRetParams = mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectTalkReadySTOMPOFFUserId", jsonParams);

            String cnt = objRetParams.getString("CNT");
            int iCnt = Integer.parseInt(cnt);

            if(iCnt > 0) {

                if("STOMPOFF".equals(objRetParams.getString("USER_CHT_STAT"))) {
                    jsonParams.setString("USER_CHT_STAT", "CHATON");
                }

                //1. 자동 채팅OFF 종료시간 업데이트
                mobjDao.update("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "updateTalkReadyOffInEndTime", jsonParams);

                //2. 채팅OFF 히스토리 기록
                jsonParams.setString("CHT_RDY_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_RDY_HSTRY_ID")));
                mobjDao.insert("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "insertTalkReadyOffHist", jsonParams);

                //3. 채팅OFF 초기화 (고객사 키 제거 로그아웃시 없음 ) 
                mobjDao.delete("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "deleteTalkReadyOff", jsonParams);

                //4. 채팅ON 등록
                mobjDao.insert("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "insertTalkReadyOff", jsonParams);

                //5. 배정대기 등록
                routingToAgentReadyDAO.insertTalkReadyByUserId(jsonParams);
            }
        }
        catch(Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }
    }
}
