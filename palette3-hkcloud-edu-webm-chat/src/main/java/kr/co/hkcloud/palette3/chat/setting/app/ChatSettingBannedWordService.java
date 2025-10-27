package kr.co.hkcloud.palette3.chat.setting.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface ChatSettingBannedWordService
{
    TelewebJSON getProhibiteWordList(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON getProhibiteWordDetail(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertProhibiteWord(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON putProhibiteWord(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteProhibiteWord(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON fbdwdDuplCheck(TelewebJSON jsonParams) throws TelewebAppException;
}
