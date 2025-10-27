package kr.co.hkcloud.palette3.phone.center.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


/**
 * 스케쥴관리 서비스 인퍼페이스
 * 
 * @author dckim
 *
 */
public interface PhoneCenterScheduleManageService
{
    /**
     * 스케쥴관리 예약 콜백 현황 조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectSchedulMngList(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 스케쥴관리 예약 콜백 이력 데이터 조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectSchedulHisList(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 스케쥴관리 정보를 수정한다.
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON updateSchedulMng(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 스케쥴관리 처리상태 변경
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON updateSchedulAlloc(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 스케쥴관리 상담원재할당
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON updateSchedulCsltId(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 스케쥴관리 부서 검색 한다.
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectUserDeptSrch(TelewebJSON mjsonParams) throws TelewebAppException;

    TelewebJSON selectRtnUserInfoPop(TelewebJSON jsonParams) throws TelewebAppException;

}
