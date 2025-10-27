package kr.co.hkcloud.palette3.phone.sendInfo.app;

import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.core.JsonProcessingException;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface PhoneSendPlaceContactService {

	/**
 	 * 메서드 설명		: 위치정보, 연락처정보 조회
     * @Method Name  	: selectPlace
     * @date   			: 2023. 6. 22
     * @author   		: 김성태
     * @version     	: 1.0
     * ---------------------------------------- 
	 * @param jsonParams
	 * @return
	 * @throws TelewebApiException
	 */
	TelewebJSON selectPlace(TelewebJSON jsonParams) throws TelewebAppException;

    /**
 	 * 메서드 설명		: 문자,알림톡 단건 전송
     * @Method Name  	: sendInfo
     * @date   			: 2023. 6. 27
     * @author   		: 김성태
     * @version     	: 1.0
     * ---------------------------------------- 
	 * @param jsonParams
	 * @return
     * @throws UnsupportedEncodingException 
	 * @throws TelewebApiException
	 */
	TelewebJSON sendInfo(TelewebJSON jsonParams) throws TelewebAppException, UnsupportedEncodingException, JsonProcessingException;

	/**
 	 * 메서드 설명		: 위치정보,연락처정보 전용 템플릿 조회 
     * @Method Name  	: selectTempleteInfo
     * @date   			: 2023. 7. 24
     * @author   		: 김성태
     * @version     	: 1.0
     * ---------------------------------------- 
	 * @param jsonParams
	 * @return
	 * @throws TelewebApiException
	 */
	TelewebJSON selectTempleteInfo(TelewebJSON jsonParams) throws TelewebAppException;

    /**
 	 * 메서드 설명		: 알림톡 단건 전송 템플릿 조회
     * @Method Name  	: selectAtalkTemplete
     * @date   			: 2023. 7. 25
     * @author   		: 김성태
     * @version     	: 1.0
     * ---------------------------------------- 
	 * @param jsonParams
	 * @return
	 * @throws TelewebApiException
	 */
	TelewebJSON selectAtalkTemplete(TelewebJSON jsonParams) throws TelewebAppException;

  
}
