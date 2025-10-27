package kr.co.hkcloud.palette3.phone.sms.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface PhoneAtalkTmplManageService
{
	/**
	 * 발송프로필키 목록 조회
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON selectSendProfileKeys(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
	 * 알림톡 템플릿 저장
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON checktmplCd(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
	 * 알림톡 템플릿 저장
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON saveAtalkTmpl(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
	 * 알림톡 템플릿 저장
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON modifyAtalkTmpl(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
	 * 알림톡 템플릿 저장
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON deleteAtalkTmpl(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
	 * 발송프로필키 별 알림톡 템플릿 목록 조회
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON selectAtalkTmplList(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
	 * 알림톡 템플릿 상태 업데이트 후 상태 리스트 가져오기
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON atalkTmplSttsUpdateAndSelect(TelewebJSON jsonParams) throws TelewebAppException;
	
	
	/**
	 * 발송프로필키 별 알림톡 템플릿 목록 조회
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON selectImageList(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
	 * 알림톡 템플릿 MTS 등록
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON mtsRegister(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
	 * 알림톡 템플릿 MTS 검수요청
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON mtsRequest(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
	 * 알림톡 템플릿 MTS 검수요청 취소
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON mtsCancelRequest(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
	 * 알림톡 템플릿 MTS 승인 취소
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON mtsCancelApproval(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
	 * 알림톡 템플릿 MTS 승인 취소
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON mtsStop(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
	 * 알림톡 템플릿 MTS 승인 취소
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON mtsReuse(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
	 * 알림톡 템플릿 MTS 승인 취소
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON mtsRelease(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
	 * 알림톡 카테고리 리스트 가져오기
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON getAtalkCat(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
	 * 알림톡 채널 관리자 인증번호 발급
	 *
	 * @param  jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON getToken(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
	 * 알림톡 채널 발급
	 *
	 * @param  jsonParams 
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON getAtalkChn(TelewebJSON jsonParams) throws TelewebAppException;
	
}
