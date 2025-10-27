package kr.co.hkcloud.palette3.message.app;

import java.io.UnsupportedEncodingException;

import com.fasterxml.jackson.core.JsonProcessingException;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface MessageService {
	
	/**
	 * 
	 * 메서드 설명		: 쪽지 보내기 및 회신
     * @Method Name  	: sendMsg
     * @date   			: 2023. 6. 14
     * @author   		: 김성태
     * @version     	: 1.0
     * ----------------------------------------
	 * 
	 * @param mjsonParams
	 * @param result
	 * @return
	 * @throws TelewebApiException
	 */
	TelewebJSON sendMsg(TelewebJSON mjsonParams) throws TelewebAppException;

	/**
	 * 
     * 메서드 설명		: 받은쪽지 및 보낸쪽지 조회
     * @Method Name  	: selectMsgList
     * @date   			: 2023. 6. 14
     * @author   		: 김성태
     * @version     	: 1.0
     * ----------------------------------------
	 * @param mjsonParams
	 * @param result
	 * @return
	 * @throws TelewebApiException
	 */
	TelewebJSON selectMsgList(TelewebJSON mjsonParams) throws TelewebAppException;
	
    /**
     * 
     * 메서드 설명		: 회신쪽지 조회
     * @Method Name  	: selectMsgList2
     * @date   			: 2023. 6. 14
     * @author   		: 김성태
     * @version     	: 1.0
     * ----------------------------------------
	 * @param mjsonParams
	 * @param result
	 * @return
	 * @throws TelewebApiException
	 */
	TelewebJSON selectMsgList2(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * 메서드 설명		: 사용자 조회
     * @Method Name  	: selectUser
     * @date   			: 2023. 7. 10
     * @author   		: 김성태
     * @version     	: 1.0
     * ----------------------------------------
	 * @param mjsonParams
	 * @param result
	 * @return
	 * @throws TelewebApiException
	 */
	TelewebJSON selectUser(TelewebJSON mjsonParams) throws TelewebAppException;

	/**
	 * 
     * 메서드 설명		: 최신 받은 쪽지 조회
     * @Method Name  	: selectMsgList
     * @date   			: 2023. 7. 19
     * @author   		: 김성태
     * @version     	: 1.0
     * ----------------------------------------
	 * @param mjsonParams
	 * @param result
	 * @return
	 * @throws TelewebApiException
	 */
	TelewebJSON selectNewMsg(TelewebJSON mjsonParams) throws TelewebAppException;

	
	/**
	 * 
     * 메서드 설명		: 쪽지 개수 조회
     * @Method Name  	: selectNewMsg
     * @date   			: 2023. 7. 19
     * @author   		: 김성태
     * @version     	: 1.0
     * ----------------------------------------
	 * @param mjsonParams
	 * @param result
	 * @return
	 * @throws TelewebApiException
	 */
	TelewebJSON selectMsgCnt(TelewebJSON mjsonParams) throws TelewebAppException;
	/**
	 *
     * 메서드 설명		: 쪽지 조회 여부 업데이트
     * @Method Name  	: updateInqMsg
     * @date   			: 2023. 8. 30
     * @author   		: 나준영
     * @version     	: 1.0
     * ----------------------------------------
	 * @param mjsonParams
	 * @param result
	 * @return
	 * @throws TelewebApiException
	 */
	TelewebJSON updateInqMsg(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
 	 * 메서드 설명		: 문자,알림톡 단건 전송
 	 * 					기존 phone-api에 있던 api를 common으로 변경
 	 * 					chat에서도 해당 api를 사용하기 때문
     * @Method Name  	: sendInfo
     * @date   			: 2023. 12. 15
     * @author   		: ktj
     * @version     	: 1.0
     * ---------------------------------------- 
	 * @param jsonParams
	 * @return
     * @throws UnsupportedEncodingException 
	 * @throws TelewebApiException
	 */
	TelewebJSON sendInfo(TelewebJSON jsonParams) throws TelewebAppException, UnsupportedEncodingException, JsonProcessingException;

}
