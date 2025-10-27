package kr.co.hkcloud.palette3.common.chat.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 채팅공통 서비스 인터페이스 구현체
 * 
 * @author Orange
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("chatCmmnService")
public class ChatCmmnServiceImpl implements ChatCmmnService
{
    private final TwbComDAO mobjDao;


    /**
     * 채팅전달 에이전트 조회
     * 
     * @param  jsonParams
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnPageAgentDeliver(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.common.chat.dao.ChatCmmnMapper", "selectRtnPageAgentDeliver", jsonParams);
    }


    /**
     * 상담설정정보 리스트 조회
     * 
     * @param  jsonParams
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectRtnCnslProp(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.common.chat.dao.ChatCmmnMapper", "selectRtnCnslProp", jsonParams);
    }


    /**
     * 업무 시작시간 조회
     * 
     * @param  jsonParams
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectSTJobTime(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.common.chat.dao.ChatCmmnMapper", "selectSTJobTime", jsonParams);
    }


    /**
     * 업무 종료시간 조회
     * 
     * @param  jsonParams
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectENDJobTime(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.common.chat.dao.ChatCmmnMapper", "selectENDJobTime", jsonParams);
    }

}
