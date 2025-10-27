package kr.co.hkcloud.palette3.setting.board.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * @author Orange
 *
 */
public interface SettingBoardManageService
{
    TelewebJSON selectRtn(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON processRtn(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON deleteRtn(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON selectRtNotiRtn(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON selectRtnDupBrdMaster(TelewebJSON mjsonParams) throws TelewebAppException;
    TelewebJSON selectRtnBrdDataCnt(TelewebJSON mjsonParams) throws TelewebAppException;
}
