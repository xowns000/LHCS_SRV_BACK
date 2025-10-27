package kr.co.hkcloud.palette3.phone.history.app;

import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;

public interface PhoneHistoryInqireService {

    /**
     * 상담원그룹정보 조회
     * 
     * @param mjsonParams
     * @return
     */
    TelewebJSON selectRtnTmKind(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 상담이력관리 목록 조회(공제)
     * 
     * @param mjsonParams
     * @return
     */
    TelewebJSON selectRtnCnslHistMngDe(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 상담이력관리 수정(공제)
     * 
     * @param mjsonParams
     * @return
     */
    TelewebJSON updateRtnCnslHistMngDe(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON updateRtnCnslHistMngDe2(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 상담이력변경관리 등록
     * 
     * @param mjsonParams
     * @return
     */
    TelewebJSON insertCnslChngHistDe(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 상담이력조회 칭찬콜등록
     * 
     * @param mjsonParams
     * @return
     */
    TelewebJSON insertCnslCmptCall(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 녹취ID 기준의 조회고객별 상담저장여부 조회
     * 
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectRtnCnslHistSaveYnDe(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 조합검색 팝업 조회 (기존 기간계) > 하드코딩
     * 
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON SELMCZ10106(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON selectRtnUserInfoPop(TelewebJSON jsonParams) throws TelewebAppException;

    TelewebJSON insertCnslHistOutputMng(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * 전화상담 이력 목록
     * 
     * @Method Name : cuttHistList
     * @date : 2023. 6. 27.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cuttHistList(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * 전화상담 이력 관리 목록
     * 
     * @Method Name : cuttHistMngList
     * @date : 2023. 6. 27.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cuttHistMngList(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * 전화상담 내역 변경 정보 조회
     * 
     * @Method Name : cuttChgInfo
     * @date : 2023. 6. 29.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cuttChgInfo(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * 전화상담 내역 변경 요청 승인
     * 
     * @Method Name : cuttChgAprvProc
     * @date : 2023. 6. 29.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cuttChgAprvProc(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 
     * 전화상담 내역 일괄 변경 요청 승인
     * 
     * @Method Name : cuttChgAprvProc
     * @date : 2023. 6. 29.
     * @author : ryucease
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON batchCuttChgAprvProc(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     *
     * 전화상담 이력 엑셀 다운로드
     * 
     * @Method Name : cuttHistExcelDwnld
     * @date : 2023. 11. 08.
     * @author : njy
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     *
     * @throws TelewebApiException
     */
    TelewebJSON cuttHistExcelDwnld(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     *
     * 전화상담 이력 상담, 고객 확장속성 정보 조회
     * 
     * @Method Name : cuttHistGetExpsnAttr
     * @date : 2023. 11. 13.
     * @author : njy
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     *
     * @throws TelewebApiException
     */
    TelewebJSON cuttHistGetExpsnAttr(TelewebJSON mjsonParams) throws TelewebAppException;
    /**
     *
     * 나의 상담 이력 관리 차트 정보
     *
     * @Method Name : getMyCuttHistCnt
     * @date : 2024. 01. 05.
     * @author : njy
     * @version : 1.0
     * ----------------------------------------
     * @param mjsonParams
     * @return
     *
     * @throws TelewebApiException
     */
    TelewebJSON getMyCuttHistCnt(TelewebJSON mjsonParams) throws TelewebAppException;

}
