package kr.co.hkcloud.palette3.cron.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface CronService {
	public void executeItgrtStatisticsRowData();
	public void executeItgrtChatStatisticsRowData();
	
	/**
	 * 
	 * 설문, QA, 캠페인 상태 변경
	 * @Method Name  	: executePlanStatChange
	 * @date   			: 2023. 9. 18.
	 * @author   		: ryucease
	 * @version     	: 1.0
	 * ----------------------------------------
	 * @param mjsonParams
	 */
	public void executePlanStatChange();
	
	TelewebJSON selectBatchTenantList() throws TelewebAppException;
}
