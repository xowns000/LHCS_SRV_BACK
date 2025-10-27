package kr.co.hkcloud.palette3.chat.status.app;


import org.springframework.stereotype.Service;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("ChatStatusAgentService")
public class ChatStatusAgentServiceImpl implements ChatStatusAgentService
{
    private final TwbComDAO twbComDAO;


    /**
     * 상담사모니터링 조회
     * 
     */
    @Override
    public TelewebJSON selectRtnAgentDashboard(TelewebJSON telewebJSON) throws TelewebAppException
    {
        TelewebJSON result = twbComDAO
            .select("kr.co.hkcloud.palette3.chat.status.dao.ChatStatusAgentMapper", "selectRtnAgentDashboard", telewebJSON);

        return result;
    }


    /**
     * 상담사모니터링현황 조회
     * 
     */
    @Override
    public TelewebJSON selectRtnAgentMonitoringStatus(TelewebJSON telewebJSON) throws TelewebAppException
    {
        TelewebJSON result = twbComDAO
            .select("kr.co.hkcloud.palette3.chat.status.dao.ChatStatusAgentMapper", "selectRtnAgentMonitoringStatus", telewebJSON);

        return result;
    }


    /**
     * 홈화면 sns상담현황
     * 
     */
    @Override
    public TelewebJSON selectChnStts(TelewebJSON telewebJSON) throws TelewebAppException
    {
        TelewebJSON result = twbComDAO
            .select("kr.co.hkcloud.palette3.chat.status.dao.ChatStatusAgentMapper", "selectChnStts", telewebJSON);

        return result;
    }


    /**
     * 오늘의 상담 현황 / 전일대비
     * 
     */
    @Override
    public TelewebJSON selectSttsToday(TelewebJSON telewebJSON) throws TelewebAppException
    {
        TelewebJSON result = twbComDAO
            .select("kr.co.hkcloud.palette3.chat.status.dao.ChatStatusAgentMapper", "selectSttsToday", telewebJSON);

        return result;
    }
    
    
}
