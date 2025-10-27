package kr.co.hkcloud.palette3.phone.callback.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneCallbackListService
{

	 /**
   	 * 
   	 * 콜백정보 조회
   	 * @Method Name  	: selectClbkInqList
   	 * @date   			: 2023. 7. 17.
   	 * @author   		: NJY
   	 * @version     	: 1.0
   	 * ----------------------------------------
   	 * @param  mjsonParams	
   	 * @return TelewebJSON 형식의 조회결과 데이터
   	 * @throws TelewebApiException
   	 */
    TelewebJSON selectClbkInqList(TelewebJSON jsonParams) throws TelewebAppException;
    /**
     * 콜백 처리결과를 등록한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON updateClbkInq(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * 콜백 이력 목록
     * @Method Name  	: clbkStatHistList
     * @date   			: 2023. 7. 18.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON clbkStatHistList(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 
     * 상담원별 콜백 상태
     * @Method Name  	: cuslClbkMonitor
     * @date   			: 2023. 7. 18.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cuslClbkMonitor(TelewebJSON jsonParam) throws TelewebAppException;

    /**
     * 
     * 상담원별 처리결과별 상태
     * @Method Name  	: cuslClbkDtlMonitor
     * @date   			: 2023. 7. 18.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParam
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cuslClbkDtlMonitor(TelewebJSON jsonParam) throws TelewebAppException;
}
