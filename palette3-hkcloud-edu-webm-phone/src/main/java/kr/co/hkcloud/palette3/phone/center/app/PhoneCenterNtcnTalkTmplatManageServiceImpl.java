package kr.co.hkcloud.palette3.phone.center.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * <pre>
 * 시스템명 : 신협 공제 고객센터 시스템(S/W)
 * 업무구분 : 알림톡템플릿관리
 * 파 일 명 : PhoneCenterNtcnTalkTmplatManageServiceImpl.java
 * 작 성 자 : 현은지
 * 작 성 일 : 2021.04.29
 * 설    명 : 알림톡템플릿관리 서비스 구현체
 * --------------------------------------------------------------------------------
 * 변경일            변경자  변경내역
 * --------------------------------------------------------------------------------
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Service("phoneCenterNtcnTalkTmplatManageService")
public class PhoneCenterNtcnTalkTmplatManageServiceImpl implements PhoneCenterNtcnTalkTmplatManageService
{
    private final TwbComDAO mobjDao;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectNtcnTalkMngList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.phone.center.dao.PhoneCenterNtcnTalkTmplatManageMapper", "selectNtcnTalkMngList", mjsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertNtcnTalkMng(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .insert("kr.co.hkcloud.palette3.phone.center.dao.PhoneCenterNtcnTalkTmplatManageMapper", "insertNtcnTalkMng", mjsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateNtcnTalkMng(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .update("kr.co.hkcloud.palette3.phone.center.dao.PhoneCenterNtcnTalkTmplatManageMapper", "updateNtcnTalkMng", mjsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteNtcnTalkMng(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .delete("kr.co.hkcloud.palette3.phone.center.dao.PhoneCenterNtcnTalkTmplatManageMapper", "deleteNtcnTalkMng", mjsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectNtcnTalkMngCnt(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.phone.center.dao.PhoneCenterNtcnTalkTmplatManageMapper", "selectNtcnTalkMngCnt", mjsonParams);
    }

}
