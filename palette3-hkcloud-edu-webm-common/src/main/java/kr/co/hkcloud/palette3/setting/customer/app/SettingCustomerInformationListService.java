package kr.co.hkcloud.palette3.setting.customer.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;
import kr.co.hkcloud.palette3.setting.customer.dto.CustomerVO;


public interface SettingCustomerInformationListService
{

    TelewebJSON selectRtnPageCustInfo(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnCustInfo(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON insertUserInfo(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON insertCustInfo(TelewebJSON jsonParams) throws TelewebAppException;

    void mergeCustomerBaseInfo(CustomerVO customerVO) throws TelewebAppException;

    String decryptCustBaseInfo(String encryptString) throws TelewebAppException;

    void mergeCustomerBaseInfoBySony(CustomerVO customerVOBySony) throws TelewebAppException;

    void mergeCustomerBaseInfoBySsg(CustomerVO customerVOBySsg) throws TelewebAppException;

    void mergeCustomerBaseInfoByKaom(CustomerVO customerVoByKaom) throws TelewebAppException;

    TelewebJSON deleteRtnCustInfo(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnCustInfoPop(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnComCustInqire(TelewebJSON jsonParams) throws TelewebAppException;                //고객검색 조회 Artrhur.Kim 2021.10.14

    TelewebJSON selectRtnComObdCustInqire(TelewebJSON jsonParams) throws TelewebAppException;             //캠페인 고객검색 조회 Artrhur.Kim 2021.10.15

    TelewebJSON selectRtnCust(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectQUEUEInqire(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * 고객 전화번호 체크
     * @Method Name  	: custTelNoCheck
     * @date   			: 2023. 9. 12.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON custTelNoCheck(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * 동일 전화번호 존재 시 채팅 키를 통해 통합 가능 고객 판단
     * @Method Name  	: chtCustDuplChk
     * @date   			: 2024. 3. 6.
     * @author   		: ktj
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON chtCustDuplChk(TelewebJSON jsonParams) throws TelewebAppException;
    
    /**
     * 
     * 고객정보 저장, 수정
     * @Method Name  	: custProc
     * @date   			: 2023. 6. 30.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON custProc(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * 고객 기본 정보 수정 및 주의 고객 정보 저장
     * @Method Name  	: custMod
     * @date   			: 2023. 6. 30.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON custMod(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * 고객 확장 컬럼 정보 조회
     * @Method Name  	: custcoExpsnInfo
     * @date   			: 2023. 7. 4.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON custcoExpsnInfo(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * 고객 동의 이력 저장, 수정
     * @Method Name  	: custAgreHstryProc
     * @date   			: 2023. 6. 30.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON custAgreHstryProc(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * 고객 기본 정보 조회
     * @Method Name  	: custDefaultInoSelect
     * @date   			: 2023. 6. 21.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON custDefaultInoSelect(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * 고객 기본 정보 조회
     * @Method Name  	: custSelect
     * @date   			: 2023. 6. 21.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON custSelect(TelewebJSON jsonParams) throws TelewebAppException;
    
    /**
     * 
     * 고객 확장 정보 조회
     * @Method Name  	: custExpsnInfoSelect
     * @date   			: 2023. 6. 21.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON custExpsnInfoSelect(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * 고객 동의 정보 조회
     * @Method Name  	: custAgreeInfoSelect
     * @date   			: 2023. 6. 21.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON custAgreeInfoSelect(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * PALETTE3 상담 이력 목록
     * @Method Name  	: integCuttHistList
     * @date   			: 2023. 6. 22.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON integCuttHistList(TelewebJSON jsonParams) throws TelewebAppException;
    
    /**
     * 
     * PALETTE3 VOC 목록
     * @Method Name  	: vocList
     * @date   			: 2023. 6. 22.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON vocList(TelewebJSON jsonParams) throws TelewebAppException;
    
    /**
     * 
     * PALETTE3 예약 콜 목록
     * @Method Name  	: rsvtCallList
     * @date   			: 2023. 6. 22.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON rsvtCallList(TelewebJSON jsonParams) throws TelewebAppException;
    
    /**
     * 
     * PALETTE3 콜백 목록
     * @Method Name  	: callBackList
     * @date   			: 2023. 6. 22.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON callBackList(TelewebJSON jsonParams) throws TelewebAppException;
    
    /**
     * 
     * PALETTE3 캠페인 목록
     * @Method Name  	: cpiStatHistList
     * @date   			: 2023. 6. 22.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cpiStatHistList(TelewebJSON jsonParams) throws TelewebAppException;
    
    /**
     * 
     * 고객 통합
     * @Method Name  	: mergeCust
     * @date   			: 2023. 11. 29.
     * @author   		: kimtaejun
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON mergeCust(TelewebJSON jsonParams) throws TelewebAppException;
    
}
