package kr.co.hkcloud.palette3.phone.sms.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneSmsMessageManageService
{
	/**
	 * SMS 템플릿 리스트 조회
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON selectSmsTmplClsfList(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
	 * SMS 템플릿 리스트 조회
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON selectSmsTmplList(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
	 * SMS 발송 이력 리스트 조회
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON selectSmsSendHistory(TelewebJSON jsonParams) throws TelewebAppException;
}
