package kr.co.hkcloud.palette3.common.prvc.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * Description : 개인정보 조회 이력 Service
 * package  : kr.co.hkcloud.palette3.common.prvc.app
 * filename : PrvcService.java
 * Date : 2023. 9. 7.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 9. 7., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */
public interface PrvcService
{
	
	/**
	 * 
	 * 개인정보 조회 이력 -목록
	 * @Method Name  	: prvcInqHistList
	 * @date   			: 2023. 9. 7.
	 * @author   		: ryucease
	 * @version     	: 1.0
	 * ----------------------------------------
	 * @param mjsonParams
	 * @return
	 * @throws TelewebAppException
	 */
    TelewebJSON prvcInqHistList(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * 개인정보 조회 이력 저장 처리
     * @Method Name  	: insertPrvcInqLog
     * @date   			: 2023. 9. 8.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
	TelewebJSON insertPrvcInqLog(TelewebJSON jsonParams) throws TelewebAppException;

}
