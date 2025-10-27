package kr.co.hkcloud.palette3.phone.callback.api;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.callback.app.PhoneCallbackListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * packageName : kr.co.hkcloud.palette3.phone.callback.api
 * fileName : PhoneCallbackListRestController.java
 * author : USER
 * date : 2023-10-27
 * description : 콜백조회 REST 컨트롤러
 * ===========================================================
 * DATE AUTHOR NOTE
 * -----------------------------------------------------------
 * 2023-10-27 Orange 최초 생성
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneCallbackListRestController", description = "콜백조회 REST 컨트롤러")
public class PhoneCallbackListRestController {

    private final PhoneCallbackListService phoneCallbackListService;

    /**
     * 콜백정보 조회
     *
     * @return TelewebJSON 형식의 조회결과 데이터
     */
    @ApiOperation(value = "콜백 조회", notes = "콜백정보를 조회한다.")
    @PostMapping("/phone-api/callback/inqire/list")
    public Object selectClbkInqList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {
        //client에서 전송받은 파라메터 생성
        // TelewebJSON mjsonParams     = (TelewebJSON)inHashMap.get("mjsonParams");

        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        //DAO 검색 메소드 호출
        objRetParams = phoneCallbackListService.selectClbkInqList(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 콜백처리결과 데이터 처리
     *
     * @return TelewebJSON 형식의 처리 결과 데이터
     */
    @ApiOperation(value = "콜백처리결과 데이터  처리", notes = "콜백처리결과 데이터  처리")
    @PostMapping("/phone-api/callback/inqire/modify")
    public Object updateClbkInq(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException {

        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);                    //반환 파라메터 생성
        String strTransFlag = mjsonParams.getString("DATA_FLAG");                   //화면에서 전송된 플래그 설정

        //콜백결과 수정
        mjsonParams.setBlankToNull(0);                      //빈값에 대한 NULL 처리
        objRetParams = phoneCallbackListService.updateClbkInq(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 콜백 이력 목록
     */
    @ApiOperation(value = "콜백 이력 목록", notes = "콜백 이력 목록을 조회한다.")
    @PostMapping("/phone-api/callback/inqire/clbkStatHistList")
    public Object clbkStatHistList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(jsonParam); //반환 파라메터 생성
        if (StringUtils.isEmpty(jsonParam.getString("IS_ALL_ENV")))
            jsonParam.setString("IS_ALL_ENV", "false");

        objRetParams = phoneCallbackListService.clbkStatHistList(jsonParam); //상담원별 콜백 목록

        objRetParams.setDataObject("MONITOR", phoneCallbackListService.cuslClbkMonitor(jsonParam)); //상담원별 콜백 상태

        objRetParams.setDataObject("MONITOR_RS", phoneCallbackListService.cuslClbkDtlMonitor(jsonParam)); //상담원별 처리결과별 상태

        //최종결과값 반환
        return objRetParams;
    }

    /**
     * 콜백
     */
    @ApiOperation(value = "콜백 알람 모니터링용", notes = "콜백이 있을 경우 상담직원이 인지할 수 있도록 우측 사이드바의 해당 아이콘에 에니메이션 효과를 적용 용도.")
    @PostMapping("/phone-api/callback/inqire/callbackAlarmMonitor")
    public Object callbackAlarmMonitor(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        TelewebJSON objRetParams = phoneCallbackListService.cuslClbkMonitor(jsonParam); //반환 파라메터 생성
        return objRetParams;
    }
}
