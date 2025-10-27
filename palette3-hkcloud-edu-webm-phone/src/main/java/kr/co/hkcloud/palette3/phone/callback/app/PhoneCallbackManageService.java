package kr.co.hkcloud.palette3.phone.callback.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneCallbackManageService
{

    /**
     * 콜백 목록을 조회한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectClbkMngList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 콜백 완료처리 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON updateClbkMng(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 콜백 배분 처리 등록한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON insertClbkMngDiv(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 콜백 배분 처리 수정한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON updateClbkMngDiv(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 콜백배분정보을 조회한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectClbkDivList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 콜백상담원배분정보를 조회한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectClbkDstrDivList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 콜백고객정보를 조회한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectClbkCustList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 콜백배분 등록한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON insertClbkDiv(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 콜백미시도를 조회한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectClbkNotEndList(TelewebJSON jsonParams) throws TelewebAppException;

	/**
	 * 
	 * 콜백 배분 회수
	 * 
	 * @Method Name : deleteClbkDiv
	 * @date : 2023. 7. 19.
	 * @author : NJY
	 * @version : 1.0 ----------------------------------------
	 * @param jsonParams
	 * @return TelewebJSON 형식의 삭제 결과 데이터
	 * @throws TelewebAppException
	 */
    TelewebJSON deleteClbkDiv(TelewebJSON jsonParams) throws TelewebAppException;
    
 
    
}
