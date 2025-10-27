package kr.co.hkcloud.palette3.infra.palette.app;


import java.sql.SQLException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("TwbSystemNoticeService")
public class TwbSystemNoticeServiceImpl implements TwbSystemNoticeService
{
    private final TwbComDAO mobjDao;


    /**
     * 시스템공지사항 데이터 조회 템플릿
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 조회결과 데이터
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED,
                   rollbackFor = {Exception.class, SQLException.class},
                   readOnly = true)
    public TelewebJSON selectRtnChatbotSkillSystemNotice(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("com.hcteletalk.teletalk.mng.bbs.bbs02.dao.TwbBbs02Mapper", "selectRtnChatbotSkillSystemNotice", jsonParams);
    }
}