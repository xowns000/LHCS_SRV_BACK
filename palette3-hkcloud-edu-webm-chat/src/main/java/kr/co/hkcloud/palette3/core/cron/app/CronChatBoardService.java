package kr.co.hkcloud.palette3.core.cron.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface CronChatBoardService {
    public TelewebJSON collectChatCuttBbs(TelewebJSON jsonParams) throws TelewebAppException;
    public TelewebJSON selectCustcoChannelBbsSettingList(TelewebJSON jsonParams) throws TelewebAppException;
    public TelewebJSON updateChtCuttId(TelewebJSON jsonParmas) throws TelewebAppException;

}
