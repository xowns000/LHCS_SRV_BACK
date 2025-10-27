package kr.co.hkcloud.palette3.common.voc.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * Description : VOC Service
 * package  : kr.co.hkcloud.palette3.setting.voc.app
 * filename : VocService.java
 * Date : 2023. 6. 9.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 6. 9., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */
public interface VocService
{
	/**
	 * 
	 * VOC-목록
	 * @Method Name  	: vocList
	 * @date   			: 2023. 5. 17.
	 * @author   		: ryucease
	 * @version     	: 1.0
	 * ----------------------------------------
	 * @param mjsonParams
	 * @return
	 * @throws TelewebAppException
	 */
    TelewebJSON vocList(TelewebJSON mjsonParams) throws TelewebAppException;
    
    /**
     * 
     * VOC-등록, 수정
     * @Method Name  	: vocProc
     * @date   			: 2023. 5. 17.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON vocProc(TelewebJSON mjsonParams) throws TelewebAppException;

}
