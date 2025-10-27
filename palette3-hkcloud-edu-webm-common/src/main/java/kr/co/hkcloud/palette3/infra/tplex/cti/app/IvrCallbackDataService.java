package kr.co.hkcloud.palette3.infra.tplex.cti.app;


import javax.servlet.http.HttpServletRequest;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface IvrCallbackDataService
{

    /**
   	 * 
   	 * 콜백 등록
   	 * @Method Name  	: callbackRegistProcess
   	 * @date   			: 2023. 7. 17.
   	 * @author   		: NJY
   	 * @version     	: 1.0
   	 * ----------------------------------------
   	 * @param  request
   	 * @return 
   	 * @throws TelewebAppException
   	 */
//    TelewebJSON callbackRegistProcess(TelewebJSON mjsonParams) throws TelewebAppException;
    String callbackRegistProcess(HttpServletRequest request) throws TelewebAppException;
    /**
   	 * 
   	 * 콜백등록 - 고객사 ID 조회
   	 * @Method Name  	: getCustcoId
   	 * @date   			: 2023. 7. 20.
   	 * @author   		: NJY
   	 * @version     	: 1.0
   	 * ----------------------------------------
   	 * @param  mjsonParams
   	 * @return 
   	 * @throws TelewebAppException
   	 */
    TelewebJSON getCustcoId(TelewebJSON mjsonParams) throws TelewebAppException;
    /**
   	 *
   	 * 콜백등록 - 고객사 schemaId 조회
   	 * @Method Name  	: getSchemaId
   	 * @date   			: 2023. 10. 6.
   	 * @author   		: NJY
   	 * @version     	: 1.0
   	 * ----------------------------------------
   	 * @param  mjsonParams
   	 * @return
   	 * @throws TelewebAppException
   	 */
    TelewebJSON getSchemaId(TelewebJSON mjsonParams) throws TelewebAppException;
    
    /**
   	 * 
   	 * 콜백등록 - 고객테이블에 존재하지 않는 고객 등록
   	 * @Method Name  	: custRegPrcs
   	 * @date   			: 2023. 7. 20.
   	 * @author   		: NJY
   	 * @version     	: 1.0
   	 * ----------------------------------------
   	 * @param  mjsonParams
   	 * @return 
   	 * @throws TelewebAppException
   	 */
    TelewebJSON custRegPrcs(TelewebJSON mjsonParams) throws TelewebAppException;

	/**
   	 *
   	 * 콜백등록 - 고객테이블에 존재하지 않는 고객, 고객확장속성에 고객상태 정상 강제 등록
   	 * @Method Name  	: custExpsnAttrReg
   	 * @date   			: 2023. 10. 18.
   	 * @author   		: NJY
   	 * @version     	: 1.0
   	 * ----------------------------------------
   	 * @param  mjsonParams
   	 * @return
   	 * @throws TelewebAppException
   	 */
    TelewebJSON clbkCustExpsnAttrReg(TelewebJSON mjsonParams) throws TelewebAppException;
    /*
     * 콜백 등록시 고객 이름 가져와서 등록
     */
    TelewebJSON custInfoProcess(TelewebJSON mjsonParams) throws TelewebAppException;

    /*
     * 큐값으로 CUSTCO_ID 가져오기
     */
    TelewebJSON callInfoProgress(TelewebJSON mjsonParams) throws TelewebAppException;

    /*
     * 회사전화번호 가져오기
     */
    TelewebJSON selectSendNum(TelewebJSON mjsonParams) throws TelewebAppException;

    /*
     * 개인 전광판 조회
     */
    TelewebJSON getMonitor(TelewebJSON mjsonParams) throws TelewebAppException;   
    
    /*
     * 콜백 자동 배분
     */
    TelewebJSON callbackAutoRegistProcess(TelewebJSON mjsonParams) throws TelewebAppException;      
 
    /*
     * 콜백 자동 배분 저장
     */
    TelewebJSON callbackAutoUpdateProcess(TelewebJSON mjsonParams) throws TelewebAppException; 
    
    /*
     * 콜백 자동 배분 YN체크
     */
    TelewebJSON callbackYNCheck(TelewebJSON mjsonParams) throws TelewebAppException;    
    
}
