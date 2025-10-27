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
 * 업무구분 : SMS관리
 * 파 일 명 : PhoneCenterSMSManageServiceImpl.java
 * 작 성 자 : 김대찬
 * 작 성 일 : 2020.01.06
 * 설    명 : SMS관리 서비스 구현체 클래스
 * --------------------------------------------------------------------------------
 * 변경일            변경자  변경내역
 * --------------------------------------------------------------------------------
 * </pre>
 */
@Slf4j
@RequiredArgsConstructor
@Service("phoneCenterSMSManageService")
public class PhoneCenterSMSManageServiceImpl implements PhoneCenterSMSManageService
{
    private final TwbComDAO mobjDao;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectSmsMngList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.phone.center.dao.PhoneCenterSMSManageMapper", "selectSmsMngList", mjsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertSmsMng(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .insert("kr.co.hkcloud.palette3.phone.center.dao.PhoneCenterSMSManageMapper", "insertSmsMng", mjsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateSmsMng(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .update("kr.co.hkcloud.palette3.phone.center.dao.PhoneCenterSMSManageMapper", "updateSmsMng", mjsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON deleteSmsMng(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .delete("kr.co.hkcloud.palette3.phone.center.dao.PhoneCenterSMSManageMapper", "deleteSmsMng", mjsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectSmsSndCdList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.phone.center.dao.PhoneCenterSMSManageMapper", "selectSmsSndCdList", mjsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectSmsMngCnt(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.phone.center.dao.PhoneCenterSMSManageMapper", "selectSmsMngCnt", mjsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON insertSmsSendHist(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .insert("kr.co.hkcloud.palette3.phone.center.dao.PhoneCenterSMSManageMapper", "insertSmsSendHist", mjsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectScrDispList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.phone.center.dao.PhoneCenterSMSManageMapper", "selectScrDispList", mjsonParams);
    }
}
