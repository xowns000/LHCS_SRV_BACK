
package kr.co.hkcloud.palette3.config.stomp.listeners;


import java.security.Principal;
import java.util.Optional;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import kr.co.hkcloud.palette3.common.innb.app.InnbCreatCmmnService;
import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.chat.router.dao.RoutingToAgentReadyDAO;

// import com.hcteletalk.teletalk.core.redis.dao.TalkRedisChatEscalatingRepository;
// import com.hcteletalk.teletalk.core.redis.dao.TalkRedisChatInoutRepository;
// import com.hcteletalk.teletalk.core.redis.dao.TalkRedisChatReadyRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Component
public class TalkStompSessionDisconnectedEventListener implements ApplicationListener<SessionDisconnectEvent>
{
//    private final TalkRedisChatReadyRepository      redisChatReadyRepository;
//    private final TalkRedisChatInoutRepository      redisChatInoutRepository;
//    private final TalkRedisChatEscalatingRepository redisChatEscalatingRepository;
    private final RoutingToAgentReadyDAO routingToAgentReadyDAO;
    private final TwbComDAO              mobjDao;
    private final InnbCreatCmmnService   innbCreatCmmnService;


    @SuppressWarnings("static-access")
    @Override
    public void onApplicationEvent(SessionDisconnectEvent sessionDisconnectEvent)
    {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(sessionDisconnectEvent.getMessage());
        log.debug("TalkStompSessionDisconnectedEventListener.onApplicationEvent ###\n{}", headerAccessor.toString());

        TelewebJSON objRetSessionParams = new TelewebJSON();

        String userId = "";
        String simpsessionId = "";
        String headerAccessorStr = "";

        int strIndex = 0;
        int strLastIndex = 0;

        String tmpUserId = "";

        try {

            userId = Optional.ofNullable((Principal) headerAccessor.getUser()).map(Principal::getName).orElse("UnknownUser");
            simpsessionId = headerAccessor.getSessionId();

            headerAccessorStr = headerAccessor.toString();

            strIndex = headerAccessorStr.indexOf("teletalk_asp_user_id=");
            strLastIndex = headerAccessorStr.indexOf("teletalk_httpSession.id");

            tmpUserId = headerAccessorStr.substring(strIndex, strLastIndex);

            String[] aryUserId = tmpUserId.replaceAll(",", "").replaceAll(" ", "").split("=");

            if(userId.equals("UnknownUser")) {

                if(aryUserId.length > 0) {
                    userId = aryUserId[1];
                }
            }

            // 웹소켓이 끊긴 경우 배정대기(talk_ready) 에서 무조건제거 , 브라우저 다운등으로 배정대기로 남아있어 실제 로그인이 되지 않더라도 배정이 되고있음.sjh
            TelewebJSON inJson = new TelewebJSON();
            inJson.setString("USER_ID", userId);
            inJson.setString("USER_CHT_STAT", "STOMPOFF");

            //웹소켓 단절 시 채팅대기테이블(PLT_CHT_RDY)에서 사용자 삭제
            //이후 웹소켓이 재연결되어도 채팅대기테이블에 들어가지 않아 상담사 정상 배정이 되지 않음
            //웹소켓 단절 시 발생하는 이벤트 로직에서 해당 데이터 DELETE를 두번 하면서 버그 발생
//            routingToAgentReadyDAO.deleteTalkReady(inJson);

            //이석상태 이력에도 넣어줄 수 있도록 로직 추가
            // 2020.10.08 umh

            //TelewebJSON objRetParams = mobjDao.select("com.hcteletalk.teletalk.main.hkcdv.dao.TwbMainMapper", "selectTalkReadyOffUserId", inJson);
            //웹소켓 단절 시 채팅대기테이블(PLT_CHT_RDY)에서 사용자 삭제 한번 더(중복)
            objRetSessionParams = routingToAgentReadyDAO.deleteTalkReadySession(inJson);
            int rslcnt = objRetSessionParams.getHeaderInt("COUNT");

            if(rslcnt > 0) {

                inJson.setString("USER_CHT_STAT", "STOMPOFF");

                //routingToAgentReadyDAO.deleteTalkReady(inJson);

                //이석상태 이력에도 넣어줄 수 있도록 로직 추가
                // 2020.10.08 umh

                //TelewebJSON objRetParams = mobjDao.select("com.hcteletalk.teletalk.main.hkcdv.dao.TwbMainMapper", "selectTalkReadyOffUserId", inJson);
                TelewebJSON objRetParams = mobjDao.select("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "selectTalkReadyOffCHATONUserId", inJson);
                if(objRetParams.getInt("CNT") > 0) {

                    if("CHATON".equals(objRetParams.getString("USER_CHT_STAT"))) {
                        inJson.setString("USER_CHT_STAT", "STOMPOFF");
                    }

                    //1. 채팅OFF 종료시간 업데이트
                    mobjDao.update("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "updateTalkReadyOffInEndTime", inJson);

                    //2. 채팅OFF 히스토리 기록
                    inJson.setString("CHT_RDY_HSTRY_ID", Integer.toString(innbCreatCmmnService.createSeqNo("CHT_RDY_HSTRY_ID")));
                    mobjDao.insert("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "insertTalkReadyOffHist", inJson);

                    //3. 채팅OFF 초기화 (고객사 키 제거 로그아웃시 없음 ) 
                    mobjDao.delete("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "deleteTalkReadyOff", inJson);

                    //4. 자동 채팅OFF 등록
                    mobjDao.insert("kr.co.hkcloud.palette3.main.dao.PaletteMainMapper", "insertTalkReadyOff", inJson);
                }

            }
        }
        catch(Exception e) {
            log.error(e.getLocalizedMessage(), e);
        }

//        //레디스 세션을 정리해 준다?
//        try
//        {
//            //READY
//            log.debug("DISCONNECTED:READY:REDIS:SIZE={}", redisChatReadyRepository.getSize());
//            
//            //ESCALATING
//            log.debug("DISCONNECTED:ESCALATING:REDIS:SIZE={}", redisChatEscalatingRepository.getSize());
//            
//            //INOUT
//            log.debug("DISCONNECTED:INOUT:REDIS:SIZE={}", redisChatInoutRepository.getSize());
//        }
//        catch (Exception e)
//        {
//            log.error(e.getLocalizedMessage(), e);
//        }
    }
}
