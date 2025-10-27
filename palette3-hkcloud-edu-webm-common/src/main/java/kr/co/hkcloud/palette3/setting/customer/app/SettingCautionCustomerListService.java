package kr.co.hkcloud.palette3.setting.customer.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface SettingCautionCustomerListService {
    /**
     *
     * 주의 고객 목록 조회
     * @Method Name  	: selectCautionCustList
     * @date   			: 2023. 12. 26.
     * @author   		: njy
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectCautionCustList(TelewebJSON jsonParams) throws TelewebAppException;

}
