package kr.co.hkcloud.palette3.svy.api;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.svy.app.SvyPreviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "SvyPreviewRestController",
     description = "설문지 컨트롤러")
public class SvyPreviewRestController
{
    private final SvyPreviewService svyPreviewService;

    @ApiOperation(value = "설문지 메인항목 조회",
                  notes = "설문지 메인항목을 조회한다.")
    @PostMapping("/api/svy/preview/selectmainlist")
    public Object selectMainList(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
        return svyPreviewService.selectMainList(jsonParam);
    }

    @ApiOperation(value = "개인정보수집동의",
            notes = "개인정보수집동의를 한다.")
    @PostMapping("/api/svy/preview/upsertterms")
    public Object upsertTerms(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
        return svyPreviewService.upsertTerms(jsonParam);
    }

    @ApiOperation(value = "설문답변저장",
            notes = "설문답변을 저장한다.")
    @PostMapping("/api/svy/preview/insertitem")
    public Object insertItem(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
        return svyPreviewService.insertItem(jsonParam);
    }

    @ApiOperation(value = "url복호화",
            notes = "url복호화.")
    @PostMapping("/api/svy/preview/aesurldecrypt")
    public Object aesUrlDecrypt(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException
    {
        return svyPreviewService.aesUrlDecrypt(jsonParam);
    }
    
    @ApiOperation(value = "url복호화",
        notes = "url복호화.")
    @PostMapping("/api/svy/preview/deleteGroupRspns")
    public Object deleteGroupRspns(@TelewebJsonParam TelewebJSON jsonParam) throws TelewebApiException {
        return svyPreviewService.deleteGroupRspns(jsonParam);
    }
}
