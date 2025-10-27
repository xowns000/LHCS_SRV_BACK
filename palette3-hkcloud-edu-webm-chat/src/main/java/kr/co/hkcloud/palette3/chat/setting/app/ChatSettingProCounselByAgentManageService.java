package kr.co.hkcloud.palette3.chat.setting.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface ChatSettingProCounselByAgentManageService
{
    TelewebJSON selectRtnInqryCd(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnUserInqry(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnUserAllocInqry(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnUserInqryByUser(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON updateUserSkillRank(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnUserInqryByUser(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON deleteRtnUserInqryByInqry(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON SELECT_USER_MAX_RMK(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON SELECT_TALK_USER_INQRY_LEVEL2(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON INSERT_TALK_USER_INQRY(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON DELETE_TALK_USER_INQRY(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnUserPage_new(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON STNG_CHT_USER_SKILL(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON STNG_CHT_SKILL_USER(TelewebJSON jsonParams) throws TelewebAppException;
}
