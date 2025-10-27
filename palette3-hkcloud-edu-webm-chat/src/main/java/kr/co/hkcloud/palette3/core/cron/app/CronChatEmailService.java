package kr.co.hkcloud.palette3.core.cron.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface CronChatEmailService {
    /**
     * 채팅_상담_이메일 수집
     */
    public TelewebJSON collectChatCuttEmail(TelewebJSON jobMngObj) throws TelewebAppException;
    
    /**
     * 고객사_채널_이메일_설정 목록 조회
     * @return
     */
    public TelewebJSON selectCustcoChannelEmailSettingList(TelewebJSON jsonParams) throws TelewebAppException;
    
    /**
     * 채팅_상담_ID 업데이트
     * @param params
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON updateChtCuttId(TelewebJSON jsonParmas) throws TelewebAppException;
}
