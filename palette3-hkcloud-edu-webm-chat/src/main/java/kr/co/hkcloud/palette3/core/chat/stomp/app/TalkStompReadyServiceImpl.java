package kr.co.hkcloud.palette3.core.chat.stomp.app;


import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("talkStompReadyService")
public class TalkStompReadyServiceImpl implements TalkStompReadyService
{
    private final TwbComDAO mobjDao;


    /**
     * 상담 대기 삽입
     */
    @Override
    public TelewebJSON insertReady(TelewebJSON mjsonParams) throws TelewebAppException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = mobjDao
            .insert("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "insertTalkReady", mjsonParams);

        return objRetParams;
    }


    /**
     * 상담 대기 삭제
     */
    @Override
    @Transactional(readOnly = false)
    public void deleteReady(TelewebJSON inJson) throws TelewebAppException
    {
        mobjDao.delete("kr.co.hkcloud.palette3.core.chat.router.dao.TalkRouteMapper", "deleteTalkReady", inJson);
    }

}
