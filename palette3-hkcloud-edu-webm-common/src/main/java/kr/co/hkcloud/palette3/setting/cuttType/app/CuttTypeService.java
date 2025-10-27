package kr.co.hkcloud.palette3.setting.cuttType.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 
 * Description : 상담유형 설정 관리
 * package  : kr.co.hkcloud.palette3.setting.cuttType.app
 * filename : CuttTypeService.java
 * Date : 2023. 5. 22.
 * History :
 * - 작성자 : ryucease, 날짜 : 2023. 5. 22., 설명 : 최초작성<br>
 *
 * @author ryucease
 * @version 1.0
 */
public interface CuttTypeService
{
	/**
	 * 
	 * 상담유형 설정 Tree
	 * @Method Name  	: cuttTypeTreeList
	 * @date   			: 2023. 5. 22.
	 * @author   		: ryucease
	 * @version     	: 1.0
	 * ----------------------------------------
	 * @param mjsonParams
	 * @return
	 * @throws TelewebAppException
	 */
    TelewebJSON cuttTypeTreeList(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * 상담유형 셋팅 정보
     * @Method Name  	: cuttTypeSettingInfo
     * @date   			: 2023. 5. 22.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cuttTypeSettingInfo(TelewebJSON mjsonParams) throws TelewebAppException;
    
    /**
     * 
     * 상담유형 등록 수정
     * @Method Name  	: cuttTypeProc
     * @date   			: 2023. 5. 25.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cuttTypeProc(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * 상담유형 Tree 순서 변경
     * @Method Name  	: cuttTypeOrderUpdate
     * @date   			: 2023. 5. 25.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cuttTypeOrderUpdate(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * 상담유형 삭제
     * @Method Name  	: cuttTypeDel
     * @date   			: 2023. 5. 25.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cuttTypeDel(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * 상담유형 설정 저장
     * @Method Name  	: cuttTypeSettingSave
     * @date   			: 2023. 5. 25.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cuttTypeSettingSave(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * 고객사 대표번호 목록
     * @Method Name  	: dsptchNoList
     * @date   			: 2023. 9. 1.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON dsptchNoList(TelewebJSON mjsonParams) throws TelewebAppException;
    
    /**
     * 
     * 상담사 상담유형 북마크(즐겨찾기) 목록
     * @Method Name  	: cuslCuttTypeBmkList
     * @date   			: 2023. 11. 6.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cuslCuttTypeBmkList(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * 상담사 상담유형 북마크(즐겨찾기) 등록, 삭제
     * @Method Name  	: cuslCuttTypeBmkProc
     * @date   			: 2023. 11. 7.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cuslCuttTypeBmkProc(TelewebJSON mjsonParams) throws TelewebAppException;
}
