package kr.co.hkcloud.palette3.setting.ipExt.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * Description : IP 내선번호 설정 Service
 * package  : kr.co.hkcloud.palette3.setting.ipExt.app
 * filename : IpExtService.java
 * Date : 2023. 6. 9.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 6. 9., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */
public interface IpExtService
{
	/**
	 * 
	 * IP 내선번호 관리-목록
	 * @Method Name  	: ipExtList
	 * @date   			: 2023. 5. 17.
	 * @author   		: ryucease
	 * @version     	: 1.0
	 * ----------------------------------------
	 * @param mjsonParams
	 * @return
	 * @throws TelewebAppException
	 */
    TelewebJSON ipExtList(TelewebJSON mjsonParams) throws TelewebAppException;
    
    /**
     * 
     * IP 내선번호 관리-등록, 수정
     * @Method Name  	: ipExtProc
     * @date   			: 2023. 5. 17.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON ipExtProc(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * 내선번호 중복 체크
     * @Method Name  	: extNoDuplCheck
     * @date   			: 2023. 6. 12.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON extNoDuplCheck(TelewebJSON mjsonParams) throws TelewebAppException;
    
    /**
     * 
     * IP 내선번호 관리-삭제
     * @Method Name  	: ipExtDel
     * @date   			: 2023. 5. 17.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON ipExtDel(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * 내선번호가 있는 사용자 목록
     * @Method Name  	: extNotEmptyCuslList
     * @date   			: 2023. 7. 27.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON extNotEmptyCuslList(TelewebJSON mjsonParams) throws TelewebAppException;

}
