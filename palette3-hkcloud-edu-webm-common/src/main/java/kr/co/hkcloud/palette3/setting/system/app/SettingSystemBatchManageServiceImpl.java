package kr.co.hkcloud.palette3.setting.system.app;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.co.hkcloud.palette3.common.twb.dao.TwbComDAO;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@Service("settingSystemBatchManageService")
public class SettingSystemBatchManageServiceImpl implements SettingSystemBatchManageService
{
    private final TwbComDAO mobjDao;


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON INSERT_PLT_BAT(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.insert("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemBatchManageMapper", "INSERT_PLT_BAT", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON UPDATE_PLT_BAT(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.update("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemBatchManageMapper", "UPDATE_PLT_BAT", jsonParams);
    }


    @Override
    @Transactional(readOnly = false)
    public TelewebJSON DELETE_PLT_BAT(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.delete("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemBatchManageMapper", "DELETE_PLT_BAT", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON SELECT_PLT_BAT(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemBatchManageMapper", "SELECT_PLT_BAT", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON SELECT_PAGE_PLT_BAT(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemBatchManageMapper", "SELECT_PAGE_PLT_BAT", jsonParams);
    }


    @Override
    @Transactional(readOnly = true)
    public TelewebJSON SELECT_PLT_BAT_MNG(TelewebJSON jsonParams) throws TelewebAppException
    {
        return mobjDao.select("kr.co.hkcloud.palette3.setting.system.dao.SettingSystemBatchManageMapper", "SELECT_PLT_BAT_MNG", jsonParams);
    }

}
