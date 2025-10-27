package kr.co.hkcloud.palette3.svy.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.svy.app.SvyResultService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SvyResultRestController",
     description = "설문지 컨트롤러")
public class SvyResultRestController
{
    private final SvyResultService svyResultService;

    @ApiOperation(value = "분석결과 요약 조회",
            notes = "분석결과 요약을 조회한다.")
    @PostMapping("/api/svy/result/selectsummylist")
    public Object selectSummyList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
        return svyResultService.selectSummyList(jsonParam);
    }
    @ApiOperation(value = "분석결과 항목 조회",
            notes = "분석결과 항목 조회한다.")
    @PostMapping("/api/svy/result/selectsummyitem")
    public Object selectSummyItem(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
        return svyResultService.selectSummyItem(jsonParam);
    }
    @ApiOperation(value = "분석결과 항목 자세히 조회",
            notes = "분석결과 항목을 자세히 조회한다.")
    @PostMapping("/api/svy/result/selectdetaillist")
    public Object selectDetailList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
        return svyResultService.selectDetailList(jsonParam);
    }
}
