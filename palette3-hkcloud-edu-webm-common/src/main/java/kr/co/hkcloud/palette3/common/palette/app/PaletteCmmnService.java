package kr.co.hkcloud.palette3.common.palette.app;


import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;

import org.json.simple.parser.ParseException;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 팔레트공통 서비스 인터페이스
 * 
 * @author Orange
 *
 */
public interface PaletteCmmnService
{
    TelewebJSON selectRtnAttrDiv(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCachingAspSenderKey(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCachingBizServicesCd(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON insertRtnCnslUnityHst(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCnslUnityHst(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCnslUnityList(TelewebJSON jsonParams) throws TelewebAppException;              // 나의상담이력(통합상담이력) 조회 Arthur.Kim 2021.10.13
    TelewebJSON selectRtnCnslUnityHstDtl(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCnslUnityHstPhone(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCnslUnityHstChat(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCnslUnityHstMail(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCnslUnityHstSms(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON selectRtnCnslUnityHstNtcnTalk(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON getCmmCode1(TelewebJSON jsonParams) throws TelewebAppException;
    TelewebJSON getCompanyNM(TelewebJSON jsonParams) throws TelewebAppException;
    
    /**
     * 
     * 상담 통합 이력 저장
     * @Method Name  	: cuttItgrtHistReg
     * @date   			: 2023. 6. 28.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cuttItgrtHistReg(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * 전화 콜 이력 저장 처리
     * @Method Name  	: phnCallHistReg
     * @date   			: 2023. 7. 13.
     * @author   		: ryucease
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON phnCallHistReg(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 
     * 발신 프로필 키에 따른 테넌시 검색(채팅)
     * @Method Name  	: phnCallHistReg
     * @date   			: 2023. 8. 23.
     * @author   		: ktj982028
     * @version     	: 1.0
     * ----------------------------------------
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON getCertCustcoId(TelewebJSON jsonParams) throws TelewebAppException;
	
    /**
     * 
     * 상담 이관 이력 목록
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cuttTrnsfHstryList(TelewebJSON jsonParams) throws TelewebAppException;
    
    /**
     * 
     * 상담 이관 상태 변경
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
	TelewebJSON cuttTrnsfHstryReg(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
	 * 상담 이관 저장
	 * @param jsonParams
     * @return
     * @throws TelewebAppException
	 */
	TelewebJSON cuttTrnsfReg(TelewebJSON jsonParams) throws TelewebAppException;
	
	/**
	 * 
	 * 이관 처리현황 이관 및 고객 확장속성 정보 조회
	 * @param mjsonParams
	 * @return
	 */
	TelewebJSON cuttTrnsfHistGetExpsnAttr(TelewebJSON mjsonParams);

    /**
     * 해당 고객사의 이관 담당자 옵션 코드 조회
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON getTrnsfTargetOptCd(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * (설문)부서 리스트 조회
     * @param jsonParams
     * @return
     * @throws TelewebAppException
     */
    public TelewebJSON getDeptList(TelewebJSON jsonParams) throws TelewebAppException;
    
}
