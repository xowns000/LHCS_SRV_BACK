package kr.co.hkcloud.palette3.phone.script.api;


import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import kr.co.hkcloud.palette3.common.twb.model.TelewebJSON;
import kr.co.hkcloud.palette3.config.aspect.TelewebJsonParam;
import kr.co.hkcloud.palette3.exception.teleweb.TelewebApiException;
import kr.co.hkcloud.palette3.phone.script.app.PhoneScriptInqireService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@RequiredArgsConstructor
@RestController
@Api(value = "PhoneScriptInqireRestController",
     description = "스크립트관리 서비스콜 REST 컨트롤러")
public class PhoneScriptInqireRestController
{
    private final PhoneScriptInqireService phoneScriptInqireService;


    /**
     * 스크립트트리 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "스크립트트리 조회",
                  notes = "스크립트트리 조회")
    @PostMapping("/phone-api/script/inqire/tr/inqire")
    public Object selectRtnScriptTree(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneScriptInqireService.selectRtnScriptTree(mjsonParams);

        return objRetParams;
    }


    /**
     * 스크립트 리스트 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "스크립트리스트 조회",
                  notes = "스크립트리스트 조회")
    @PostMapping("/phone-api/script/inqire/list")
    public Object selectMainScriptList(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        log.debug("mjsonParams" + mjsonParams);
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneScriptInqireService.selectMainScriptList(mjsonParams);

        return objRetParams;
    }


    /**
     * 스크립트상세조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "스크립트상세조회",
                  notes = "스크립트상세조회")
    @PostMapping("/phone-api/script/inqire/detail")
    public Object selectRtnScriptDetail(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneScriptInqireService.selectRtnScriptDetail(mjsonParams);

        return objRetParams;
    }


    /**
     * 스크립트 트리 클릭 시 해당 스크립트의 하위 스크립트 조회
     * 
     * @param  mjsonParams
     * @return
     */
    @ApiOperation(value = "스크립트하위리스트 조회",
                  notes = "스크립트하위리스트 조회")
    @PostMapping("/phone-api/script/inqire/low/list")
    public Object selectRtnLowScript(@TelewebJsonParam TelewebJSON mjsonParams) throws TelewebApiException
    {
        TelewebJSON objRetParams = new TelewebJSON(mjsonParams);

        objRetParams = phoneScriptInqireService.selectRtnLowScript(mjsonParams);

        return objRetParams;
    }
}
