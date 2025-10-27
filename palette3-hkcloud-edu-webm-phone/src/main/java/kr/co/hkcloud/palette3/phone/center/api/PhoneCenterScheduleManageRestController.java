package kr.co.hkcloud.palette3.phone.center.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.center.app.PhoneCenterScheduleManageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/**
 * 스케쥴관리 REST 컨트롤러 클래스
 * 
 * @author dckim
 *
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneCenterScheduleManageRestController",
     description = "스케줄관리 REST 컨트롤러")
public class PhoneCenterScheduleManageRestController
{
    /** 스케쥴관리 서비스 */
    private final PhoneCenterScheduleManageService phoneCenterScheduleManageService;


    /**
     * 스케쥴관리 예약 콜백 현황 조회
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "스케쥴관리 예약 콜백 현황 조회",
                  notes = "스케쥴관리 예약 콜백 현황 조회")
    @PostMapping("/phone-api/center/schedule-manage/sttus/inqire")
    public Object selectSchedulMngList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성

        // 스케쥴관리 예약/콜백이력 목록 조회
        objRetParams = phoneCenterScheduleManageService.selectSchedulMngList(mjsonParams);

        return objRetParams;
    }


    /**
     * 스케쥴관리 예약 콜백 이력 데이터 조회
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "스케쥴관리 예약 콜백 이력 데이터 조회",
                  notes = "스케쥴관리 예약 콜백 이력 데이터 조회")
    @PostMapping("/phone-api/center/schedule-manage/hist/inqire")
    public Object selectSchedulHisList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성

        // 스케쥴관리 예약/콜백이력 목록 조회
        objRetParams = phoneCenterScheduleManageService.selectSchedulHisList(mjsonParams);

        return objRetParams;
    }


    /**
     * 스케쥴관리 수정
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "스케쥴관리 수정 데이터 처리",
                  notes = "스케쥴관리 수정 데이터 처리")
    @PostMapping("/phone-api/center/schedule-manage/modify")
    public Object updateSchedulMng(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정

        //mjsonParams.setBlankToNull(0);                      //빈값에 대한 NULL 처리
        objRetParams = phoneCenterScheduleManageService.updateSchedulMng(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 스케쥴관리 처리상태 변경
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "스케쥴관리 수정 데이터 처리",
                  notes = "스케쥴관리 수정 데이터 처리")
    @PostMapping("/phone-api/center/schedule-manage/proces-sttus/modify")
    public Object updateSchedulAlloc(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정

        //mjsonParams.setBlankToNull(0);                      //빈값에 대한 NULL 처리
        objRetParams = phoneCenterScheduleManageService.updateSchedulAlloc(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 스케쥴관리 상담원재할당
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "스케쥴관리 수정 데이터 처리",
                  notes = "스케쥴관리 수정 데이터 처리")
    @PostMapping("/phone-api/center/schedule-manage/cnslr-reasgn/process")
    public Object updateSchedulCsltId(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정

        //mjsonParams.setBlankToNull(0);                      //빈값에 대한 NULL 처리
        objRetParams = phoneCenterScheduleManageService.updateSchedulCsltId(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 스케쥴관리 상담원 부서 검색
     * 
     * @param  inHashMap
     * @return           TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "스케쥴관리 상담원 부서 검색",
                  notes = "스케쥴관리 상담원 부서 검색")
    @PostMapping("/phone-api/center/schedule-manage/dept/inqire")
    public Object selectUserDeptSrch(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성

        objRetParams = phoneCenterScheduleManageService.selectUserDeptSrch(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 조합검색 팝업 조회
     * 
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "조합검색 팝업 조회",
                  notes = "조합검색 팝업 조회")
    @PostMapping("/phone-api/center/assc/inqire-pop")
    public Object selectRtnUserInfoPop(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        objRetParams = phoneCenterScheduleManageService.selectRtnUserInfoPop(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

}
