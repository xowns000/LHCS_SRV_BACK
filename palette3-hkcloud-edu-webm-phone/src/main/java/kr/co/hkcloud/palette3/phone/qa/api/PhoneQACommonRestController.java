package kr.co.hkcloud.palette3.phone.qa.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.qa.app.PhoneQACommonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController("TwbQamCommonRestController")
@Api(value = "TwbQamCommonRestController",
     description = "QA평가 공통 REST 컨트롤러")
public class PhoneQACommonRestController
{
    private final PhoneQACommonService phoneQACommonService;


    /**
     * 서버에서 전월의 마지막 날짜를 가져온다.
     * 
     * @param  mjsonParams
     * @return                     TelewebJSON 형식의 조회결과 데이터
     * @throws TelewebApiException
     */
    @ApiOperation(value = "서버에서 전월의 마지막 날짜를 가져온다.",
                  notes = "서버에서 전월의 마지막 날짜를 가져온다.")
    @PostMapping("/phone-api/qa/common/lsmth-last-day/inqire")
    public Object selectRtnBeforeMonthLastDay(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //client에서 전송받은 파라메터 생성
//        TelewebJSON mjsonParams = (TelewebJSON) inHashMap.get("mjsonParams");
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);
        //DAO검색 메서드 호출

        objRetParams = phoneQACommonService.selectRtnBeforeMonthLastDay(mjsonParams);
        //최종결과값 반환
        return objRetParams;
    }
}
