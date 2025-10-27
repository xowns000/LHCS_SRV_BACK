package kr.co.hkcloud.palette3.phone.sms.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface PhoneAtalkMessageManageService
{
	/**
	 * 알림톡 템플릿 목록 조회
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON atalkTmpls(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
	 * 알림톡 템플릿 목록 조회
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON atalkSendHistory(TelewebJSON jsonParams) throws TelewebAppException;
		
}
