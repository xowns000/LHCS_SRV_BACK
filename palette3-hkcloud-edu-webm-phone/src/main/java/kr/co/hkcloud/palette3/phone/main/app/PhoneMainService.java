package kr.co.hkcloud.palette3.phone.main.app;


import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebAppException;


public interface PhoneMainService
{

    /**
     * 상담이력 등록
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cnsltHistRegist(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 호전환 팝업 그룹조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cnvrsList(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 호전환 팝업 상담원 조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cnvrsDetailList(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectObndMainList(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 캠페인조회 Arthur.Kim 2021.10.12
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectCamMainList(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 아웃바운드 통화시도 횟수 증가
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON cntobndcallModify(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 아웃바운드통화결과저장
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON obndcallModify(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 설문지 조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON srvyProcList(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 설문지문항 조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON srvyProcQList(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 설문지문항 등록 및 수정
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON mergeSrvyProc(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 콜백등록
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON callbackRegistProcess(TelewebJSON mjsonParams) throws TelewebAppException;

    /**
     * 콜백 목록을 조회한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectClbkInqList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 콜백 목록을 조회한다. Arthur.Kim 2021.10.12
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON selectClbkMngInqire(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 콜백 완료처리 한다.
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON updateClbkInqDiv(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 콜백 처리시도 횟수 증가
     * 
     * @param  jsonParams
     * @return
     * @throws TelewebAppException
     */
    TelewebJSON updateClbkTryCnt(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 콜백,아웃바운드 알람 처리
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    TelewebJSON callbackObundList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 예약콜 등록
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    TelewebJSON resveCallRegistProcess(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 예약콜 조회
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    TelewebJSON resveCallList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 예약콜 시도횟수증가
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    TelewebJSON updateResvTryCnt(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 예약 조회 Arthur.Kim 2021.10.13
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    TelewebJSON selectSchdulList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 예약콜 상세조회
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    TelewebJSON selectScheduleManageList(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 고객정보/메모및[B/L]저장 2021.10.16
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    TelewebJSON mergeCstmrinfo(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 고객메모 및 BL여부 업데이트
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    TelewebJSON updateCstmrinfo(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * IP내선번호 조회
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    TelewebJSON selectIplxtnnoInqire(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 고객등급 조회
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    TelewebJSON getBLVIP(TelewebJSON jsonParams) throws TelewebAppException;

    /**
     * 고객등급 저장
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    TelewebJSON registBLVIP(TelewebJSON jsonParams) throws TelewebAppException;
    
    /**
     * 전화받기
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    TelewebJSON callReceive(TelewebJSON jsonParams) throws TelewebAppException;    
    
    
    /**
     * 상담이력 강제저장
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    TelewebJSON cnslForceRegist(TelewebJSON jsonParams) throws TelewebAppException;   
    
    
    /**
     * 신규고객팝업오류 처리
     * 221201
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    TelewebJSON searchCustId(TelewebJSON jsonParams) throws TelewebAppException;   
    

    /**
     * 만트럭 차량정보 조회
     * 221208
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    TelewebJSON getManCarInfo(TelewebJSON jsonParams) throws TelewebAppException;   
    

    /**
     * 만트럭 차량정보 저장
     * 221209
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    TelewebJSON insertManCarInfo(TelewebJSON jsonParams) throws TelewebAppException;   
    
    
    /**
     * 만트럭 지역별 서비스센터조회
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    TelewebJSON getManCenter(TelewebJSON jsonParams) throws TelewebAppException;   
    

    /**
     * PDS조회
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    TelewebJSON getPds(TelewebJSON jsonParams) throws TelewebAppException;  
    

    /**
     * 퇴직연금 아웃바운드 정보 조회
     * 230221
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    TelewebJSON getPensionOutInfo(TelewebJSON jsonParams) throws TelewebAppException;   
    

    /**
     * 퇴직연금 아웃바운드 정보 저장
     * 230221
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    TelewebJSON insertPensionOutInfo(TelewebJSON jsonParams) throws TelewebAppException;   
    

    /**
     * 퇴직연금 아웃바운드 정보 수정
     * 230221
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    TelewebJSON updatePensionOutInfo(TelewebJSON jsonParams) throws TelewebAppException;   
}
