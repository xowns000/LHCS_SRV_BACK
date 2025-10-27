package kr.co.hkcloud.palette3.common.date.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.date.app.DateCmmnService;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.NoBizLog;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "DateCmmnRestController",
     description = "날짜공통 REST 컨트롤러")
public class DateCmmnRestController
{
    private final DateCmmnService dateCmmnService;


    /**
     * 서버의 현재 일시를 포맷을 적용하여 검색한다.
     * 
     * @param  mjsonParams
     * @return
     */
    @NoBizLog
    @ApiOperation(value = "서버현재시간",
                  notes = "서버의 현재 일시를 포맷을 적용하여 검색한다.")
    @PostMapping("/api/date/common/current-time/inqry")
    public Object selectServerDate(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = dateCmmnService.selectServerDate(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 기준시간 기준으로 날짜를 더한 결과 일을 검색한다.
     * 
     * @param  mjsonParams
     * @return
     */
    @NoBizLog
    @ApiOperation(value = "기준시간에 일을 더한 일",
                  notes = "기준시간 기준으로 날짜를 더한 결과 일을 검색한다.")
    @PostMapping("/api/date/common/add-date/inqry")
    public Object selectAddDate(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = dateCmmnService.selectAddDate(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 두날짜의 차이를 구분에 따라 계산하여 검색한다.
     * 
     * @param  mjsonParams
     * @return
     */
    @NoBizLog
    @ApiOperation(value = "두 날짜 차이 계산",
                  notes = "두날짜의 차이를 구분에 따라 계산하여 검색한다.")
    @PostMapping("/api/date/common/date-dffrnc-calc/inqry")
    public Object selectDiffDate(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = dateCmmnService.selectDiffDate(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 기준일자의 마지막일을 검색한다.
     * 
     * @param  mjsonParams
     * @return
     */
    @NoBizLog
    @ApiOperation(value = "해당 월의 마지막 날짜",
                  notes = "기준일자의 마지막일을 검색한다.")
    @PostMapping("/api/date/common/last-day-mt/inqry")
    public Object selectLastDay(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = dateCmmnService.selectLastDay(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 서버시간 기준으로 오늘날짜, 3일전, 7주일전, 등의 날짜를 검색한다.
     * 
     * @param  mjsonParams
     * @return
     */
    @NoBizLog
    @ApiOperation(value = "전 일자 반환",
                  notes = "서버시간 기준으로 오늘날짜, 3일전, 7주일전,  등의 날짜를 검색한다.")
    @PostMapping("/api/date/common/prev-date/inqry")
    public Object selectDatePattern(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = dateCmmnService.selectDatePattern(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }


    /**
     * 서버시간 기준으로 오늘날짜, 3일후, 7일후, 1달후, 3달후의 날짜를 검색한다.
     * 
     * @param  mjsonParams
     * @return
     */
    @NoBizLog
    @ApiOperation(value = "후 일자 반환",
                  notes = "서버시간 기준으로 오늘날짜, 3일후, 7일후, 1달후, 3달후의 날짜를 검색한다.")
    @PostMapping("/api/date/common/date-after/inqry")
    public Object selectAfterDatePattern(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        //반환 파라메터 생성
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = dateCmmnService.selectAfterDatePattern(mjsonParams);

        //최종결과값 반환
        return objRetParams;
    }

}
