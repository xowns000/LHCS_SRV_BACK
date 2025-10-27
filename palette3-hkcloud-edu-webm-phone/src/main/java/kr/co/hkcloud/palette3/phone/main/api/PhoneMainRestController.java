package kr.co.hkcloud.palette3.phone.main.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.palette.app.PaletteCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.main.app.PhoneMainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneMainRestController",
     description = "공제메인 REST 컨트롤러")
public class PhoneMainRestController
{

    private final PhoneMainService   phoneMainService; // 전화상담메인 서비스 
    private final PaletteCmmnService paletteCmmnService; // 공통 서비스

//    private final HttpConnectionConfig httpConnectionConfig;

    /**
     * 1-3 아웃바운드 조회 #01
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "아웃바운드 조회",
                  notes = "아웃바운드 조회")
    @PostMapping("/phone-api/main/outbound/inqire")
    public Object PhoneMainOutboundList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성
        //아웃바운드 정보 조회
        objRetParams = phoneMainService.selectObndMainList(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 캠페인조회 Arthur.Kim 2021.10.12
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "캠페인 조회",
                  notes = "캠페인 조회")
    @PostMapping("/phone-api/main/cam/inqire")
    public Object PhoneMainCamList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성
        //아웃바운드 정보 조회
        objRetParams = phoneMainService.selectCamMainList(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 아웃바운드 통화시도 횟수증가
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 처리결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "아웃바운드통화시도횟수증가",
                  notes = "아웃바운드통화시도횟수증가")
    @PostMapping("/phone-api/main/cnt-obndcall/modify")
    public Object CntobndcallModify(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams); //반환 파라메터 생성
        //아웃바운드 정보 조회
        objRetParams = phoneMainService.cntobndcallModify(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 호전환 팝업 그룹조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "호전환 팝업 그룹조회",
                  notes = "호전환 팝업 그룹조회")
    @PostMapping("/phone-api/main/cnvrs-group/list")
    public Object cnvrsList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);  //반환 파라메터 생성
        objRetParams = phoneMainService.cnvrsList(mjsonParams);
        return objRetParams;
    }


    /**
     * 상담이력 저장
     *
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "상담이력 저장",
                  notes = "상담이력 저장")
    @PostMapping("/phone-api/main/cnslt-hist/regist")
    public Object PhoneMainConsultingRecordCallRegist(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        objRetParams = phoneMainService.cnsltHistRegist(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 설문지조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "설문지조회",
                  notes = "설문지조회")
    @PostMapping("/phone-api/main/qestnr-regist-popup-srvy-proc/list")
    public Object srvyProcList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);  //반환 파라메터 생성
        objRetParams = phoneMainService.srvyProcList(mjsonParams);
        return objRetParams;
    }


    /**
     * 설문지조문항조회
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "설문지문항조회",
                  notes = "설문지문항조회")
    @PostMapping("/phone-api/main/qestnr-regist-popup-srvy-proc-q/list")
    public Object srvyProcQList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);  //반환 파라메터 생성
        objRetParams = phoneMainService.srvyProcQList(mjsonParams);
        return objRetParams;
    }


    /**
     * 설문지조문항등록 및 수정
     * 
     * @param  mjsonParams
     * @return
     * @throws TelewebApiException
     */
    @ApiOperation(value = "설문지문항등록",
                  notes = "설문지문항등록")
    @PostMapping("/phone-api/main/qestnr-regist-popup-merge-srvy-proc/regist")
    public Object mergeSrvyProc(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);  //반환 파라메터 생성
        objRetParams = phoneMainService.mergeSrvyProc(mjsonParams);
        return objRetParams;
    }


    /**
     * 콜백접수정보를 조회한다.
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "콜백접수 조회",
                  notes = "콜백접수정보를 조회한다.")
    @PostMapping("/phone-api/main/callback-manage/list")
    public Object selectClbkMngList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DAO 검색 메소드 호출
        objRetParams = phoneMainService.selectClbkInqList(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 콜백접수정보를 조회한다. Arthur.Kim 2021.10.12
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "콜백접수 조회",
                  notes = "콜백접수정보를 조회한다.")
    @PostMapping("/phone-api/main/callback-manage/inqire")
    public Object selectClbkMngInqire(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DAO 검색 메소드 호출
        objRetParams = phoneMainService.selectClbkMngInqire(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 콜백 완료처리
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "콜백 완료처리",
                  notes = "콜백 완료처리")
    @PostMapping("/phone-api/main/callback-manage/modify")
    public Object updateClbkMng(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        objRetParams = phoneMainService.updateClbkInqDiv(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 콜백 시도횟수증가
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "콜백 시도횟수증가",
                  notes = "콜백 시도횟수증가")
    @PostMapping("/phone-api/main/callback-manage-cnt/modify")
    public Object updateClbkTryCnt(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        objRetParams = phoneMainService.updateClbkTryCnt(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 예약콜 시도횟수증가
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "예약콜 시도횟수증가",
                  notes = "예약콜 시도횟수증가")
    @PostMapping("/phone-api/main/resvcall-manage-cnt/modify")
    public Object updateResvTryCnt(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        objRetParams = phoneMainService.updateResvTryCnt(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 콜백,아웃바운드 알람 처리
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "콜백 완료처리",
                  notes = "콜백 완료처리")
    @PostMapping("/phone-api/main/callback-obund/list")
    public Object callbackObundList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        objRetParams = phoneMainService.callbackObundList(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 예약콜 조회(IF)
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "예약콜 조회",
                  notes = "예약콜 조회")
    @PostMapping("/phone-api/main/resve-call/list")
    public Object resveCallList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneMainService.resveCallList(mjsonParams);
        //최종결과값 반환
        return objRetParams;
        //최종결과값 반환
    }


    /**
     * 예약 조회 Arthur.Kim 2021.10.13
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "스케줄 조회",
                  notes = "스케줄 조회")
    @PostMapping("/phone-api/main/schdul/list")
    public Object selectSchdulList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneMainService.selectSchdulList(mjsonParams);
        //최종결과값 반환
        return objRetParams;
        //최종결과값 반환
    }


    /**
     * 스케줄 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "스케줄 조회",
                  notes = "스케줄 조회")
    @PostMapping("/phone-api/main/schedule-manage/list")
    public Object selectScheduleManageList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneMainService.selectScheduleManageList(mjsonParams);
        //최종결과값 반환
        return objRetParams;
        //최종결과값 반환
    }


    /**
     * 나의상담이력(통합상담이력) 조회 Arthur.Kim 2021.10.13
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "나의상담이력 조회",
                  notes = "나의상담이력 조회")
    @PostMapping("/phone-api/main/unity-cnsl/list")
    public Object unityCnslList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = paletteCmmnService.selectRtnCnslUnityList(mjsonParams);
        //최종결과값 반환
        return objRetParams;
        //최종결과값 반환
    }


    /**
     * 통합상담이력 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "통합상담이력 조회",
                  notes = "통합상담이력 조회")
    @PostMapping("/phone-api/main/unity-cnsl/inqire")
    public Object unityCnslInqire(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = paletteCmmnService.selectRtnCnslUnityHst(mjsonParams);
        //최종결과값 반환
        return objRetParams;
        //최종결과값 반환
    }


    /**
     * 통합상담이력 조회 (전화상담)
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "통합상담이력 전화상세조회",
                  notes = "통합상담이력 전화상세조회")
    @PostMapping("/phone-api/main/unity-cnsl-hst-phone/inqire")
    public Object selectRtnCnslUnityHstPhone(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = paletteCmmnService.selectRtnCnslUnityHstPhone(mjsonParams);
        //최종결과값 반환
        return objRetParams;
        //최종결과값 반환
    }


    /**
     * 통합상담이력 조회 (전화상담)
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "통합상담이력 채팅상세조회",
                  notes = "통합상담이력 채팅상세조회")
    @PostMapping("/phone-api/main/unity-cnsl-hst-chat/inqire")
    public Object selectRtnCnslUnityHstChat(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = paletteCmmnService.selectRtnCnslUnityHstChat(mjsonParams);
        //최종결과값 반환
        return objRetParams;
        //최종결과값 반환
    }


    /**
     * 고객정보/메모및[B/L]저장 2021.10.16
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "고객정보/메모및[B/L]저장",
                  notes = "고객메모및[B/L]저장")
    @PostMapping("/phone-api/main/cstmr-info/insert")
    public Object mergeCstmrinfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneMainService.mergeCstmrinfo(mjsonParams);
        //최종결과값 반환
        return objRetParams;
        //최종결과값 반환
    }


    /**
     * 고객정보수정
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "고객메모및[B/L]수정",
                  notes = "고객메모및[B/L]수정")
    @PostMapping("/phone-api/main/cstmr-info/modify")
    public Object updateCstmrinfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneMainService.updateCstmrinfo(mjsonParams);
        //최종결과값 반환
        return objRetParams;
        //최종결과값 반환
    }


    /**
     * IP내선번호 관리
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "고객메모및[B/L]수정",
                  notes = "고객메모및[B/L]수정")
    @PostMapping("/phone-api/main/iplxtnno/inqire")
    public Object iplxtnnoinqire(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneMainService.selectIplxtnnoInqire(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 고객등급 조회
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "고객등급 조회",
                  notes = "고객등급 조회")
    @PostMapping("/phone-api/main/blvip/inqire")
    public Object getBLVIP(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneMainService.getBLVIP(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 고객등급 저장
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "고객등급 저장",
                  notes = "고객등급 저장")
    @PostMapping("/phone-api/main/blvip/regist")
    public Object registBLVIP(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneMainService.registBLVIP(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }
    /**
     * 전화받기
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "전화 받기",
                  notes = "전화 받기")
    @PostMapping("/phone-api/main/call/recieve")
    public Object callReceive(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneMainService.registBLVIP(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }    
    
    /**
     * 상담이력 강제저장
     *
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "상담이력 저장",
                  notes = "상담이력 저장")
    @PostMapping("/phone-api/main/cnslt-hist/regist-force")
    public Object cnslForceRegist(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        objRetParams = phoneMainService.cnslForceRegist(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }
    
    /**
     * 신규고객팝업오류 처리
     * 221201
     *	
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "신규고객팝업 오류 처리-고객 존재여부 체크",
                  notes = "신규고객팝업 오류 처리-고객 존재여부 체크")
    @PostMapping("/phone-api/main/search-custId")
    public Object searchCustId(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        objRetParams = phoneMainService.searchCustId(mjsonParams);
        //최종결과값 반환
        log.debug("objRetParams"+objRetParams);
        return objRetParams;
    }
    
    /**
     * 만트럭 차량정보조회
     * 221208
     *
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "만트럭 차량정보 조회",
                  notes = "만트럭 차량정보 조회")
    @PostMapping("/phone-api/main/man-car/inqire")
    public Object getManCarInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        objRetParams = phoneMainService.getManCarInfo(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }
    
    /**
     * 만트럭 차량정보저장
     * 221209
     *
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "만트럭 차량정보 저장",
                  notes = "만트럭 차량정보 저장")
    @PostMapping("/phone-api/main/man-car/regist")
    public Object insertManCarInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        objRetParams = phoneMainService.insertManCarInfo(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }
    
    /**
     * 만트럭 지역별 서비스센터 조회
     *
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "만트럭 서비스센터 조회",
                  notes = "만트럭 서비스센터 조회")
    @PostMapping("/phone-api/main/man-center/inqire")
    public Object getManCenter(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        objRetParams = phoneMainService.getManCenter(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }
    
    /**
     * PDS 조회
     *
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "PDS조회",
                  notes = "PDS조회")
    @PostMapping("/phone-api/main/pds/inqire")
    public Object getPds(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        objRetParams = phoneMainService.getPds(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }
    
    /**
     * 퇴직연금 아웃바운드 조회
     * 230221
     *
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "퇴직연금 아웃바운드정보 조회",
                  notes = "퇴직연금 아웃바운드정보 조회")
    @PostMapping("/phone-api/main/pension-out/inqire")
    public Object getPensionOutInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        objRetParams = phoneMainService.getPensionOutInfo(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }
    
    /**
     * 퇴직연금 아웃바운드 정보저장
     * 230221
     *
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "퇴직연금 아웃바운드 정보저장",
                  notes = "퇴직연금 아웃바운드 정보저장")
    @PostMapping("/phone-api/main/pension-out/regist")
    public Object insertPensionOutInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        objRetParams = phoneMainService.insertPensionOutInfo(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }
    
    /**
     * 퇴직연금 아웃바운드 정보저장
     * 230221
     *
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "퇴직연금 아웃바운드 정보수정",
                  notes = "퇴직연금 아웃바운드 정보수정")
    @PostMapping("/phone-api/main/pension-out/update")
    public Object updatePensionOutInfo(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        objRetParams = phoneMainService.updatePensionOutInfo(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }
}