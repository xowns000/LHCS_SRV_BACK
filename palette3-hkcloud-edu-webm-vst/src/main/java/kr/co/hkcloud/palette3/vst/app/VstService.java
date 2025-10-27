package kr.co.hkcloud.palette3.vst.app;

import java.text.ParseException;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface VstService {

	TelewebJSON prdctTypeList(TelewebJSON jsonParams) throws TelewebAppException;
	
	TelewebJSON vstDayTimeList(TelewebJSON jsonParams) throws TelewebAppException;

	TelewebJSON vstDayTimeVstrList(TelewebJSON jsonParams) throws TelewebAppException;

	TelewebJSON vstVstrRdyStatChk(TelewebJSON jsonParams) throws TelewebAppException;

	TelewebJSON vstVstrRdyProc(TelewebJSON jsonParams) throws TelewebAppException;

	TelewebJSON vstRsvtList(TelewebJSON jsonParams) throws TelewebAppException;

	/**
	 * 방문 예약 저장
	 * @throws ParseException 
	 */
	TelewebJSON vstRsvtProc(TelewebJSON jsonParams) throws TelewebAppException, ParseException;

	/**
	 * 방문 예약 이력 저장
	 */
	TelewebJSON vstRsvtHstryReg(TelewebJSON jsonParams) throws TelewebAppException;

	/**
	 * 
	 * 배정 및 처리 이력 목록
	 * @param jsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON vstRsvtHistList(TelewebJSON jsonParams) throws TelewebAppException;

}
