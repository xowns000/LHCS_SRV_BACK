package kr.co.hkcloud.palette3.core.chat.stomp.app;


import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.core.chat.redis.dao.TalkRedisChatInoutRepository;
import kr.co.hkcloud.palette3.core.chat.stomp.domain.ChatStompVO;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 
 * @author Orange
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("talkStompInoutService")
public class TalkStompInoutServiceImpl implements TalkStompInoutService
{
    private final TwbComDAO                    mobjDao;
    private final TalkRedisChatInoutRepository chatInoutRepository;


    /**
     * 상담스택병합 및 상담시작(중) 삭제 처리
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public void processTalkStackIng(TelewebJSON inJson) throws TelewebAppException
    {
        //mergeTalkStack(inJson);
        //deleteTalkIng(inJson);
    	
    	mobjDao.insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "updateCuttSttsCd", inJson);
        //INOUT:GET:REDIS - 상담중
        String userKey = inJson.getString("CHT_USER_KEY");

        //INOUT:REMOVE:REDIS - 상담중 세션 제거
        chatInoutRepository.removeUserKey(userKey);
    }


    /**
     * 대기중삭제 처리
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public void processTalkStackReady(TelewebJSON inJson) throws TelewebAppException
    {

        //INOUT:GET:REDIS - 대기중 stack
        String userKey = inJson.getString("TALK_USER_KEY");

        //INOUT:REMOVE:REDIS - 대기중 세션 제거
        chatInoutRepository.removeUserKey(userKey);
    }


    /**
     * 상담 건수 등록
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    @Override
    public TelewebJSON mergeTalkStack(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "mergeTalkStack", mjsonParams);

        return objRetParams;
    }


    /**
     * 진행중 상담 삭제
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    @Override
    public TelewebJSON deleteTalkIng(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.delete("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "deleteTalkIng", mjsonParams);

        return objRetParams;
    }


    /**
     * 상담시작(중) 삽입과 마지막상담 병합 처리
     * 
     * @param TelewebJSON inJson
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   isolation = Isolation.READ_COMMITTED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = false)
    public void processTalkIngLast(TelewebJSON inJson) throws TelewebAppException
    {
        System.out.println("<<<<<<<<<<<<");
        System.out.println("<<<<<<<<<<<<inJson333333>>>>>>>>>>>>" + inJson);
        System.out.println(">>>>>>>>>>>>");

        //INOUT:INOUT_CONN - 고객연결완료 (상담중 시작)
        insertTalkIng(inJson);
//      mergeTalkLast(inJson);

//        TalkWebsocketChatInoutRepository.getInstance().addInOutConnection(talkWebsocketChatInoutServerEndpoint);

        String userKey = inJson.getString("TALK_USER_KEY");
        String userId = inJson.getString("USER_ID");
        chatInoutRepository.setStompVO(ChatStompVO.builder().agentId(userId).userKey(userKey).build());
    }


    /**
     * 진행 중 상담 등록
     * 
     * @Transactional         Auto Commit
     * @param         HashMap
     * @return                TelewebJSON 형식의 처리 결과 데이터
     */
    @Override
    public TelewebJSON insertTalkIng(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao.insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "insertTalkIng", mjsonParams);

        return objRetParams;
    }

}
