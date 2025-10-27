package kr.co.hkcloud.palette3.setting.system.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface SettingSystemPstnCtcManageService {

	/**
	 * 
	 * 메서드 설명		: 위치정보 리스트 조회
     * @Method Name  	: selectPstn
     * @date   			: 2023. 6. 19
     * @author   		: 김성태
     * @version     	: 1.0
     * ----------------------------------------
	 * @param mjsonParams
	 * @return
	 * @throws TelewebApiException
	 */
	TelewebJSON selectPstn(TelewebJSON mjsonParams) throws TelewebAppException;
	
	/**
     * 메서드 설명		: 위치정보 등록 및 수정
     * @Method Name  	: increaseCnt
     * @date   			: 2023. 6. 19
     * @author   		: 김성태
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
	TelewebJSON upsertPstn(TelewebJSON mjsonParams) throws TelewebAppException;

	/**
     * 
     * 메서드 설명		: 위치정보 삭제 
     * @Method Name  	: increaseCnt
     * @date   			: 2023. 6. 19
     * @author   		: 김성태
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
	TelewebJSON deletePstn(TelewebJSON mjsonParams) throws TelewebAppException;

	
	/**
	 * 
	 * 메서드 설명		: 위치정보 리스트 조회
     * @Method Name  	: selectCtc
     * @date   			: 2023. 6. 19
     * @author   		: 김성태
     * @version     	: 1.0
     * ----------------------------------------
	 * @param mjsonParams
	 * @return
	 * @throws TelewebApiException
	 */
	TelewebJSON selectCtc(TelewebJSON mjsonParams) throws TelewebAppException;
	
	/**
     * 메서드 설명		: 연락처정보 등록 및 수정
     * @Method Name  	: upsertCtc
     * @date   			: 2023. 6. 19
     * @author   		: 김성태
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
	TelewebJSON upsertCtc(TelewebJSON mjsonParams) throws TelewebAppException;
	
    /**
     * 
     * 메서드 설명		: 위치정보 삭제 
     * @Method Name  	: deleteCtc
     * @date   			: 2023. 6. 19
     * @author   		: 김성태
     * @version     	: 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebApiException
     */
	TelewebJSON deleteCtc(TelewebJSON mjsonParams) throws TelewebAppException;
}
