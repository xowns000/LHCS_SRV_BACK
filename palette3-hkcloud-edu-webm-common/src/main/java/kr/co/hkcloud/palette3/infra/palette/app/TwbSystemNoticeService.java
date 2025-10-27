package kr.co.hkcloud.palette3.infra.palette.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/*
 * 서비스설정 서비스
 */
public interface TwbSystemNoticeService
{
    TelewebJSON selectRtnChatbotSkillSystemNotice(TelewebJSON jsonParams) throws TelewebAppException;
}
