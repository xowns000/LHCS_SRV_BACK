package kr.co.hkcloud.palette3.setting.ognz.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * Description : 조직 관리
 * package  : kr.co.hkcloud.palette3.setting.ognz.app
 * filename : OgnzService.java
 * Date : 2023. 6. 7.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 6. 7., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */
public interface OgnzService
{
	
	/**
	 * 
	 * 조직 Tree
	 * @Method Name  	: ognzTreeList
	 * @date   			: 2023. 6. 7.
	 * @author   		: ryucease
	 * @version     	: 1.0
	 * ----------------------------------------
	 * @param mjsonParams
	 * @return
	 * @throws TelewebAppException
	 */
	TelewebJSON ognzTreeList(TelewebJSON mjsonParams) throws TelewebAppException;
	
    /**
     * 
     * 조직 관리-등록, 수정
     * @Method Name  	: ognzProc
     * @date   			: 2023. 6. 7.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON ognzProc(TelewebJSON mjsonParams) throws TelewebAppException;
    
    /**
     * 
     * 조직 관리-삭제
     * @Method Name  	: ognzDel
     * @date   			: 2023. 6. 7.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON ognzDel(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * 조직 순서 재정의
     * @Method Name  	: ognzReOrder
     * @date   			: 2023. 6. 7.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON ognzOrderUpdate(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * 지역 목록
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
	TelewebJSON rgnList(TelewebJSON mjsonParams) throws TelewebAppException;
}
