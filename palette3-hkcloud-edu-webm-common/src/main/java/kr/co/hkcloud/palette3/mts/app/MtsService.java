package kr.co.hkcloud.palette3.mts.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface MtsService {
	
	/**
	 * MTS 발송 결과 업데이트 스케줄러 
	 * @return
	 * @throws TelewebAppException
	 */
	public TelewebJSON cronMtsSendingResult() throws TelewebAppException;
	
	/**
	 * MTS 발송 결과 업데이트
	 * @return
	 * @throws TelewebAppException
	 */
	public TelewebJSON updateMtsSendingResult(TelewebJSON mjsonParams) throws TelewebAppException;

}
