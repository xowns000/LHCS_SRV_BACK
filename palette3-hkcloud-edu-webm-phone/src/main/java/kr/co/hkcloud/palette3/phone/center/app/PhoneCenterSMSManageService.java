package kr.co.hkcloud.palette3.phone.center.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * <pre>
 * 시스템명 : 신협 공제 고객센터 시스템(S/W)
 * 업무구분 : SMS관리
 * 파 일 명 : PhoneCenterSMSManageService.java
 * 작 성 자 : 김대찬
 * 작 성 일 : 2020.01.06
 * 설    명 : SMS관리 서비스 인퍼페이스
 * --------------------------------------------------------------------------------
 * 변경일            변경자  변경내역
 * --------------------------------------------------------------------------------
 * </pre>
 */
public interface PhoneCenterSMSManageService
{
    /**
     * SMS관리 목록을 조회한다.
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectSmsMngList(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * SMS관리 정보를 등록한다.
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON insertSmsMng(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * SMS관리 정보를 수정한다.
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON updateSmsMng(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * SMS관리 정보를 삭제한다.
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON deleteSmsMng(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * SMS전송 코드 목록을 조회한다.
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectSmsSndCdList(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * SMS관리 건수를 조회한다.
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectSmsMngCnt(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * SMS발송이력을 등록한다.
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON insertSmsSendHist(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * SMS 즐겨찾기 데이터를 조회한다.
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectScrDispList(TelewebJSON mjsonParams) throws TelewebAppException;
}
