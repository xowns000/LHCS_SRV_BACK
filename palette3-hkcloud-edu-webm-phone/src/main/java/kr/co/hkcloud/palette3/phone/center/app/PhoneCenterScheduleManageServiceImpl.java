package kr.co.hkcloud.palette3.phone.center.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 스케쥴관리 서비스 구현체 클래스
 * 
 * @author dckim
 *
 */
@Slf4j
@RequiredArgsConstructor
@Service("phoneCenterScheduleManageService")
public class PhoneCenterScheduleManageServiceImpl implements PhoneCenterScheduleManageService
{
    private final TwbComDAO mobjDao;


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectSchedulMngList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.phone.center.dao.PhoneCenterScheduleManageMapper", "selectSchedulMngList", mjsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectSchedulHisList(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.phone.center.dao.PhoneCenterScheduleManageMapper", "selectSchedulHisList", mjsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateSchedulMng(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .update("kr.co.hkcloud.palette3.phone.center.dao.PhoneCenterScheduleManageMapper", "updateSchedulMng", mjsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateSchedulAlloc(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .update("kr.co.hkcloud.palette3.phone.center.dao.PhoneCenterScheduleManageMapper", "updateSchedulAlloc", mjsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON updateSchedulCsltId(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .update("kr.co.hkcloud.palette3.phone.center.dao.PhoneCenterScheduleManageMapper", "updateSchedulCsltId", mjsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON selectUserDeptSrch(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.phone.center.dao.PhoneCenterScheduleManageMapper", "selectUserDeptSrch", mjsonParams);
    }


    /**
     * 사용자 정보 목록 조회 팝업
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    @Override
    @Transactional(readOnly = false)
    public TelewebJSON selectRtnUserInfoPop(TelewebJSON mjsonParams) throws TelewebAppException
    {
        return mobjDao
            .select("kr.co.hkcloud.palette3.phone.history.dao.PhoneCenterScheduleManageMapper", "selectRtnUserInfoPop", mjsonParams);
    }

}
