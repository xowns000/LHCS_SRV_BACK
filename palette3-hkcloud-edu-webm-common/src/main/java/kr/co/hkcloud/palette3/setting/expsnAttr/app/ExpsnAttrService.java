package kr.co.hkcloud.palette3.setting.expsnAttr.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * Description : 확장속성 관리
 * package  : kr.co.hkcloud.palette3.setting.expsnAttr.app
 * filename : ExpsnAttrService.java
 * Date : 2023. 5. 17.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 5. 17., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */
public interface ExpsnAttrService
{
	/**
	 * 
	 * 확장속성관리-목록
	 * @Method Name  	: expsnAttrList
	 * @date   			: 2023. 5. 17.
	 * @author   		: ryucease
	 * @version     	: 1.0
	 * ----------------------------------------
	 * @param mjsonParams
	 * @return
	 * @throws TelewebAppException
	 */
    TelewebJSON expsnAttrList(TelewebJSON mjsonParams) throws TelewebAppException;
    
    /**
     * 
     * 확장속성관리-등록, 수정
     * @Method Name  	: expsnAttrProc
     * @date   			: 2023. 5. 17.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON expsnAttrProc(TelewebJSON mjsonParams) throws TelewebAppException;
    
    /**
     * 
     * 확장속성관리-삭제
     * @Method Name  	: expsnAttrDel
     * @date   			: 2023. 5. 17.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON expsnAttrDel(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * 확장항목 순서 재정의
     * @Method Name  	: expsnAttrReOrder
     * @date   			: 2023. 5. 30.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON expsnAttrReOrder(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * 확장속성관리-항목ID 중복 체크
     * @Method Name  	: expsnAttrColIdDupleChk
     * @date   			: 2023. 11. 1.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    boolean expsnAttrColIdDupleChk(TelewebJSON mjsonParams) throws TelewebAppException;
}
